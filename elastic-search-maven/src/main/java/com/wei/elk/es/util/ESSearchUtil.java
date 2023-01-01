package com.wei.elk.es.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.SuggestMode;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.sql.QueryResponse;
import co.elastic.clients.json.JsonData;
import com.alibaba.fastjson.JSON;
import com.wei.elk.es.common.constant.ESIndexEntityMapping;
import com.wei.elk.es.common.entity.ESBatchDataEntity;
import com.wei.elk.es.common.vo.*;
import com.wei.elk.es.data.huji.entity.TestEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/11/26 13:17:31
 */
@Component
public class ESSearchUtil {
    private static final Logger logger = LoggerFactory.getLogger(ESIndexUtil.class.getName());

    private static final String INDEX_NAME = "huji2";

    @Autowired
    private ElasticsearchClient client;
    /**
     * 查询索有文档
     */
    public void getDocAll(String indexName) throws IOException {
        /*SearchResponse<Map> response = client.search(builder -> builder.index(indexName), Map.class);
        logger.info(response.toString());*/
        SearchResponse<Object> response = client.search(builder -> builder.index(indexName), Object.class);
        logger.info(response.toString());
        // logger.info(JSON.toJSONString(response.));
    }

    /**
     * 查询单个文档
     */
    public void getDoc(ESSimpleDataVO vo) throws IOException {
        GetResponse<Map> response = client.get(builder -> builder.index(vo.getIndexName()).id(vo.getId()), Map.class);
        if (response.found()) {
            logger.info(response.source().toString());
        }

        GetResponse response2 = client.get(builder -> builder.index(vo.getIndexName()).id(vo.getId()), ESIndexEntityMapping.indexEntityMap.get(vo.getIndexName()));
        if (response2.found()) {
            logger.info(JSON.toJSONString(response2.source()));
        }
    }

    /**
     * term/terms查询,对输入内容不做分词处理
     */
    public void searchTermMap(ESTermQueryVO queryVO) throws IOException {
        SearchResponse<Map> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(queryVO.getIndexName())
                        .query(queryBuilder -> queryBuilder
                                .term(termQueryBuilder -> termQueryBuilder
                                        .field(queryVO.getParamName()).value(queryVO.getParamValue())))
                        /*.sort(sortOptionsBuilder -> sortOptionsBuilder
                                .field(fieldSortBuilder -> fieldSortBuilder
                                        .field("name").order(SortOrder.Asc)))
                        .source(sourceConfigBuilder -> sourceConfigBuilder
                                .filter(sourceFilterBuilder -> sourceFilterBuilder
                                        .includes("age", "name")))*/
                        .from(caculateESPageDataFrom(queryVO.getPageNum(), queryVO.getPageSize()))
                        .size(queryVO.getPageSize())
                , Map.class);
        logger.info(response.toString());
    }

    /**
     * term/terms查询,对输入内容不做分词处理
     */
    public void searchTermEntity(ESTermQueryVO queryVO) throws IOException {
        Class clazz = ESIndexEntityMapping.indexEntityMap.get(queryVO.getIndexName());
        SearchResponse response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(queryVO.getIndexName())
                        .query(queryBuilder -> queryBuilder
                                .term(termQueryBuilder -> termQueryBuilder
                                        .field(queryVO.getParamName()).value(queryVO.getParamValue())))
                        /*.sort(sortOptionsBuilder -> sortOptionsBuilder
                                .field(fieldSortBuilder -> fieldSortBuilder
                                        .field("name").order(SortOrder.Asc)))
                        .source(sourceConfigBuilder -> sourceConfigBuilder
                                .filter(sourceFilterBuilder -> sourceFilterBuilder
                                        .includes("age", "name")))*/
                        .from(caculateESPageDataFrom(queryVO.getPageNum(), queryVO.getPageSize()))
                        .size(queryVO.getPageSize())
                , ESIndexEntityMapping.indexEntityMap.get(queryVO.getIndexName()));
        logger.info(response.toString());
    }

    /**
     * term/terms查询,对输入内容不做分词处理
     */
    public void searchTermsMap(ESTermQueryVO queryVO) throws IOException {
        List<FieldValue> words = new ArrayList<>();
        String[] values = queryVO.getParamValue().replaceAll("\\s*|\t|\r|\n", "").split(",");
        for (String value : values) {
            words.add(new FieldValue.Builder().stringValue(value).build());
        }
        SearchResponse<Map> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(queryVO.getIndexName())
                        .query(queryBuilder -> queryBuilder
                                .terms(termsQueryBuilder -> termsQueryBuilder
                                        .field(queryVO.getParamName()).terms(termsQueryFieldBuilder -> termsQueryFieldBuilder.value(words))))
                        /*.source(sourceConfigBuilder -> sourceConfigBuilder
                                .filter(sourceFilterBuilder -> sourceFilterBuilder
                                        .excludes("about")))*/
                        .from(caculateESPageDataFrom(queryVO.getPageNum(), queryVO.getPageSize()))
                        .size(queryVO.getPageSize())
                , Map.class);
        logger.info(response.toString());
    }

    /**
     * term/terms查询,对输入内容不做分词处理
     */
    public void searchTermsEntity(ESTermQueryVO queryVO) throws IOException {
        List<FieldValue> words = new ArrayList<>();
        String[] values = queryVO.getParamValue().replaceAll("\\s*|\t|\r|\n", "").split(",");
        for (String value : values) {
            words.add(new FieldValue.Builder().stringValue(value).build());
        }
        SearchResponse<Map> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(queryVO.getIndexName())
                        .query(queryBuilder -> queryBuilder
                                .terms(termsQueryBuilder -> termsQueryBuilder
                                        .field(queryVO.getParamName()).terms(termsQueryFieldBuilder -> termsQueryFieldBuilder.value(words))))
                        /*.source(sourceConfigBuilder -> sourceConfigBuilder
                                .filter(sourceFilterBuilder -> sourceFilterBuilder
                                        .excludes("about")))*/
                        .from(caculateESPageDataFrom(queryVO.getPageNum(), queryVO.getPageSize()))
                        .size(queryVO.getPageSize())
                , ESIndexEntityMapping.indexEntityMap.get(queryVO.getIndexName()));
        logger.info(response.toString());
    }

    /**
     * range查询,范围查询
     */
    public void searchRange(ESRangeQueryVO queryVO) throws IOException {
        SearchResponse response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(queryVO.getIndexName())
                        .query(queryBuilder -> queryBuilder
                                .range(rangeQueryBuilder -> rangeQueryBuilder
                                        .field(queryVO.getParamName()).gte(JsonData.of(queryVO.getRangeFrom())).lt(JsonData.of(queryVO.getRangeTo()))))
                        .from(caculateESPageDataFrom(queryVO.getPageNum(), queryVO.getPageSize()))
                        .size(queryVO.getPageSize())
                , ESIndexEntityMapping.indexEntityMap.get(queryVO.getIndexName()));
        logger.info(response.toString());
    }

    /**
     * match查询，对输入内容先分词再查询
     */
    public void searchMatch(ESTermQueryVO queryVO) throws IOException {
        SearchResponse<Map> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(queryVO.getIndexName())
                        .query(queryBuilder -> queryBuilder
                                .match(matchQueryBuilder -> matchQueryBuilder
                                        .field(queryVO.getParamName()).query(queryVO.getParamValue())))
                        .from(caculateESPageDataFrom(queryVO.getPageNum(), queryVO.getPageSize()))
                        .size(queryVO.getPageSize())
                , Map.class);
        logger.info(response.toString());
        /*SearchResponse response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(queryVO.getIndexName())
                        .query(queryBuilder -> queryBuilder
                                .match(matchQueryBuilder -> matchQueryBuilder
                                        .field(queryVO.getParamName()).query(queryVO.getParamValue())))
                        .from(caculateESPageDataFrom(queryVO.getPageNum(), queryVO.getPageSize()))
                        .size(queryVO.getPageSize())
                , ESIndexEntityMapping.indexEntityMap.get(queryVO.getIndexName()));
        logger.info(response.toString());*/
    }

    /**
     * multi_match查询,- 多个field对应一个内容
     */
    public void searchMultiMatch(ESTermQueryVO queryVO) throws IOException {
        List<String> paramNames = Arrays.asList(queryVO.getParamName().trim().split(","));
        SearchResponse<Map> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(queryVO.getIndexName())
                        .query(queryBuilder -> queryBuilder
                                .multiMatch(multiMatchQueryBuilder -> multiMatchQueryBuilder
                                        .fields(paramNames).query(queryVO.getParamValue())))
                        .from(caculateESPageDataFrom(queryVO.getPageNum(), queryVO.getPageSize()))
                        .size(queryVO.getPageSize())
                , Map.class);
        logger.info(response.toString());
    }

    /**
     * match_phrase 查询,匹配整个查询字符串
     * 1、match_phrase还是分词后去搜的
     * 2、目标文档需要包含分词后的所有词
     * 3、目标文档还要保持这些词的相对顺序和文档中的一致
     */
    public void searchMatchPhrase(ESTermQueryVO queryVO) throws IOException {
        SearchResponse<Map> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(queryVO.getIndexName())
                        .query(queryBuilder -> queryBuilder
                                .matchPhrase(matchPhraseQueryBuilder -> matchPhraseQueryBuilder.field(queryVO.getParamName()).query(queryVO.getParamValue())))
                        .from(caculateESPageDataFrom(queryVO.getPageNum(), queryVO.getPageSize()))
                        .size(queryVO.getPageSize())
                , Map.class);
        logger.info(response.toString());
    }

    /**
     * match_all 查询,查询所有
     */
    public void searchMatchAll(ESTermQueryVO queryVO) throws IOException {
        SearchResponse<Map> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(queryVO.getIndexName())
                        .query(queryBuilder -> queryBuilder
                                .matchAll(matchAllQueryBuilder -> matchAllQueryBuilder))
                        .from(caculateESPageDataFrom(queryVO.getPageNum(), queryVO.getPageSize()))
                        .size(queryVO.getPageSize())
                , Map.class);
        logger.info(response.toString());
    }

    /**
     * query_string 查询
     */
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
     * elasticsearch中涉及了 4种类别的suggester。分别是：
     * Term Suggester
     * Phrase Suggester
     * Completion Suggester
     * Context Suggester
     */
    public void searchSuggest(ESSuggestQueryVO queryVO) throws IOException {
        SearchResponse<Map> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(queryVO.getIndexName())
                        .suggest(suggesterBuilder -> suggesterBuilder
                                .suggesters(queryVO.getSuggestKey(), fieldSuggesterBuilder -> fieldSuggesterBuilder
                                        .text(queryVO.getParamValue())
                                        .term(termSuggesterBuilder -> termSuggesterBuilder
                                                .field(queryVO.getParamName())
                                                .suggestMode(queryVO.getSuggestMode())
                                                .minWordLength(queryVO.getMinWordLength())
                                        )
                                )
                        )
                        .from(caculateESPageDataFrom(queryVO.getPageNum(), queryVO.getPageSize()))
                        .size(queryVO.getPageSize())
                , Map.class);
        logger.info(response.toString());
    }

    /**
     * 高亮显示
     */
    public void searchHighlight(ESTermQueryVO queryVO) throws IOException {
        SearchResponse<Map> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(queryVO.getIndexName())
                        .query(queryBuilder -> queryBuilder
                                .match(matchQueryBuilder -> matchQueryBuilder
                                        .field(queryVO.getParamName()).query(queryVO.getParamValue())))
                        .highlight(highlightBuilder -> highlightBuilder
                                .preTags("<span color='red'>")
                                .postTags("</span>")
                                .fields(queryVO.getParamName(), highlightFieldBuilder -> highlightFieldBuilder))
                .from(caculateESPageDataFrom(queryVO.getPageNum(), queryVO.getPageSize()))
                .size(queryVO.getPageSize())
                , Map.class);
        logger.info(response.toString());
    }

    /**
     * sql查询，报错
     */
    public void searchSql(ESSqlQueryVO queryVO) throws IOException {
        QueryResponse response = client.sql().query(builder -> builder
                .format(queryVO.getFormatter()).query(queryVO.getSql()));
        logger.info(response.toString());
    }

    /**
     * 计算ES分页查询数据开始的数值
     * @param pageNum
     * @param pageSize
     * @return
     */
    private int caculateESPageDataFrom(int pageNum, int pageSize) {
        int from = pageNum * pageSize - pageSize;
        return from;
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
