package com.wei.elk.es.common.vo;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description ES数据添加的vo
 * @date 2022/11/27 21:29:41
 */
public class ESSimpleDataVO<T> {

    /**
     * 索引名
     */
    private String indexName;
    /**
     * id
     */
    private String id;
    /**
     * 数据实体
     */
    private T data;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
