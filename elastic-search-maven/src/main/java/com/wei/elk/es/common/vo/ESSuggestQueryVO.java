package com.wei.elk.es.common.vo;

import co.elastic.clients.elasticsearch._types.SuggestMode;
import com.wei.elk.es.common.entity.PageQueryEntity;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description suggest查询实体类
 * @date 2022/12/1 00:55:03
 */
public class ESSuggestQueryVO extends PageQueryEntity {

    /**
     * 索引名
     */
    private String indexName;
    /**
     * suggest名
     */
    private String suggestKey;
    /**
     * 参数名
     */
    private String paramName;
    /**
     * 参数值
     */
    private String paramValue;
    /**
     * suggest类型
     */
    private SuggestMode suggestMode;
    /**
     * 最小字符长度
     */
    private int minWordLength = 2;

    public ESSuggestQueryVO() {
        this.suggestMode = SuggestMode.Always;
        this.minWordLength = 2;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getSuggestKey() {
        return suggestKey;
    }

    public void setSuggestKey(String suggestKey) {
        this.suggestKey = suggestKey;
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

    public SuggestMode getSuggestMode() {
        return suggestMode;
    }

    public void setSuggestMode(SuggestMode suggestMode) {
        this.suggestMode = suggestMode;
    }

    public int getMinWordLength() {
        return minWordLength;
    }

    public void setMinWordLength(int minWordLength) {
        this.minWordLength = minWordLength;
    }
}
