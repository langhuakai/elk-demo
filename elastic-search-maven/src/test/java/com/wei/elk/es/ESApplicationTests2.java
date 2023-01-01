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
     * 创建索引
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
     * 修改索引的_mapping信息
     * 字段可以新增，已有的字段只能修改字段的search_analyzer属性
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
     * 删除索引
     */
    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexResponse response = client.indices().delete(builder -> builder.index(INDEX_NAME));
        logger.info("acknowledged={}", response.acknowledged());
    }

    /**
     * 查询索引列表
     */
    @Test
    public void getIndex() throws IOException {
        //使用 * 也可以
        GetIndexResponse response = client.indices().get(builder -> builder.index("_all"));
        logger.info(response.result().toString());
    }

    /**
     * 查询索引详情
     */
    @Test
    public void getIndexDetail() throws IOException {
        GetIndexResponse response = client.indices().get(builder -> builder.index(INDEX_NAME));
        logger.info(response.result().toString());
    }

    /**
     * 创建文档
     */
    @Test
    public void createDoc() throws IOException {

        TestEntity testEntity = new TestEntity();
        testEntity.setIdcardNo("420624198803136818");
        testEntity.setName("测试用户3");
        ESSimpleDataVO<TestEntity> vo = new ESSimpleDataVO();
        vo.setIndexName("test_index");
        vo.setId(testEntity.getIdcardNo());
        vo.setData(testEntity);
        esOperateUtil.createDoc(vo);
    }

    /**
     * 删除文档
     */
    @Test
    public void deleteDoc() throws IOException {
        ESSimpleDataVO vo = new ESSimpleDataVO();
        vo.setIndexName("test_index");
        vo.setId("420624198803136818");
        esOperateUtil.deleteDoc(vo);
    }

    /**
     * 清空文档
     */
    @Test
    public void trunkDoc() throws IOException {
        esOperateUtil.trunkDoc("zhejiang_xueji");
    }

    /**
     * 修改文档，只修改设置的字段
     */
    @Test
    public void updateDocEntity() throws IOException {
        /*TestEntity testEntity = new TestEntity();
        //testEntity.setIDCardNo("420624198803136818");
        testEntity.setName("测试用户3");
        ESSimpleDataVO<TestEntity> vo = new ESSimpleDataVO();
        vo.setIndexName("test_index");
        vo.setId("2");
        vo.setData(testEntity);
        esOperateUtil.updateDocEntity(vo);*/

        Map<String, Object> doc = new HashMap<>();
        doc.put("name", "李白4");
        ESSimpleDataVO<Map<String, Object>> vo = new ESSimpleDataVO();
        vo.setIndexName("test_index");
        vo.setId("2");
        vo.setData(doc);
        esOperateUtil.updateDocEntity(vo);
    }

    /**
     * 修改文档，只修改设置的字段
     */
    @Test
    public void updateDocMap() throws IOException {
        /*Map<String, Object> doc = new HashMap<>();
        doc.put("name", "李白2");
        ESSimpleDataVO<Map<String, Object>> vo = new ESSimpleDataVO();
        vo.setIndexName("test_index");
        vo.setId("2");
        vo.setData(doc);
        esOperateUtil.updateDocMap(vo);*/

        TestEntity testEntity = new TestEntity();
        //testEntity.setIDCardNo("420624198803136818");
        // testEntity.setName("李白5");
        testEntity.setAddress("中国");
        ESSimpleDataVO<TestEntity> vo = new ESSimpleDataVO();
        vo.setIndexName("test_index");
        vo.setId("123456785");
        vo.setData(testEntity);
        esOperateUtil.updateDocMap(vo);
    }

    /**
     * 新增或修改文档，修改时所有的字段都会覆盖(相当于先删除在新增)
     */
    @Test
    public void createOrUpdateDoc() throws IOException {
        ESSimpleDataVO vo = new ESSimpleDataVO();
        TestEntity testEntity = new TestEntity();
        //testEntity.setName("李白6");
        testEntity.setIdcardNo("12345678");
        testEntity.setAddress("");
        vo.setIndexName("test_index");
        vo.setId("3");
        vo.setData(testEntity);
        esOperateUtil.createOrUpdateDoc(vo);

        /*Poet poet = new Poet();
        poet.setAge(40);
        poet.setName("杜甫2");
        response = client.index(builder -> builder.index(INDEX_NAME).id("2").document(poet));
        logger.info(response.toString());*/
    }


    /**
     * 批量新增
     */
    @Test
    public void batchInsert() throws IOException {
        ESBatchDataVO vo = new ESBatchDataVO();
        vo.setIndexName("test_index");
        List<TestEntity> entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestEntity testEntity = new TestEntity();
            testEntity.setName("白居易" + i);
            testEntity.setIdcardNo("12345678" + i);
            testEntity.setAddress("广东省深圳市龙岗区坂田街道" + (i + 1) + "号");
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
     * 查询索有文档
     */
    @Test
    public void getDocAll() throws IOException {
        esSearchUtil.getDocAll("test_index");
    }

    /**
     * 查询单个文档
     */
    @Test
    public void getDoc() throws IOException {

        ESSimpleDataVO vo = new ESSimpleDataVO();
        vo.setIndexName("zhejiang_xueji");
        vo.setId("532128200403304142");
        esSearchUtil.getDoc(vo);
    }

    /**
     * term/terms查询,对输入内容不做分词处理（精确匹配,此查询只能匹配一个字段，但是可以匹配多个值）
     */
    @Test
    public void searchTerm() throws IOException {
        ESTermQueryVO queryVO = new ESTermQueryVO();
        queryVO.setIndexName("zhejiang_xueji");
        queryVO.setParamName("name");
        queryVO.setParamValue("赵丽颖");
        //esSearchUtil.searchTermsEntity(queryVO);
        esSearchUtil.searchTermsMap(queryVO);
    }

    /**
     * range查询,范围查询
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
     * match查询，对输入内容先分词再查询
     */
    @Test
    public void searchMatch() throws IOException {
        SearchResponse<Map> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index("huji")
                        .query(queryBuilder -> queryBuilder
                                .match(matchQueryBuilder -> matchQueryBuilder
                                        .field("name").query("刘亦菲"))).from(0).size(40)
                , Map.class);
        logger.info(response.toString());
    }

    /**
     * multi_match查询,-
     */
    @Test
    public void searchMultiMatch() throws IOException {

        ESTermQueryVO queryVO = new ESTermQueryVO();
        queryVO.setIndexName("test_index");
        queryVO.setParamName("address,name");
        queryVO.setParamValue("深圳市龙岗区");
        queryVO.setPageSize(20);
        esSearchUtil.searchMultiMatch(queryVO);
    }

    /**
     * match_phrase 查询,匹配（模糊匹配）整个查询字符串
     */
    @Test
    public void searchMatchPhrase() throws IOException {
        ESTermQueryVO queryVO = new ESTermQueryVO();
        queryVO.setIndexName("test_index");
        queryVO.setParamName("address");
        queryVO.setParamValue("广东省");
        esSearchUtil.searchMatchPhrase(queryVO);
    }

    /**
     * match_all 查询,查询所有
     */
    @Test
    public void searchMatchAll() throws IOException {
        ESTermQueryVO queryVO = new ESTermQueryVO();
        queryVO.setIndexName("test_index");
        esSearchUtil.searchMatchAll(queryVO);
    }

    /**
     * query_string 查询
     */
    @Test
    public void searchQueryString() throws IOException {
        //类似 match
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .defaultField("success").query("古典文学")))
                , Poet.class);
        logger.info(response.toString());

        //类似 mulit_match
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .fields("about", "success").query("古典文学")))
                , Poet.class);
        logger.info(response.toString());

        //类似 match_phrase
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .defaultField("success").query("文学作家")))
                , Poet.class);
        logger.info(response.toString());

        //带运算符查询，运算符两边的词不再分词
        //查询同时包含 ”文学“ 和 ”伟大“ 的文档
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .fields("success").query("文学 AND 伟大")))
                , Poet.class);
        logger.info(response.toString());

        //等同上一个查询
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .fields("success").query("文学 伟大").defaultOperator(Operator.And)))
                , Poet.class);
        logger.info(response.toString());

        //查询 name 或 success 字段包含"文学"和"伟大"这两个单词，或者包含"李白"这个单词的文档。
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .fields("name","success").query("(文学 AND 伟大) OR 高度")))
                , Poet.class);
        logger.info(response.toString());
    }

    /**
     * simple_query_string 查询,和query_string类似
     * 不支持AND OR NOT，会当做字符串处理
     * 使用 +替代AND,|替代OR,-替代NOT
     */
    @Test
    public void searchSimpleQueryString() throws IOException {
        //查询同时包含 ”文学“ 和 ”伟大“ 的文档
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .simpleQueryString(simpleQueryStringQueryBuilder -> simpleQueryStringQueryBuilder
                                        .fields("success").query("文学 + 伟大")))
                , Poet.class);
        logger.info(response.toString());
    }

    /**
     * 模糊查询
     */
    @Test
    public void searchFuzzy() throws IOException {
        //全文查询时使用模糊参数，先分词再计算模糊选项。
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .match(matchQueryBuilder -> matchQueryBuilder
                                        .field("success").query("思考").fuzziness("1")))
                , Poet.class);
        logger.info(response.toString());

        //使用 fuzzy query，对输入不分词，直接计算模糊选项。
        SearchResponse<Poet> response2 = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .fuzzy(fuzzyQueryBuilder ->  fuzzyQueryBuilder
                                        .field("success").fuzziness("1").value("理想")))
                , Poet.class);
        logger.info(response2.toString());
    }

    /**
     * bool查询,组合查询
     */
    @Test
    public void searchBool() throws IOException {
        //查询 success 包含 “思想” 且 age 在 [20-40] 之间的文档
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .bool(boolQueryBuilder -> boolQueryBuilder
                                        .must(queryBuilder2 -> queryBuilder2
                                                .match(matchQueryBuilder -> matchQueryBuilder
                                                        .field("success").query("思想"))
                                        )
                                        .must(queryBuilder2 -> queryBuilder2
                                                .range(rangeQueryBuilder -> rangeQueryBuilder
                                                        .field("age").gte(JsonData.of("20")).lt(JsonData.of("40")))
                                        )
                                )
                        )
                , Poet.class);
        logger.info(response.toString());

        //过滤出 success 包含 “思想” 且 age 在 [20-40] 之间的文档，不计算得分
        SearchResponse<Poet> response2 = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .bool(boolQueryBuilder -> boolQueryBuilder
                                        .filter(queryBuilder2 -> queryBuilder2
                                                .match(matchQueryBuilder -> matchQueryBuilder
                                                        .field("success").query("思想"))
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
     * aggs查询,聚合查询
     */
    @Test
    public void searchAggs() throws IOException {
        //求和
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .aggregations("age_sum", aggregationBuilder -> aggregationBuilder
                                .sum(sumAggregationBuilder -> sumAggregationBuilder
                                        .field("age")))
                , Poet.class);
        logger.info(response.toString());

        //类似 select count distinct(age) from poet-index
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .aggregations("age_count", aggregationBuilder -> aggregationBuilder
                                .cardinality(cardinalityAggregationBuilder -> cardinalityAggregationBuilder.field("age")))
                , Poet.class);
        logger.info(response.toString());

        //数量、最大、最小、平均、求和
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

        //类似 select avg(age) from poet-index where name='李白'
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .bool(boolQueryBuilder -> boolQueryBuilder
                                        .filter(queryBuilder2 -> queryBuilder2
                                                .term(termQueryBuilder -> termQueryBuilder
                                                        .field("name").value("李白")))))
                        .aggregations("ave_age", aggregationBuilder -> aggregationBuilder
                                .avg(averageAggregationBuilder -> averageAggregationBuilder.field("age")))
                , Poet.class);
        logger.info(response.toString());
    }

    /**
     * suggest查询,推荐搜索, 报错
     */
    @Test
    public void searchSuggest() throws IOException {

        ESSuggestQueryVO queryVO = new ESSuggestQueryVO();
        queryVO.setIndexName("test_index");
        queryVO.setSuggestKey("address_suggest");
        queryVO.setParamName("address");
        queryVO.setParamValue("坂田街道");
        queryVO.setSuggestMode(SuggestMode.Always);
        queryVO.setMinWordLength(2);
        esSearchUtil.searchSuggest(queryVO);
    }

    /**
     * 高亮显示
     */
    @Test
    public void searchHighlight() throws IOException {
        ESTermQueryVO queryVO = new ESTermQueryVO();
        queryVO.setIndexName("test_index");
        queryVO.setParamName("address");
        queryVO.setParamValue("西乡街道");
        esSearchUtil.searchHighlight(queryVO);
    }

    /**
     * sql查询，报错
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
        /**成就*/
        private String success;
    }
}