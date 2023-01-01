package com.wei.elk.es.util.file;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.wei.elk.es.common.constant.ESIndexEntityMapping;
import com.wei.elk.es.data.huji.entity.HujiEntity;
import com.wei.elk.es.handler.esfieldtypehandler.ESFieldAdapter;
import com.wei.elk.es.handler.esfileuploadhandler.ESFileUpAdapter;
import com.wei.elk.es.handler.esfileuploadhandler.ESFileUpHandler;
import com.wei.elk.es.listener.ESEntityListener;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/12/1 13:44:54
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    /***
     * @description 文件导入数据解析
     * @param filePath 文件名（文件路径）
     * @param indexName 对应es的索引
     * @param columnFieldMap  列和属性名的对应关系 例 {1："id"}
     * @return void
     * @author
     * @date 2022/12/2 2:11
     */
    public static void fileImport(String filePath, String indexName, Map<Integer, String> columnFieldMap) {
        File file = new File(filePath);
        fileImport(file, indexName, columnFieldMap);
    }

    public static void fileImport(File file, String indexName, Map<Integer, String> columnFieldMap) {
        List<String> files = new ArrayList<>();
        travelFile(file, files);
        for (String f : files) {
            String fileType = getMimeType(new File(f));
            ESFileUpHandler handler = ESFileUpAdapter.getHandler(fileType);
            if (null != handler) {
                handler.handle(new File(f), indexName, columnFieldMap);
            }
        }
        logger.info("----------------------------------------------数据导入完毕-----------------------------------------------");
        return;
    }

    /***
     * @description 文件导入数据解析
     * @param filePath 文件名（文件路径）
     * @param indexName 对应es的索引
     * @param columnFieldMap  列和属性名的对应关系 例 {1："id"}
     * @return void
     * @author
     * @date 2022/12/2 2:11
     */
    public static void fileImport(String filePath, String indexName, String split, Map<Integer, String> columnFieldMap) {
        File file = new File(filePath);
        fileImport(file, indexName, split, columnFieldMap);
    }
    public static void fileImport(File file, String indexName, String splitString, Map<Integer, String> columnFieldMap) {
        List<String> files = new ArrayList<>();
        travelFile(file, files);
        for (String f : files) {
            String fileType = getMimeType(new File(f));
            ESFileUpHandler handler = ESFileUpAdapter.getHandler(fileType);
            if (null != handler) {
                handler.handle(new File(f), indexName, columnFieldMap);
            }
        }
        return;
    }

    public static void travelFile(String filePath, List<String> fileList) {
        File file = new File(filePath);
        travelFile(file, fileList);
    }

    public static final void travelFile(File file, List<String> fileList) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    travelFile(f, fileList);
                }
            } else {
                // System.out.println(file.getAbsoluteFile());
                fileList.add(file.getAbsolutePath());
            }
        }
    }

    /**
     * 获取文件类型
     * @param file
     * @return
     */
    private static String getMimeType(File file) {
        if (file.isDirectory()) {
            return null;
        }

        AutoDetectParser parser = new AutoDetectParser();
        parser.setParsers(new HashMap<MediaType, Parser>());

        Metadata metadata = new Metadata();
        metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, file.getName());

        try {
            InputStream stream = new FileInputStream(file);
            parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
            stream.close();
        } catch (TikaException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return metadata.get(HttpHeaders.CONTENT_TYPE);
    }

    public static void main(String[] args) {
        /*List<String> files = new ArrayList<>();
        travelFile("F:\\", files);
        files.stream().forEach(e -> System.out.println(e));*/
       /* String[] sarr = {"111", "222"};
        System.out.println("--------------------------------------------");
        System.out.println(JSON.toJSONString(sarr));*/
        /*String str = ",,,,,,,,,,,,";
        String[] arr = str.split(",");
        System.out.println(arr.length);
        List<String> files = new ArrayList<>();
        travelFile("C:\\temp", files);
        for (String file : files) {
            String fileType = getMimeType(new File(file));
            System.out.println(file + ":" + fileType);
            //利用Tika的AutoDetectReader类检测文件的编码格式
            AutoDetectReader dr = null;
            try {
                dr = new AutoDetectReader(new FileInputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TikaException e) {
                e.printStackTrace();
            }
            System.out.println("charset:"+dr.getCharset().name());
        }*/


    }


}
