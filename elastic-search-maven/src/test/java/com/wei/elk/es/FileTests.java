package com.wei.elk.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.elasticsearch.indices.PutMappingResponse;
import com.wei.elk.es.common.entity.ESIndexEntity;
import com.wei.elk.es.data.huji.entity.HujiEntity;
import com.wei.elk.es.data.huji.entity.TestEntity;
import com.wei.elk.es.temp.IdentityContextHandler;
import com.wei.elk.es.temp.IdentityStrategy;
import com.wei.elk.es.temp.IdentityType;
import com.wei.elk.es.temp.UserInfo;
import com.wei.elk.es.util.ESIndexUtil;
import com.wei.elk.es.util.file.FileUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/11/25 20:25:08
 */
@SpringBootTest
public class FileTests {

    @Test
    public void importFile() {
        Map<Integer, String> columnFieldMap = new HashMap<>();
        columnFieldMap.put(0, "name");
        columnFieldMap.put(1, "idcardNo");
        columnFieldMap.put(2, "nativePlace");
        columnFieldMap.put(3, "parentName");
        columnFieldMap.put(4, "cellPhoneNo");
        columnFieldMap.put(5, "addressDetail");
        columnFieldMap.put(6, "nativePlace2");
        columnFieldMap.put(7, "className");
        columnFieldMap.put(8, "schoolName");
        FileUtil.fileImport("F:\\社工库\\整理\\0002浙江学籍", "zhejiang_xueji", columnFieldMap);
    }
}
