package com.wei.elk.es.common.vo;

import java.util.List;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/11/28 03:06:23
 */
public class ESBatchDeleteVO {

    /**
     * 索引名
     */
    private String indexName;
    /**
     * id集合
     */
    private List<String> ids;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
