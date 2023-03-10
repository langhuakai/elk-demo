package com.wei.elk.es;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/11/26 12:52:14
 */
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.*;
import co.elastic.clients.elasticsearch._types.aggregations.AverageAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.CardinalityAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.SumAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.cat.IndicesResponse;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.elasticsearch.sql.QueryResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wei.elk.es.common.entity.ESIndexEntity;
import com.wei.elk.es.common.vo.*;
import com.wei.elk.es.data.huji.entity.TestEntity;
import com.wei.elk.es.util.ESOperateUtil;
import com.wei.elk.es.util.ESSearchUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ESApplicationTests2 {
    private static final Logger logger = LoggerFactory.getLogger(ESApplicationTests2.class.getName());

    private static final String INDEX_NAME = "huji";

    // private ElasticsearchTransport transport;
    @Autowired
    private ElasticsearchClient client;
    @Autowired
    private ESOperateUtil esOperateUtil;
    @Autowired
    private ESSearchUtil esSearchUtil;

    /*@BeforeEach
    public void before() {
        RestClient restClient = RestClient.builder(
                new HttpHost("10.49.196.10", 9200),
                new HttpHost("10.49.196.11", 9200),
                new HttpHost("10.49.196.12", 9200)).build();
        ObjectMapper objectMapper = new ObjectMapper();
        transport = new RestClientTransport(restClient, new JacksonJsonpMapper(objectMapper));
        client = new ElasticsearchClient(transport);
    }

    @AfterEach
    public void after() throws IOException {
        transport.close();
    }*/

    /**
     * ????????????
     */
    @Test
    public void createIndex() throws IOException {
        CreateIndexResponse response = client.indices().create(builder -> builder
                .settings(indexSettingsBuilder -> indexSettingsBuilder.numberOfReplicas("1").numberOfShards("2"))
                .mappings(typeMappingBuilder -> typeMappingBuilder
                        .properties("age", propertyBuilder -> propertyBuilder.integer(integerNumberPropertyBuilder -> integerNumberPropertyBuilder))
                        .properties("name", propertyBuilder -> propertyBuilder.keyword(keywordPropertyBuilder -> keywordPropertyBuilder))
                        .properties("poems", propertyBuilder -> propertyBuilder.text(textPropertyBuilder -> textPropertyBuilder.analyzer("ik_max_word").searchAnalyzer("ik_max_word")))
                        .properties("about", propertyBuilder -> propertyBuilder.text(textPropertyBuilder -> textPropertyBuilder.analyzer("ik_max_word").searchAnalyzer("ik_max_word")))
                        .properties("success", propertyBuilder -> propertyBuilder.text(textPropertyBuilder -> textPropertyBuilder.analyzer("ik_max_word").searchAnalyzer("ik_max_word")))
                )
                .index(INDEX_NAME));
        logger.info("acknowledged={}", response.acknowledged());
    }

    /**
     * ???????????????_mapping??????
     * ?????????????????????????????????????????????????????????search_analyzer??????
     */
    @Test
    public void modifyIndex() throws IOException {
        PutMappingResponse response = client.indices().putMapping(typeMappingBuilder -> typeMappingBuilder
                .index(INDEX_NAME)
                .properties("age", propertyBuilder -> propertyBuilder.integer(integerNumberPropertyBuilder -> integerNumberPropertyBuilder))
                .properties("name", propertyBuilder -> propertyBuilder.keyword(keywordPropertyBuilder -> keywordPropertyBuilder))
                .properties("poems", propertyBuilder -> propertyBuilder.text(textPropertyBuilder -> textPropertyBuilder.analyzer("ik_max_word").searchAnalyzer("ik_smart")))
        );
        logger.info("acknowledged={}", response.acknowledged());
    }

    /**
     * ????????????
     */
    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexResponse response = client.indices().delete(builder -> builder.index(INDEX_NAME));
        logger.info("acknowledged={}", response.acknowledged());
    }

    /**
     * ??????????????????
     */
    @Test
    public void getIndex() throws IOException {
        //?????? * ?????????
        GetIndexResponse response = client.indices().get(builder -> builder.index("_all"));
        logger.info(response.result().toString());
    }

    /**
     * ??????????????????
     */
    @Test
    public void getIndexDetail() throws IOException {
        GetIndexResponse response = client.indices().get(builder -> builder.index(INDEX_NAME));
        logger.info(response.result().toString());
    }

    /**
     * ????????????
     */
    @Test
    public void createDoc() throws IOException {

        TestEntity testEntity = new TestEntity();
        testEntity.setIdcardNo("420624198803136818");
        testEntity.setName("????????????3");
        ESSimpleDataVO<TestEntity> vo = new ESSimpleDataVO();
        vo.setIndexName("test_index");
        vo.setId(testEntity.getIdcardNo());
        vo.setData(testEntity);
        esOperateUtil.createDoc(vo);
    }

    /**
     * ????????????
     */
    @Test
    public void deleteDoc() throws IOException {
        ESSimpleDataVO vo = new ESSimpleDataVO();
        vo.setIndexName("test_index");
        vo.setId("420624198803136818");
        esOperateUtil.deleteDoc(vo);
    }

    /**
     * ????????????
     */
    @Test
    public void trunkDoc() throws IOException {
        esOperateUtil.trunkDoc("zhejiang_xueji");
    }

    /**
     * ???????????????????????????????????????
     */
    @Test
    public void updateDocEntity() throws IOException {
        /*TestEntity testEntity = new TestEntity();
        //testEntity.setIDCardNo("420624198803136818");
        testEntity.setName("????????????3");
        ESSimpleDataVO<TestEntity> vo = new ESSimpleDataVO();
        vo.setIndexName("test_index");
        vo.setId("2");
        vo.setData(testEntity);
        esOperateUtil.updateDocEntity(vo);*/

        Map<String, Object> doc = new HashMap<>();
        doc.put("name", "??????4");
        ESSimpleDataVO<Map<String, Object>> vo = new ESSimpleDataVO();
        vo.setIndexName("test_index");
        vo.setId("2");
        vo.setData(doc);
        esOperateUtil.updateDocEntity(vo);
    }

    /**
     * ???????????????????????????????????????
     */
    @Test
    public void updateDocMap() throws IOException {
        /*Map<String, Object> doc = new HashMap<>();
        doc.put("name", "??????2");
        ESSimpleDataVO<Map<String, Object>> vo = new ESSimpleDataVO();
        vo.setIndexName("test_index");
        vo.setId("2");
        vo.setData(doc);
        esOperateUtil.updateDocMap(vo);*/

        TestEntity testEntity = new TestEntity();
        //testEntity.setIDCardNo("420624198803136818");
        // testEntity.setName("??????5");
        testEntity.setAddress("??????");
        ESSimpleDataVO<TestEntity> vo = new ESSimpleDataVO();
        vo.setIndexName("test_index");
        vo.setId("123456785");
        vo.setData(testEntity);
        esOperateUtil.updateDocMap(vo);
    }

    /**
     * ????????????????????????????????????????????????????????????(???????????????????????????)
     */
    @Test
    public void createOrUpdateDoc() throws IOException {
        ESSimpleDataVO vo = new ESSimpleDataVO();
        TestEntity testEntity = new TestEntity();
        //testEntity.setName("??????6");
        testEntity.setIdcardNo("12345678");
        testEntity.setAddress("");
        vo.setIndexName("test_index");
        vo.setId("3");
        vo.setData(testEntity);
        esOperateUtil.createOrUpdateDoc(vo);

        /*Poet poet = new Poet();
        poet.setAge(40);
        poet.setName("??????2");
        response = client.index(builder -> builder.index(INDEX_NAME).id("2").document(poet));
        logger.info(response.toString());*/
    }


    /**
     * ????????????
     */
    @Test
    public void batchInsert() throws IOException {
        ESBatchDataVO vo = new ESBatchDataVO();
        vo.setIndexName("test_index");
        List<TestEntity> entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestEntity testEntity = new TestEntity();
            testEntity.setName("?????????" + i);
            testEntity.setIdcardNo("12345678" + i);
            testEntity.setAddress("???????????????????????????????????????" + (i + 1) + "???");
            testEntity.setId(testEntity.getIdcardNo() + i);
            entities.add(testEntity);
        }
        vo.setData(entities);
        esOperateUtil.batchInsert(vo);
    }

    @Test
    public void batchDelete() throws IOException {
        ESBatchDeleteVO vo = new ESBatchDeleteVO();
        vo.setIndexName("test_index");
        List<String> ids = new ArrayList<>();
        ids.add("123456780");
        ids.add("123456781");
        ids.add("123456782");
        ids.add("123456783");
        ids.add("123456784");
        vo.setIds(ids);
        esOperateUtil.batchDelete(vo);
    }

    /**
     * ??????????????????
     */
    @Test
    public void getDocAll() throws IOException {
        esSearchUtil.getDocAll("test_index");
    }

    /**
     * ??????????????????
     */
    @Test
    public void getDoc() throws IOException {

        ESSimpleDataVO vo = new ESSimpleDataVO();
        vo.setIndexName("zhejiang_xueji");
        vo.setId("532128200403304142");
        esSearchUtil.getDoc(vo);
    }

    /**
     * term/terms??????,????????????????????????????????????????????????,??????????????????????????????????????????????????????????????????
     */
    @Test
    public void searchTerm() throws IOException {
        ESTermQueryVO queryVO = new ESTermQueryVO();
        queryVO.setIndexName("zhejiang_xueji");
        queryVO.setParamName("name");
        queryVO.setParamValue("?????????");
        //esSearchUtil.searchTermsEntity(queryVO);
        esSearchUtil.searchTermsMap(queryVO);
    }

    /**
     * range??????,????????????
     */
    @Test
    public void searchRange() throws IOException {
        ESRangeQueryVO<String> queryVO = new ESRangeQueryVO<>();
        queryVO.setIndexName("test_index");
        queryVO.setParamName("idcardNo");
        queryVO.setRangeFrom("12345678");
        queryVO.setRangeTo("23456789");
        esSearchUtil.searchRange(queryVO);
    }

    /**
     * match??????????????????????????????????????????
     */
    @Test
    public void searchMatch() throws IOException {
        SearchResponse<Map> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index("huji")
                        .query(queryBuilder -> queryBuilder
                                .match(matchQueryBuilder -> matchQueryBuilder
                                        .field("name").query("?????????"))).from(0).size(40)
                , Map.class);
        logger.info(response.toString());
    }

    /**
     * multi_match??????,-
     */
    @Test
    public void searchMultiMatch() throws IOException {

        ESTermQueryVO queryVO = new ESTermQueryVO();
        queryVO.setIndexName("test_index");
        queryVO.setParamName("address,name");
        queryVO.setParamValue("??????????????????");
        queryVO.setPageSize(20);
        esSearchUtil.searchMultiMatch(queryVO);
    }

    /**
     * match_phrase ??????,?????????????????????????????????????????????
     */
    @Test
    public void searchMatchPhrase() throws IOException {
        ESTermQueryVO queryVO = new ESTermQueryVO();
        queryVO.setIndexName("test_index");
        queryVO.setParamName("address");
        queryVO.setParamValue("?????????");
        esSearchUtil.searchMatchPhrase(queryVO);
    }

    /**
     * match_all ??????,????????????
     */
    @Test
    public void searchMatchAll() throws IOException {
        ESTermQueryVO queryVO = new ESTermQueryVO();
        queryVO.setIndexName("test_index");
        esSearchUtil.searchMatchAll(queryVO);
    }

    /**
     * query_string ??????
     */
    @Test
    public void searchQueryString() throws IOException {
        //?????? match
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .defaultField("success").query("????????????")))
                , Poet.class);
        logger.info(response.toString());

        //?????? mulit_match
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .fields("about", "success").query("????????????")))
                , Poet.class);
        logger.info(response.toString());

        //?????? match_phrase
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .defaultField("success").query("????????????")))
                , Poet.class);
        logger.info(response.toString());

        //??????????????????????????????????????????????????????
        //?????????????????? ???????????? ??? ???????????? ?????????
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .fields("success").query("?????? AND ??????")))
                , Poet.class);
        logger.info(response.toString());

        //?????????????????????
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .fields("success").query("?????? ??????").defaultOperator(Operator.And)))
                , Poet.class);
        logger.info(response.toString());

        //?????? name ??? success ????????????"??????"???"??????"??????????????????????????????"??????"????????????????????????
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .fields("name","success").query("(?????? AND ??????) OR ??????")))
                , Poet.class);
        logger.info(response.toString());
    }

    /**
     * simple_query_string ??????,???query_string??????
     * ?????????AND OR NOT???????????????????????????
     * ?????? +??????AND,|??????OR,-??????NOT
     */
    @Test
    public void searchSimpleQueryString() throws IOException {
        //?????????????????? ???????????? ??? ???????????? ?????????
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .simpleQueryString(simpleQueryStringQueryBuilder -> simpleQueryStringQueryBuilder
                                        .fields("success").query("?????? + ??????")))
                , Poet.class);
        logger.info(response.toString());
    }

    /**
     * ????????????
     */
    @Test
    public void searchFuzzy() throws IOException {
        //?????????????????????????????????????????????????????????????????????
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .match(matchQueryBuilder -> matchQueryBuilder
                                        .field("success").query("??????").fuzziness("1")))
                , Poet.class);
        logger.info(response.toString());

        //?????? fuzzy query???????????????????????????????????????????????????
        SearchResponse<Poet> response2 = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .fuzzy(fuzzyQueryBuilder ->  fuzzyQueryBuilder
                                        .field("success").fuzziness("1").value("??????")))
                , Poet.class);
        logger.info(response2.toString());
    }

    /**
     * bool??????,????????????
     */
    @Test
    public void searchBool() throws IOException {
        //?????? success ?????? ???????????? ??? age ??? [20-40] ???????????????
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .bool(boolQueryBuilder -> boolQueryBuilder
                                        .must(queryBuilder2 -> queryBuilder2
                                                .match(matchQueryBuilder -> matchQueryBuilder
                                                        .field("success").query("??????"))
                                        )
                                        .must(queryBuilder2 -> queryBuilder2
                                                .range(rangeQueryBuilder -> rangeQueryBuilder
                                                        .field("age").gte(JsonData.of("20")).lt(JsonData.of("40")))
                                        )
                                )
                        )
                , Poet.class);
        logger.info(response.toString());

        //????????? success ?????? ???????????? ??? age ??? [20-40] ?????????????????????????????????
        SearchResponse<Poet> response2 = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .bool(boolQueryBuilder -> boolQueryBuilder
                                        .filter(queryBuilder2 -> queryBuilder2
                                                .match(matchQueryBuilder -> matchQueryBuilder
                                                        .field("success").query("??????"))
                                        )
                                        .filter(queryBuilder2 -> queryBuilder2
                                                .range(rangeQueryBuilder -> rangeQueryBuilder
                                                        .field("age").gte(JsonData.of("20")).lt(JsonData.of("40")))
                                        )
                                )
                        )
                , Poet.class);
        logger.info(response2.toString());
    }

    /**
     * aggs??????,????????????
     */
    @Test
    public void searchAggs() throws IOException {
        //??????
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .aggregations("age_sum", aggregationBuilder -> aggregationBuilder
                                .sum(sumAggregationBuilder -> sumAggregationBuilder
                                        .field("age")))
                , Poet.class);
        logger.info(response.toString());

        //?????? select count distinct(age) from poet-index
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .aggregations("age_count", aggregationBuilder -> aggregationBuilder
                                .cardinality(cardinalityAggregationBuilder -> cardinalityAggregationBuilder.field("age")))
                , Poet.class);
        logger.info(response.toString());

        //??????????????????????????????????????????
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .aggregations("age_stats", aggregationBuilder -> aggregationBuilder
                                .stats(statsAggregationBuilder -> statsAggregationBuilder
                                        .field("age")))
                , Poet.class);
        logger.info(response.toString());

        //select name,count(*) from poet-index group by name
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .aggregations("name_terms", aggregationBuilder -> aggregationBuilder
                                .terms(termsAggregationBuilder -> termsAggregationBuilder
                                        .field("name")))
                , Poet.class);
        logger.info(response.toString());

        //select name,age,count(*) from poet-index group by name,age
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .aggregations("name_terms", aggregationBuilder -> aggregationBuilder
                                .terms(termsAggregationBuilder -> termsAggregationBuilder
                                        .field("name")
                                )
                                .aggregations("age_terms", aggregationBuilder2 -> aggregationBuilder2
                                        .terms(termsAggregationBuilder -> termsAggregationBuilder
                                                .field("age")
                                        ))
                        )
                , Poet.class);
        logger.info(response.toString());

        //?????? select avg(age) from poet-index where name='??????'
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .bool(boolQueryBuilder -> boolQueryBuilder
                                        .filter(queryBuilder2 -> queryBuilder2
                                                .term(termQueryBuilder -> termQueryBuilder
                                                        .field("name").value("??????")))))
                        .aggregations("ave_age", aggregationBuilder -> aggregationBuilder
                                .avg(averageAggregationBuilder -> averageAggregationBuilder.field("age")))
                , Poet.class);
        logger.info(response.toString());
    }

    /**
     * suggest??????,????????????, ??????
     */
    @Test
    public void searchSuggest() throws IOException {

        ESSuggestQueryVO queryVO = new ESSuggestQueryVO();
        queryVO.setIndexName("test_index");
        queryVO.setSuggestKey("address_suggest");
        queryVO.setParamName("address");
        queryVO.setParamValue("????????????");
        queryVO.setSuggestMode(SuggestMode.Always);
        queryVO.setMinWordLength(2);
        esSearchUtil.searchSuggest(queryVO);
    }

    /**
     * ????????????
     */
    @Test
    public void searchHighlight() throws IOException {
        ESTermQueryVO queryVO = new ESTermQueryVO();
        queryVO.setIndexName("test_index");
        queryVO.setParamName("address");
        queryVO.setParamValue("????????????");
        esSearchUtil.searchHighlight(queryVO);
    }

    /**
     * sql???????????????
     */
    @Test
    public void searchSql() throws IOException {
        ESSqlQueryVO queryVO = new ESSqlQueryVO();
        queryVO.setFormatter("json");
        queryVO.setSql("SELECT * FROM " + "test_index" +" limit 1");
        esSearchUtil.searchSql(queryVO);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Poet {
        private Integer age;
        private String name;
        private String poems;
        private String about;
        /**??????*/
        private String success;
    }
}