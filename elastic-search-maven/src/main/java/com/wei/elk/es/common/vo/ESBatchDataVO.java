package com.wei.elk.es.common.vo;

import com.wei.elk.es.common.entity.ESBatchDataEntity;

import java.util.List;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description ES数据添加的vo
 * @date 2022/11/27 21:29:41
 */
public class ESBatchDataVO {

    /**
     * 索引名
     */
    private String indexName;
    /**
     * 数据实体集合
     */
    private List<? extends ESBatchDataEntity> data;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public List<? extends ESBatchDataEntity> getData() {
        return data;
    }

    public void setData(List<? extends ESBatchDataEntity> data) {
        this.data = data;
    }
}
