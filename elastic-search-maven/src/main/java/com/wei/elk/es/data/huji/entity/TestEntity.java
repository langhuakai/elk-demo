package com.wei.elk.es.data.huji.entity;

import com.wei.elk.es.announce.ESAttrType;
import com.wei.elk.es.announce.ESId;
import com.wei.elk.es.common.entity.ESBatchDataEntity;
import com.wei.elk.es.enums.ESAttrEnum;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/11/27 19:03:46
 */
public class TestEntity extends ESBatchDataEntity {

    /**
     * 姓名
     */
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String name;
    /**
     * 公民身份证号码
     */
    @ESAttrType(ESAttrEnum.KEY_WORD)
    @ESId
    private String idcardNo;

    /**
     * 住址
     */
    @ESAttrType(ESAttrEnum.TEXT)
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "TestEntity{" +
                "name='" + name + '\'' +
                ", idcardNo='" + idcardNo + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
