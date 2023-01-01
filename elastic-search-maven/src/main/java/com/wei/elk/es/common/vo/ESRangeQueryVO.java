package com.wei.elk.es.common.vo;

import com.wei.elk.es.common.entity.PageQueryEntity;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description 范围查询实体类
 * @date 2022/11/29 00:38:27
 */
public class ESRangeQueryVO<T> extends PageQueryEntity {

    /**
     * 索引名
     */
    private String indexName;
    /**
     * 参数名
     */
    private String paramName;
    /**
     * 范围起
     */
    private T rangeFrom;
    /**
     * 范围止
     */
    private T rangeTo;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public T getRangeFrom() {
        return rangeFrom;
    }

    public void setRangeFrom(T rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    public T getRangeTo() {
        return rangeTo;
    }

    public void setRangeTo(T rangeTo) {
        this.rangeTo = rangeTo;
    }
}
