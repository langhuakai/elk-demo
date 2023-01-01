package com.wei.elk.es.util;

import com.wei.elk.es.announce.ESId;
import com.wei.elk.es.common.entity.ESBatchDataEntity;

import java.lang.reflect.Field;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/12/3 03:50:44
 */
public class ESIdFieldUtil {

    /***
     * @description 获取id字段的值
     *  * @param data
     * @return void
     * @author
     * @date 2022/12/1 19:28
     */
    public static <T extends ESBatchDataEntity>  void analysisId(T data) {
        Class clazz = data.getClass();
        for(Field field : clazz.getDeclaredFields()){
            // 获取id注解
            ESId esAttrType = field.getAnnotation(ESId.class);
            if (null != esAttrType) {
                field.setAccessible(true);
                try {
                    String esId = (String) field.get(data);
                    data.setId(esId);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
