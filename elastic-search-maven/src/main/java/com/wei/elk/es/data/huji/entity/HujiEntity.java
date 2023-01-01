package com.wei.elk.es.data.huji.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.wei.elk.es.announce.ESAttrType;
import com.wei.elk.es.announce.ESId;
import com.wei.elk.es.common.entity.ESBatchDataEntity;
import com.wei.elk.es.enums.ESAttrEnum;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description 2016户籍实体类
 * @date 2022/11/25 21:10:17
 */
public class HujiEntity extends ESBatchDataEntity {

    /**
     * 公民身份证号码
     */
    @ExcelProperty(index = 6)
    @ESAttrType(ESAttrEnum.KEY_WORD)
    @ESId
    private String idcardNo;
    /**
     * 姓名
     */
    @ExcelProperty(index = 7)
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String name;
    /**
     * 性别 M：男 F:女
     */
    @ExcelProperty(index = 5)
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String sex;
    /**
     * 民族
     */
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String nation;
    /**
     * 出生 yyyy-MM-dd
     */
    private String birthday;
    /**
     * 住址
     */
    @ExcelProperty(index = 9)
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String address;
    /**
     * 签发机关
     */
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String issAuthority;
    /**
     * 有效期限
     */
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String validPeriod;
    /**
     * 手机号
     */
    @ExcelProperty(index = 8)
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String phoneNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    public String getIssAuthority() {
        return issAuthority;
    }

    public void setIssAuthority(String issAuthority) {
        this.issAuthority = issAuthority;
    }

    public String getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(String validPeriod) {
        this.validPeriod = validPeriod;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
