package com.wei.elk.es.data.huji.entity;

import com.wei.elk.es.announce.ESAttrType;
import com.wei.elk.es.announce.ESId;
import com.wei.elk.es.announce.ESIndexName;
import com.wei.elk.es.common.entity.ESBatchDataEntity;
import com.wei.elk.es.enums.ESAttrEnum;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description 浙江1100万学籍实体类
 * @date 2022/12/4 22:28:25
 */
@ESIndexName("zhejiang_xueji")
public class ZheJiangXueJiEntity extends ESBatchDataEntity {

    /**
     * 身份证号
     */
    @ESId
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String idcardNo;
    /**
     * 姓名
     */
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String name;
    /**
     * 籍贯
     */
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String nativePlace;
    /**
     * 家长姓名
     */
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String parentName;
    /**
     * 手机号
     */
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String cellPhoneNo;
    /**
     * 详细地址
     */
    @ESAttrType(ESAttrEnum.TEXT)
    private String addressDetail;
    /**
     * 籍贯2
     */
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String nativePlace2;
    /**
     * 班级名称
     */
    @ESAttrType(ESAttrEnum.TEXT)
    private String className;
    /**
     * 学校名称
     */
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String schoolName;

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getCellPhoneNo() {
        return cellPhoneNo;
    }

    public void setCellPhoneNo(String cellPhoneNo) {
        this.cellPhoneNo = cellPhoneNo;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getNativePlace2() {
        return nativePlace2;
    }

    public void setNativePlace2(String nativePlace2) {
        this.nativePlace2 = nativePlace2;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
