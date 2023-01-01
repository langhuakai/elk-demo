package com.wei.elk.es.data.huji.entity;

import com.wei.elk.es.announce.ESAttrType;
import com.wei.elk.es.common.entity.ESBatchDataEntity;
import com.wei.elk.es.enums.ESAttrEnum;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description 京东13G数据实体类
 * @date 2022/12/3 02:17:44
 */
public class JDEntity extends ESBatchDataEntity {

    /**
     * 真实姓名
     */
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String realName;
    /**
     * 京东用户名
     */
    @ESAttrType(ESAttrEnum.TEXT)
    private String userName;
    /**
     * 密码
     */
    @ESAttrType(ESAttrEnum.TEXT)
    private String password;
    /**
     * 邮箱
     */
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String email;
    /**
     * 身份证号
     */
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String idcardNo;
    /**
     * 手机号
     */
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String cellphoneNo;
    /**
     * 电话号
     */
    @ESAttrType(ESAttrEnum.KEY_WORD)
    private String phoneNo;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    public String getCellphoneNo() {
        return cellphoneNo;
    }

    public void setCellphoneNo(String cellphoneNo) {
        this.cellphoneNo = cellphoneNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
