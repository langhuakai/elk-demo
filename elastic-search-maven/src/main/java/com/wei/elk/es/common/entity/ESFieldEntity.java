package com.wei.elk.es.common.entity;

import com.wei.elk.es.enums.ESAttrEnum;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description ES字段实体类
 * @date 2022/11/26 22:09:48
 */

/**
 * analyzer指字段定分词器
 * boost指定权重值 8.5版本貌似没有了
 * coerce强制类型转换
 * copy_to值复制给另一字段
 * doc_values是否存储docValues
 * dynamic
 * eager_global_ordinals
 * enabled字段是否可用（设置为false,不会被搜索）
 * fielddata对于分词的字段是否需要做聚合及排序时需要考虑设置该属性
 * fields
 * format指定时间值的格式
 * ignore_above忽略大于
 * ignore_malformed
 * index_options
 * index_phrases
 * index_prefixes
 * index
 * meta
 * normalizer指字段定标准化器
 * norms
 * null_value
 * position_increment_gap
 * properties
 * search_analyzer
 * similarity
 * store
 * term_vector
 */
public class ESFieldEntity {

    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段类型
     */
    private ESAttrEnum fieldType;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public ESAttrEnum getFieldType() {
        return fieldType;
    }

    public void setFieldType(ESAttrEnum fieldType) {
        this.fieldType = fieldType;
    }
}
