package com.wei.elk.es.announce;

import com.wei.elk.es.enums.ESAttrEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description ES字段类型
 * @date 2022/11/26 18:16:08
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ESAttrType {
    /**
     * ES数据类型枚举
     * @return
     */
    ESAttrEnum value();

    /**
     * 分词器
     * @return
     */
    String analyzer() default "ik_max_word";

    /**
     * 搜索分词器
     * @return
     */
    String searchAnalyzer() default "ik_max_word";
}
