package com.wei.elk.es.handler.esfileuploadhandler;

import java.io.File;
import java.util.Map;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/12/2 02:34:47
 */
public interface ESFileUpHandler {

    void handle(File f, String indexName, Map<Integer, String> columnFieldMap);
}
