package com.wei.elk.es.common.vo;

import com.wei.elk.es.common.entity.PageQueryEntity;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description es term和match查询实体类
 * @date 2022/11/28 21:34:52
 */
public class ESTermQueryVO extends PageQueryEntity {

    /**
     * 索引名
     */
    private String indexName;
    /**
     * 参数名
     */
    private String paramName;
    /**
     * 参数值
     */
    private String paramValue;

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

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
