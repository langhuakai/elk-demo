package com.wei.elk.es.handler.esfileuploadhandler;

import com.alibaba.excel.EasyExcel;
import com.wei.elk.es.announce.ESFileUpHandlerAnnounce;
import com.wei.elk.es.common.constant.ESIndexEntityMapping;
import com.wei.elk.es.listener.ESEntityListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/12/2 02:33:01
 */
@Component
@ESFileUpHandlerAnnounce("application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
public class ESFileUpExcelHandler implements ESFileUpHandler{

    @Override
    public void handle(File f, String indexName, Map<Integer, String> columnFieldMap) {
        EasyExcel.read(f, ESIndexEntityMapping.indexEntityMap.get(indexName),
                new ESEntityListener<>(indexName)).sheet().doRead();
    }
}
