package com.wei.elk.es.common.vo;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description es sql查询VO
 * @date 2022/12/1 00:15:51
 */
public class ESSqlQueryVO {

    /**
     * 格式化器
     */
    private String formatter;
    /**
     * sql
     */
    private String sql;

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
