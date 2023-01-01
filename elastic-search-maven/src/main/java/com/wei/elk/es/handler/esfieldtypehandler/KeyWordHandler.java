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
 * @date 2022/11/26 23:07:10
 */
@Component
@ESFieldHandlerClass(ESAttrEnum.KEY_WORD)
public class KeyWordHandler implements ESFieldHandler{

    /***
     * @description TODO
     *  * @param fieldName 属性名
     * @param esAttrType 注解，后期可能会从注解拿分词器等其他设置，所以要将此参数传进来
     * @param builder
     * @return void
     * @author
     * @date 2022/12/2 1:41
     */
    public void create(String fieldName, ESAttrType esAttrType, TypeMapping.Builder builder) {
        /*ESFieldEntity esFieldEntity = new ESFieldEntity();
        esFieldEntity.setFieldName(fieldName);
        esFieldEntity.setFieldType(ESAttrEnum.KEY_WORD);*/
        builder.properties(fieldName, propertyBuilder -> propertyBuilder.keyword(keywordPropertyBuilder -> keywordPropertyBuilder));
    }

    @Override
    public void modify(String fieldName, ESAttrType esAttrType, PutMappingRequest.Builder builder) {
        /*ESFieldEntity esFieldEntity = new ESFieldEntity();
        esFieldEntity.setFieldName(fieldName);
        esFieldEntity.setFieldType(ESAttrEnum.KEY_WORD);*/
        builder.properties(fieldName, propertyBuilder -> propertyBuilder.keyword(keywordPropertyBuilder -> keywordPropertyBuilder));
    }
}
