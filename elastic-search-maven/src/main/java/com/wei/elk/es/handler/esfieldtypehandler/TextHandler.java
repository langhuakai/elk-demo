package com.wei.elk.es.handler.esfieldtypehandler;

import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.PutMappingRequest;
import com.wei.elk.es.announce.ESAttrType;
import com.wei.elk.es.announce.ESFieldHandlerClass;
import com.wei.elk.es.common.entity.ESFieldEntity;
import com.wei.elk.es.enums.ESAttrEnum;
import org.springframework.stereotype.Component;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/11/26 23:19:41
 */
@Component
@ESFieldHandlerClass(ESAttrEnum.TEXT)
public class TextHandler implements ESFieldHandler{
    public void create(String fieldName, ESAttrType esAttrType, TypeMapping.Builder builder) {
        ESFieldEntity esFieldEntity = new ESFieldEntity();
        esFieldEntity.setFieldName(fieldName);
        esFieldEntity.setFieldType(ESAttrEnum.TEXT);
        String analyzer = esAttrType.analyzer();
        String searchAnalyzer = esAttrType.searchAnalyzer();
        builder.properties(fieldName, propertyBuilder -> propertyBuilder.text(textPropertyBuilder -> textPropertyBuilder.analyzer(analyzer).searchAnalyzer(searchAnalyzer)));
    }

    @Override
    public void modify(String fieldName, ESAttrType esAttrType, PutMappingRequest.Builder builder) {
        ESFieldEntity esFieldEntity = new ESFieldEntity();
        esFieldEntity.setFieldName(fieldName);
        esFieldEntity.setFieldType(ESAttrEnum.TEXT);
        // String analyzer = esAttrType.analyzer();
        String searchAnalyzer = esAttrType.searchAnalyzer();
        builder.properties(fieldName, propertyBuilder -> propertyBuilder.text(textPropertyBuilder -> textPropertyBuilder.searchAnalyzer(searchAnalyzer)));
    }
}
