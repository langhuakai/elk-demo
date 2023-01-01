package com.wei.elk.es.handler.esfileuploadhandler;

import com.wei.elk.es.announce.ESFileUpHandlerAnnounce;
import com.wei.elk.es.util.file.CsvUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/12/2 02:42:00
 */
@Component
@ESFileUpHandlerAnnounce("text/csv")
public class ESFileUpCsvHandler implements ESFileUpHandler {
    @Override
    public void handle(File f, String indexName, Map<Integer, String> columnFieldMap) {
        CsvUtil.getCsvData5(f, indexName, columnFieldMap);
    }
}
