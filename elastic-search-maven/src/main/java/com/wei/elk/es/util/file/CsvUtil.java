package com.wei.elk.es.util.file;

import com.alibaba.fastjson.JSON;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvValidationException;
import com.wei.elk.es.common.constant.ESIndexEntityMapping;
import com.wei.elk.es.common.entity.ESBatchDataEntity;
import com.wei.elk.es.common.vo.ESBatchDataVO;
import com.wei.elk.es.util.ESIdFieldUtil;
import com.wei.elk.es.util.ESOperateUtil;
import com.wei.elk.es.util.IdUtil;
import com.wei.elk.es.util.SpringBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/12/1 17:56:20
 */
public class CsvUtil {

    private static final Logger logger = LoggerFactory.getLogger(CsvUtil.class);

    private static final String ERROR_FILE_PATH = "C:\\temp\\";
    //双引号包围的单元格中的内容正则表达式：
    //1、双引号一定成对出现("{2})*
    //2、成对双引号前可能会出现非双引号([^"]"{2})* 比如"""123"""""可以看做"【""】【123""】【""】"
    //3、但是，比如"a""""b""""""c"可以看做"【a""】【""】【b""】【""""】c"，最后的c无法匹配，所以最后还要加上可能多于出来的非双引号[^"]*
    //最终单元格中的内容正则表达式为： ([^"]*"{2})*[^"]*
    private static final Pattern p = Pattern.compile("^\"([^\"]*\"{2})*[^\"]*(\",|\"$)");//双引号包围的单元格结束符号一定是",和"$

    // private ESOperateUtil esOperateUtil = SpringBeanUtil.getBean(ESOperateUtil.class);
    /**
     * 解析csv文件并转成bean（方法一）
     * 通过上传的方式
     * @param file 文件
     * @Param indexName 索引名
     * @Param columnFieldMap 列号和实体属性名对应关系map
     * @return
     */
    public static <T> void getCsvData1(MultipartFile file, String indexName, Map<Integer, String> columnFieldMap) {
        try {
            InputStream is = file.getInputStream();
            getLocalCsvData1(is, indexName, columnFieldMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 解析csv文件并转成bean（方法一）
     * 读取本地文件的方式
     * @param file 文件
     * @Param indexName 索引名
     * @Param columnFieldMap 列号和实体属性名对应关系map
     * @return
     */
    public static <T> void getCsvData1(File file, String indexName, Map<Integer, String> columnFieldMap) {
        try {
            InputStream is = new FileInputStream(file);
            getLocalCsvData1(is, indexName, columnFieldMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析csv文件并转成bean（方法一）
     * 读取本地文件的方式
     * @param filePath 文件路径
     * @Param indexName 索引名
     * @Param columnFieldMap 列号和实体属性名对应关系map
     * @return
     */
    public static <T> void getCsvData1(String filePath, String indexName, Map<Integer, String> columnFieldMap) {
        try {
            InputStream is = new FileInputStream(filePath);
            getLocalCsvData1(is, indexName, columnFieldMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析csv文件并转成bean（方法二）
     *
     * @param file csv文件
     * @return 数组
     */
    public static void getCsvData2(MultipartFile file, String indexName, Map<Integer, String> columnFieldMap) {

        InputStream is = null;
        try {
            is = file.getInputStream();
            getCsvData2(is, indexName, columnFieldMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析csv文件并转成bean（方法二）
     *
     * @param file csv文件
     * @return 数组
     */
    public static void getCsvData2(File file, String indexName, Map<Integer, String> columnFieldMap) {

        try {
            InputStream is = new FileInputStream(file);
            getCsvData2(is, indexName, columnFieldMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析csv文件并转成bean（方法二）
     *
     * @param filePath csv文件路径
     * @return 数组
     */
    public static void getCsvData2(String filePath, String indexName, Map<Integer, String> columnFieldMap) {

        try {
            InputStream is = new FileInputStream(filePath);
            getCsvData2(is, indexName, columnFieldMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析csv文件并转成bean（方法四）
     * 通过上传的方式
     * @param file 文件
     * @Param indexName 索引名
     * @Param columnFieldMap 列号和实体属性名对应关系map
     * @return
     */
    public static <T> void getCsvData4(MultipartFile file, String indexName, Map<Integer, String> columnFieldMap) {
        try {
            InputStream is = file.getInputStream();
            getCsvDataMethod4(is, indexName, columnFieldMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 解析csv文件并转成bean（方法四）
     * 读取本地文件的方式
     * @param file 文件
     * @Param indexName 索引名
     * @Param columnFieldMap 列号和实体属性名对应关系map
     * @return
     */
    public static <T> void getCsvData4(File file, String indexName, Map<Integer, String> columnFieldMap) {
        try {
            InputStream is = new FileInputStream(file);
            getCsvDataMethod4(is, indexName, columnFieldMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析csv文件并转成bean（方法四）
     * 读取本地文件的方式
     * @param filePath 文件路径
     * @Param indexName 索引名
     * @Param columnFieldMap 列号和实体属性名对应关系map
     * @return
     */
    public static <T> void getCsvData4(String filePath, String indexName, Map<Integer, String> columnFieldMap) {
        try {
            InputStream is = new FileInputStream(filePath);
            getCsvDataMethod4(is, indexName, columnFieldMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析csv文件并转成bean（方法五）
     * 通过上传的方式
     * @param file 文件
     * @Param indexName 索引名
     * @Param columnFieldMap 列号和实体属性名对应关系map
     * @return
     */
    public static <T> void getCsvData5(MultipartFile file, String indexName, Map<Integer, String> columnFieldMap) {
        try {
            InputStream is = file.getInputStream();
            getCsvDataMethod5(is, indexName, columnFieldMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 解析csv文件并转成bean（方法五）
     * 读取本地文件的方式
     * @param file 文件
     * @Param indexName 索引名
     * @Param columnFieldMap 列号和实体属性名对应关系map
     * @return
     */
    public static <T> void getCsvData5(File file, String indexName, Map<Integer, String> columnFieldMap) {
        try {
            InputStream is = new FileInputStream(file);
            getCsvDataMethod5(is, indexName, columnFieldMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析csv文件并转成bean（方法五）
     * 读取本地文件的方式
     * @param filePath 文件路径
     * @Param indexName 索引名
     * @Param columnFieldMap 列号和实体属性名对应关系map
     * @return
     */
    public static <T> void getCsvData5(String filePath, String indexName, Map<Integer, String> columnFieldMap) {
        try {
            InputStream is = new FileInputStream(filePath);
            getCsvDataMethod5(is, indexName, columnFieldMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



    /**
     * 解析csv文件并转成bean（方法一）,逐行读取，按逗号分割
     * 缺陷：当存在某格子为空时，得到的数组长度和列数不一致，会报错
     *
     * @param is 输入流
     * @Param indexName 索引名
     * @Param columnFieldMap 列号和实体属性名对应关系map
     * @return
     */
    public static <T> void getLocalCsvData1(InputStream is, String indexName, Map<Integer, String> columnFieldMap) {
        ArrayList<T> entities = new ArrayList<>();

        InputStreamReader in = null;
        String s = null;
        Class<T> clazz = ESIndexEntityMapping.indexEntityMap.get(indexName);
        Field[] fields = clazz.getDeclaredFields();
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        String allPath = ERROR_FILE_PATH + str + ".txt";
        File errorLog = new File(allPath);
        try {
            errorLog.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // Constructor<T> constructor = clazz.getDeclaredConstructor();
            // constructor.setAccessible(true);
            in = new InputStreamReader(is, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(in);
            String line = null;
            // 如果有标题行，将标题行去除
            line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                // "汪明雨","522425200407263021","贵州省","汪勇","15957212149","大潘抖38号",,,
                // 当某行数据如上时，得到数组长度6，与预期的9不符，解析出错
                String[] split = line.split(",");
                try {
                    T entity = clazz.newInstance();
                    columnFieldMap.forEach((column, fieldName) -> {
                        try {
                            Field field = clazz.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            field.set(entity, split[column].replaceAll("\"", ""));
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
                    ESIdFieldUtil.analysisId((ESBatchDataEntity)entity);
                    if (StringUtils.isBlank(((ESBatchDataEntity) entity).getId())) {
                        ((ESBatchDataEntity) entity).setId(IdUtil.getRandomID());
                    }
                    entities.add(entity);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    logger.error("解析出错，错误数据为：" + line);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(allPath, true)));
                    bw.write(line);
                    bw.newLine();
                    bw.flush();
                    logger.info("数据出错，数据为：" + line);
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
        return;
    }

    /**
     * 解析csv文件并转成bean（方法二）
     *
     * @param inputStream csv文件
     * @return 数组
     */
    public static void getCsvData2(InputStream inputStream, String indexName, Map<Integer, String> columnFieldMap) {

        List<String[]> list = new ArrayList<String[]>();
        int i = 0;
        try {
            CSVReader csvReader = new CSVReaderBuilder(
                    new BufferedReader(
                            new InputStreamReader(inputStream, "utf-8"))).build();
            Iterator<String[]> iterator = csvReader.iterator();
            while (iterator.hasNext()) {
                String[] next = iterator.next();
                //去除第一行的表头，从第二行开始
                if (i >= 1) {
                    list.add(next);
                }
                i++;
            }
            return;
        } catch (Exception e) {
            System.out.println("CSV文件读取异常");
            return;
        }
    }

    /**
     * 解析csv文件并转成bean（方法三）
     *
     * @param file  csv文件
     * @param clazz 类
     * @param <T>   泛型
     * @return 泛型bean集合
     */
    public static <T> List<T> getCsvDataMethod3(MultipartFile file, Class<T> clazz) {
        InputStreamReader in = null;
        CsvToBean<T> csvToBean = null;
        try {
            in = new InputStreamReader(file.getInputStream(), "utf-8");
            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(clazz);
            csvToBean = new CsvToBeanBuilder<T>(in).withMappingStrategy(strategy).build();
        } catch (Exception e) {
            logger.error("数据转化失败");
            return null;
        }
        return csvToBean.parse();
    }

   /* *//**
     * 解析csv文件并转成bean（方法三）
     *
     * @param file  csv文件
     * @param clazz 类
     * @param <T>   泛型
     * @return 泛型bean集合
     *//*
    public static <T> List<T> getCsvDataMethod3My(MultipartFile file,String indexName, Map<Integer, String> columnFieldMap, Class<T> clazz) {
        InputStreamReader in = null;
        CsvToBean<T> csvToBean = null;
        try {
            in = new InputStreamReader(file.getInputStream(), "utf-8");
            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(clazz);
            csvToBean = new CsvToBeanBuilder<T>(in).withMappingStrategy(strategy).build();
        } catch (Exception e) {
            logger.error("数据转化失败");
            return null;
        }
        return csvToBean.parse();
    }*/

    /**
     * 解析csv文件并转成bean（方法四，结合opencsv自己实现的方法）
     * 此方法不需要在实体类上添加注解，需传入解析Map，将列与字段对应，更加灵活
     * @param is  输入流
     * @param indexName 索引名
     * @param <T>   泛型
     * @return 泛型bean集合
     */
    public static <T> void getCsvDataMethod4(InputStream is,String indexName, Map<Integer, String> columnFieldMap) {
        ArrayList<T> entities = new ArrayList<>();

        //InputStreamReader in = null;
        CSVReader reader = null;
        try {
            reader = new CSVReader(new InputStreamReader(is, "utf8"));
            String[] strArr = null;
            Class<T> clazz = ESIndexEntityMapping.indexEntityMap.get(indexName);
            String fileName= new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date());
            String allPath = ERROR_FILE_PATH + fileName + ".txt";
            File errorLog = new File(allPath);
            errorLog.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(allPath, true)));
            // 如果有标题行，将标题行去除
            strArr = reader.readNext();
            while((strArr = reader.readNext()) != null) {
                try {
                    T entity = clazz.newInstance();
                    for (Map.Entry<Integer, String> entry : columnFieldMap.entrySet()) {
                        Integer column = entry.getKey();
                        String fieldName = entry.getValue();
                        Field field = clazz.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        field.set(entity, strArr[column]);
                    }
                    ESIdFieldUtil.analysisId((ESBatchDataEntity)entity);
                    if (StringUtils.isBlank(((ESBatchDataEntity) entity).getId())) {
                        ((ESBatchDataEntity) entity).setId(IdUtil.getRandomID());
                    }
                    entities.add(entity);
                } catch (Exception e) {
                    bw.write(strArr.toString());
                    bw.newLine();
                    bw.flush();
                    logger.error("数据出错，数据为：" + JSON.toJSONString(strArr));
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
        } catch (Exception e) {
            logger.error("读取出错！");
        }
        // logger.info("----------------------------------------数据导入完毕-----------------------------------------");
        return;
    }


    /**
     * 解析csv文件并转成bean（方法四，结合opencsv自己实现的方法）
     * 此方法不需要在实体类上添加注解，需传入解析Map，将列与字段对应，更加灵活
     * @param is  输入流
     * @param indexName 索引名
     * @param <T>   泛型
     * @return 泛型bean集合
     */
    public static <T> void getCsvDataMethod5(InputStream is,String indexName, Map<Integer, String> columnFieldMap) {
        BufferedReader br = null;
        ArrayList<T> entities = new ArrayList<>();
        try {
            br = new BufferedReader(new InputStreamReader(is, "utf8"));
            String line;
            Class<T> clazz = ESIndexEntityMapping.indexEntityMap.get(indexName);
            // 将解析错误的数据写入文件中
            String fileName= new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date());
            String allPath = ERROR_FILE_PATH + fileName + ".txt";
            File errorLog = new File(allPath);
            errorLog.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(allPath, true)));
            // 如果有标题行，将标题行去除
            br.readLine();
            while ((line = br.readLine()) != null) {
                try {
                    List<String> row = new ArrayList<>();
                    if (!"".equals(line.trim())) {
                        getCells(line, row);
                        T entity = clazz.newInstance();
                        for (Map.Entry<Integer, String> entry : columnFieldMap.entrySet()) {
                            Integer column = entry.getKey();
                            String fieldName = entry.getValue();
                            Field field = clazz.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            field.set(entity, row.get(column));
                        }
                        ESIdFieldUtil.analysisId((ESBatchDataEntity) entity);
                        if (StringUtils.isBlank(((ESBatchDataEntity) entity).getId())) {
                            ((ESBatchDataEntity) entity).setId(IdUtil.getRandomID());
                        }
                        entities.add(entity);
                    }
                } catch (Exception e) {
                    bw.write(line);
                    bw.newLine();
                    bw.flush();
                    logger.error("数据出错，数据为：" + line);
                }
                if (entities.size() > 2000) {
                    saveData(indexName, entities);
                    entities.clear();
                }
            }
            // 最后把不足2000的数据入库
            if (!CollectionUtils.isEmpty(entities)) {
                saveData(indexName, entities);
                entities.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void getCells(final String line, List<String> row) {
        String cell;
        if (line.startsWith("\"")) {//有双引号包围的单元格
            Matcher m = p.matcher(line);
            if (m.find()) {
                cell = m.group();
                if (cell.endsWith(",")) {
                    cell = cell.substring(0, cell.length() - 1);
                }
                if (cell.startsWith("\"") && cell.endsWith("\"")) {
                    cell = cell.substring(1, cell.length() - 1);
                    cell = cell.replaceAll("\"\"", "\"");
                }
                row.add(cell);
                String nline = line.substring(m.end());
                if ("".equals(nline)) {//一条完整的行结束
                    return;
                }
                getCells(line, row);
            } else {
                return;
            }
        } else {//无双引号包围的单元格
            if (line.contains(",")) {
                cell = line.substring(0, line.indexOf(","));
                row.add(cell.trim());
                String nline = line.substring(line.indexOf(",") + 1);
                getCells(nline, row);
            } else {//一条完整的行结束
                row.add(line.trim());
                return;
            }
        }
    }


    private static String splitResult(String once) {
        String result = "";
        for (int i = 0; i < once.length(); i++) {
            if (once.charAt(i) != '"') {
                result += once.charAt(i);
            }
        }
        return result;
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
            logger.error("CsvUtil.saveData()批量导入es数据出错，参数,{}", JSON.toJSONString(batchDataVO));
        }
    }

}
