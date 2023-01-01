package com.wei.elk.es.common.entity;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description ES索引实体类
 * @date 2022/11/26 16:57:48
 */
public class ESIndexEntity<T> {

    /**
     * 索引名称
     */
    private String name;
    /**
     * 副本数量 默认1
     */
    private int replicasNum = 1;
    /**
     * 分片数量 默认1
     */
    private int shardsNum = 1;

    /**
     * 映射实体
     */
    private Class mappingClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReplicasNum() {
        return replicasNum;
    }

    public void setReplicasNum(int replicasNum) {
        this.replicasNum = replicasNum;
    }

    public int getShardsNum() {
        return shardsNum;
    }

    public void setShardsNum(int shardsNum) {
        this.shardsNum = shardsNum;
    }

    public Class getMappingClass() {
        return mappingClass;
    }

    public void setMappingClass(Class mappingClass) {
        this.mappingClass = mappingClass;
    }
}
