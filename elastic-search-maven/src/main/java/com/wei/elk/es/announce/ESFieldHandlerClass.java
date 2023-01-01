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
 * @description
 * @date 2022/11/27 19:40:26
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ESFieldHandlerClass {
    /**
     * ES数据类型枚举
     * @return
     */
    ESAttrEnum value();
}
