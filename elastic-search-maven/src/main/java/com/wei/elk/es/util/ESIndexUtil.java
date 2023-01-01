package com.wei.elk.es.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.*;
import com.wei.elk.es.announce.ESAttrType;
import com.wei.elk.es.common.entity.ESIndexEntity;
import com.wei.elk.es.handler.esfieldtypehandler.ESFieldAdapter;
import com.wei.elk.es.handler.esfieldtypehandler.ESFieldHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/11/26 13:06:49
 */
@Component
public class ESIndexUtil {

    private static final Logger logger = LoggerFactory.getLogger(ESIndexUtil.class.getName());

    private static final String INDEX_NAME = "huji";

    @Autowired
    private ElasticsearchClient client;
    @Autowired
    private ESFieldAdapter esFieldAdapter;
    /**
     * 创建索引
     */
    public void createIndex(ESIndexEntity esIndexEntity) throws IOException {
        CreateIndexResponse response = client.indices().create(builder -> builder
                .settings(indexSettingsBuilder -> indexSettingsBuilder
                        .numberOfReplicas(String.valueOf(esIndexEntity.getReplicasNum()))
                        .numberOfShards(String.valueOf(esIndexEntity.getShardsNum())))
                .mappings(typeMappingBuilder -> entityToBuilderCreate(esIndexEntity.getMappingClass(), typeMappingBuilder)
                )
                .index(esIndexEntity.getName()));
        logger.info("acknowledged={}", response.acknowledged());
    }

    /**
     * 修改索引的_mapping信息
     * 字段可以新增，已有的字段只能修改字段的search_analyzer属性
     */
    public void modifyIndex(ESIndexEntity esIndexEntity) throws IOException {
        PutMappingResponse response = client.indices().putMapping(typeMappingBuilder -> entityToBuilderModify(esIndexEntity.getMappingClass(), typeMappingBuilder)
                .index(esIndexEntity.getName())
        );
        logger.info("acknowledged={}", response.acknowledged());
    }

    /**
     * 删除索引
     */
    public void deleteIndex() throws IOException {
        DeleteIndexResponse response = client.indices().delete(builder -> builder.index(INDEX_NAME));
        logger.info("acknowledged={}", response.acknowledged());
    }

    /**
     * 查询索引列表
     */
    public void getIndex() throws IOException {
        //使用 * 也可以
        GetIndexResponse response = client.indices().get(builder -> builder.index("_all"));
        logger.info(response.result().toString());
    }

    /**
     * 查询索引详情
     */
    public void getIndexDetail() throws IOException {
        GetIndexResponse response = client.indices().get(builder -> builder.index(INDEX_NAME));
        logger.info(response.result().toString());
    }

    /**
     * 策略模式解析映射字段（创建时）
     * @param clazz builder
     * @return
     */
    public TypeMapping.Builder entityToBuilderCreate(Class clazz, TypeMapping.Builder builder){
        for(Field field : clazz.getDeclaredFields()){
            // 获取ES类型注解
            ESAttrType esAttrType = field.getAnnotation(ESAttrType.class);
            if (null != esAttrType) {
                field.setAccessible(true);
                String fieldName = field.getName();
                ESFieldHandler esFieldHandler = esFieldAdapter.getHandler(esAttrType.value());
                esFieldHandler.create(fieldName, esAttrType, builder);
            }
        }
        return builder;
    }

    /**
     * 策略模式解析映射字段（修改时）
     * @param clazz builder
     * @return
     */
    public PutMappingRequest.Builder entityToBuilderModify(Class clazz, PutMappingRequest.Builder builder){
        for(Field field : clazz.getDeclaredFields()){
            // 获取ES类型注解
            ESAttrType esAttrType = field.getAnnotation(ESAttrType.class);
            if (null != esAttrType) {
                field.setAccessible(true);
                String fieldName = field.getName();
                ESFieldHandler esFieldHandler = esFieldAdapter.getHandler(esAttrType.value());
                esFieldHandler.modify(fieldName, esAttrType, builder);
            }
        }
        return builder;
    }
}
