package com.wei.elk.es.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.wei.elk.es.announce.ESId;
import com.wei.elk.es.common.entity.ESBatchDataEntity;
import com.wei.elk.es.common.vo.ESBatchDataVO;
import com.wei.elk.es.util.ESIdFieldUtil;
import com.wei.elk.es.util.ESOperateUtil;
import com.wei.elk.es.util.SpringBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/12/1 13:53:56
 */
// @Component
public class ESEntityListener<T extends ESBatchDataEntity> extends AnalysisEventListener<T> {

    private static final Logger logger = LoggerFactory.getLogger(ESEntityListener.class);

    private static final int BATCH_COUNT = 2000;

    private ESOperateUtil esOperateUtil = SpringBeanUtil.getBean(ESOperateUtil.class);

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 数据列表
     */
    List<T> list=new ArrayList<>();

    public ESEntityListener(String indexName) {
        this.indexName = indexName;
    }

    @Override
    public void invoke(T data, AnalysisContext analysisContext) {
        logger.info("解析到一条数据-------->{}",data);
        ESIdFieldUtil.analysisId(data);
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        ESBatchDataVO batchDataVO=new ESBatchDataVO();
        batchDataVO.setIndexName(indexName);
        batchDataVO.setData(list);
        saveData();
        logger.info("所有数据解析完成！");
    }

    private void saveData(){
        logger.info("--------->{}",list.toString());
        ESBatchDataVO batchDataVO=new ESBatchDataVO();
        batchDataVO.setIndexName(indexName);
        batchDataVO.setData(list);
        try {
            esOperateUtil.batchInsert(batchDataVO);
        } catch (IOException e) {
            logger.error("ESEntityListener.saveData()批量导入es数据出错，参数,{}", JSON.toJSONString(batchDataVO));
        }
    }

}
