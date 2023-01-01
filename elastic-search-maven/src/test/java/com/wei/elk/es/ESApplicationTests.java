package com.wei.elk.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.elasticsearch.indices.PutMappingResponse;
import com.wei.elk.es.common.entity.ESIndexEntity;
import com.wei.elk.es.data.huji.entity.HujiEntity;
import com.wei.elk.es.data.huji.entity.JDEntity;
import com.wei.elk.es.data.huji.entity.TestEntity;
import com.wei.elk.es.data.huji.entity.ZheJiangXueJiEntity;
import com.wei.elk.es.temp.IdentityContextHandler;
import com.wei.elk.es.temp.IdentityStrategy;
import com.wei.elk.es.temp.IdentityType;
import com.wei.elk.es.temp.UserInfo;
import com.wei.elk.es.util.ESIndexUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/11/25 20:25:08
 */
@SpringBootTest
public class ESApplicationTests {

    private static final String INDEX_NAME = "huji";
    Logger logger = LoggerFactory.getLogger(ESApplicationTests.class);
    @Autowired
    ElasticsearchClient esClient;
    @Autowired
    IdentityContextHandler identityContextHandler;
    @Autowired
    ESIndexUtil esIndexUtil;

    @Test
    public void strategy() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserType(IdentityType.STUDENT.getCode());
        IdentityStrategy identityStrategy = identityContextHandler.getStrategy(userInfo.getUserType());
        identityStrategy.identityInfo(userInfo);
    }

    /**
     * @description 创建索引
     * @param
     * @return void
     * @author
     * @date 2022/11/26 12:30
     */
    @Test
    public void createIndexTest() throws IOException {
        ESIndexEntity esIndexEntity = new ESIndexEntity();
        esIndexEntity.setName("zhejiang_xueji");
        esIndexEntity.setMappingClass(ZheJiangXueJiEntity.class);
        esIndexUtil.createIndex(esIndexEntity);
    }

    /**
     * @description 修改索引
     * 字段可以新增，已有的字段只能修改字段的 search_analyzer 属性
     *  * @param
     * @return void
     * @author
     * @date 2022/11/26 12:35
     */
    @Test
    public void modifyIndex2() throws IOException {
        ESIndexEntity esIndexEntity = new ESIndexEntity();
        esIndexEntity.setName("test_index");
        esIndexEntity.setMappingClass(TestEntity.class);
        esIndexUtil.modifyIndex(esIndexEntity);
    }

    @Test
    public void insert() throws IOException {
        HujiEntity huji = new HujiEntity();
        huji.setName("张三");
        esClient.index(i -> i.index("huji").document("_doc"));
        IndexRequest<Object> request = IndexRequest.of(i -> i.index("huji").document(huji));
        IndexResponse response = esClient.index(request);
    }

    /*--------------------------------------------------索引start------------------------------------------*/


    /**
     * @description 修改索引
     * 字段可以新增，已有的字段只能修改字段的 search_analyzer 属性
     *  * @param
     * @return void
     * @author
     * @date 2022/11/26 12:35
     */
    @Test
    public void modifyIndex() throws IOException {
        PutMappingResponse response = esClient.indices().putMapping(typeMappingBuilder -> typeMappingBuilder
                .index(INDEX_NAME)
                .properties("age", propertyBuilder -> propertyBuilder.integer(integerNumberPropertyBuilder -> integerNumberPropertyBuilder))
                .properties("name", propertyBuilder -> propertyBuilder.keyword(keywordPropertyBuilder -> keywordPropertyBuilder))
                .properties("poems", propertyBuilder -> propertyBuilder.text(textPropertyBuilder -> textPropertyBuilder.analyzer("ik_max_word").searchAnalyzer("ik_smart")))
        );
        logger.info("acknowledged={}", response.acknowledged());
    }

    /**
     * @description 删除索引
     *  * @param
     * @return void
     * @author
     * @date 2022/11/26 12:37
     */
    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexResponse response = esClient.indices().delete(builder -> builder.index("zhejiang_xueji"));
        logger.info("acknowledged={}", response.acknowledged());
    }

    /**
     * @description 查询索引列表
     *  * @param
     * @return void
     * @author
     * @date 2022/11/26 12:38
     */
    @Test
    public void getIndex() throws IOException {
        //使用 * 也可以
        GetIndexResponse response = esClient.indices().get(builder -> builder.index("_all"));
        logger.info(response.result().toString());
    }

    /**
     * @description 查询索引详情
     *  * @param
     * @return void
     * @author
     * @date 2022/11/26 12:43
     */
    @Test
    public void getIndexDetail() throws IOException {
        GetIndexResponse response = esClient.indices().get(builder -> builder.index("jduser"));
        logger.info(response.result().toString());
    }

    /*--------------------------------------------------索引end------------------------------------------*/
}
