package com.wei.elk.es.util.file;

import com.alibaba.fastjson.JSON;
import com.wei.elk.es.common.constant.ESIndexEntityMapping;
import com.wei.elk.es.common.entity.ESBatchDataEntity;
import com.wei.elk.es.common.vo.ESBatchDataVO;
import com.wei.elk.es.util.ESIdFieldUtil;
import com.wei.elk.es.util.ESOperateUtil;
import com.wei.elk.es.util.IdUtil;
import com.wei.elk.es.util.SpringBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.core.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/12/1 17:56:33
 */
public class TxtUtil {

    private static final Logger logger = LoggerFactory.getLogger(TxtUtil.class);

    /**
     * 解析txt文件并转成bean（方法一）
     * 读取本地文件的方式
     * @param file 文件
     * @Param indexName 索引名
     * @Param columnFieldMap 列号和实体属性名对应关系map
     * @return
     */
    public static <T> void getTxtData1(File file, String indexName, Map<Integer, String> columnFieldMap) {
        try {
            InputStream is = new FileInputStream(file);
            getLocalTxtData1(is, indexName, columnFieldMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析txt文件并转成bean（方法一）
     *
     * @param is 输入流
     * @Param indexName 索引名
     * @Param columnFieldMap 列号和实体属性名对应关系map
     * @return
     */
    public static <T> void getLocalTxtData1(InputStream is, String indexName,final Map<Integer, String> columnFieldMap) {
        ArrayList<T> entities = new ArrayList<>();

        InputStreamReader in = null;
        String s = null;
        Class<T> clazz = ESIndexEntityMapping.indexEntityMap.get(indexName);
        Field[] fields = clazz.getDeclaredFields();
        try {
            // Constructor<T> constructor = clazz.getDeclaredConstructor();
            // constructor.setAccessible(true);
            in = new InputStreamReader(is, "utf8");
            BufferedReader bufferedReader = new BufferedReader(in);
            String line = null;
            while ((line = bufferedReader.readLine()) != null && line.length() != 0) {
                String[] split = line.split("---");
                try {
                    T entity = clazz.newInstance();
                    for (Map.Entry<Integer, String> entry : columnFieldMap.entrySet()) {
                        Integer column = entry.getKey();
                        String fieldName = entry.getValue();
                        try {
                            Field field = clazz.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            if ("\\N".equals(split[column])) {
                                split[column] = null;
                            }
                            field.set(entity, split[column]);
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            logger.error("解析出错，错误数据为：" + line);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\temp\\error.txt", true)));
                            bw.write(line);
                            bw.newLine();
                            bw.flush();
                        }
                    }
                    ESIdFieldUtil.analysisId((ESBatchDataEntity)entity);
                    if (StringUtils.isBlank(((ESBatchDataEntity) entity).getId())) {
                        ((ESBatchDataEntity) entity).setId(IdUtil.getRandomID());
                    }
                    entities.add(entity);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (entities.size() > 2000) {
                    saveData(indexName, entities);
                    entities.clear();
                }
            }
            if (!CollectionUtils.isEmpty(entities)) {
                saveData(indexName, entities);
                entities.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("--------------------------------------数据解析完毕------------------------------------------------");
        return;
    }

    private static void saveData(String indexName, List list){
        logger.info("--------->{}",list.toString());
        ESBatchDataVO batchDataVO=new ESBatchDataVO();
        batchDataVO.setIndexName(indexName);
        batchDataVO.setData(list);
        try {
            ESOperateUtil esOperateUtil = SpringBeanUtil.getBean(ESOperateUtil.class);
            esOperateUtil.batchInsert(batchDataVO);
        } catch (IOException e) {
            logger.error("TxtUtil.saveData()批量导入es数据出错，参数,{}", JSON.toJSONString(batchDataVO));
        }
    }
}
