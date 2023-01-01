package com.wei.elk.es.handler.esfieldtypehandler;

import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.PutMappingRequest;
import com.wei.elk.es.announce.ESAttrType;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/11/27 15:02:25
 */
public interface ESFieldHandler {

    void create(String fieldName, ESAttrType esAttrType, TypeMapping.Builder builder);

    void modify(String fieldName, ESAttrType esAttrType, PutMappingRequest.Builder builder);
}
