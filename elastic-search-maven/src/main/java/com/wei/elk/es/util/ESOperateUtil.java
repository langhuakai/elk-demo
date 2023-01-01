package com.wei.elk.es.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.wei.elk.es.common.entity.ESBatchDataEntity;
import com.wei.elk.es.common.vo.ESBatchDataVO;
import com.wei.elk.es.common.vo.ESBatchDeleteVO;
import com.wei.elk.es.common.vo.ESSimpleDataVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/11/26 13:11:08
 */
@Component
public class ESOperateUtil {

    private static final Logger logger = LoggerFactory.getLogger(ESOperateUtil.class.getName());

    private static final String INDEX_NAME = "huji";

    @Autowired
    private ElasticsearchClient client;
    /**
     * 创建单个文档
     */
    public void createDoc(ESSimpleDataVO vo) throws IOException {
        CreateResponse response = client.create(builder -> builder.index(vo.getIndexName()).id(vo.getId()).document(vo.getData()));
        logger.info(response.toString());
    }

    /**
     * 删除文档
     */
    public void deleteDoc(ESSimpleDataVO vo) throws IOException {
        DeleteResponse response = client.delete(builder -> builder.index(vo.getIndexName()).id(vo.getId()));
        logger.info(response.toString());
    }

    /**
     * 清空文档
     */
    public void trunkDoc(String indexName) throws IOException {
        DeleteByQueryResponse response = client.deleteByQuery(builder -> builder.index(indexName)
                .query(queryBuilder -> queryBuilder
                    .matchAll(matchAllQueryBuilder -> matchAllQueryBuilder)));
        logger.info(response.toString());
    }

    /**
     * 修改文档，只修改设置的字段
     */
    public void updateDocMap(ESSimpleDataVO vo) throws IOException {

        UpdateResponse response = client.update(builder -> builder.index(vo.getIndexName()).id(vo.getId()).doc(vo.getData()), Map.class);
        logger.info(response.toString());
    }

    /**
     * 修改文档，只修改设置的字段
     */
    public void updateDocEntity(ESSimpleDataVO vo) throws IOException {
        UpdateResponse response = client.update(builder -> builder.index(vo.getIndexName()).id(vo.getId()).doc(vo.getData()).docAsUpsert(true), vo.getData().getClass());
        logger.info(response.toString());
    }

    /**
     * 新增或修改文档，修改时所有的字段都会覆盖(相当于先删除在新增)
     */
    public void createOrUpdateDoc(ESSimpleDataVO vo) throws IOException {

        //更新所有字段
        IndexResponse response = client.index(builder -> builder.index(vo.getIndexName()).id(vo.getId()).document(vo.getData()));
        logger.info(response.toString());
    }


    /**
     * 批量新增
     */
    public void batchInsert(ESBatchDataVO batchDataVO) throws IOException {
        List<BulkOperation> list = new ArrayList<>();
        List<? extends ESBatchDataEntity> dataList = batchDataVO.getData();
        //批量新增
        for (ESBatchDataEntity esBatchDataEntity : dataList) {
            list.add(new BulkOperation.Builder().create(builder -> builder.index(batchDataVO.getIndexName()).id(esBatchDataEntity.getId()).document(esBatchDataEntity)).build());
        }
        BulkResponse response = client.bulk(builder -> builder.index(INDEX_NAME).operations(list));
        // logger.info(response.toString());
        logger.info("-------------------------------------------数据已插入------------------------------------------------");
    }

    /**
     * 批量删除
     */
    public void batchDelete(ESBatchDeleteVO vo) throws IOException {
        List<BulkOperation> list = new ArrayList<>();
        List<String> ids = vo.getIds();
        //批量新增
        for (String id : ids) {
            list.add(new BulkOperation.Builder().delete(builder -> builder.index(vo.getIndexName()).id(id)).build());
        }
        BulkResponse response = client.bulk(builder -> builder.index(vo.getIndexName()).operations(list));
        logger.info(response.toString());

    }
}
