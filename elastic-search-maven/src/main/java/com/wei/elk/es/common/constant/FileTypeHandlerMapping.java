package com.wei.elk.es.common.constant;

import com.wei.elk.es.data.huji.entity.HujiEntity;
import com.wei.elk.es.data.huji.entity.TestEntity;
import com.wei.elk.es.handler.esfileuploadhandler.ESFileUpCsvHandler;
import com.wei.elk.es.handler.esfileuploadhandler.ESFileUpExcelHandler;
import com.wei.elk.es.handler.esfileuploadhandler.ESFileUpTxtHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description 文件类型对应解析器map（xls，xlsx，csv等）
 * @date 2022/12/2 02:26:12
 */
public class FileTypeHandlerMapping {

    public static final Map<String, Class> fileTypeHandlerMap;
    static
    {
        fileTypeHandlerMap = new HashMap<>();
        fileTypeHandlerMap.put("application/vnd.ms-excel", ESFileUpExcelHandler.class);// excel97~2003
        fileTypeHandlerMap.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ESFileUpExcelHandler.class);// excel2007以后
        fileTypeHandlerMap.put("text/csv", ESFileUpCsvHandler.class);// csv文件
        fileTypeHandlerMap.put("text/plain", ESFileUpTxtHandler.class); // txt文件

    }
}
