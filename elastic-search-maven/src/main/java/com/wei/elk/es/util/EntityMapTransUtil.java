package com.wei.elk.es.util;

import com.wei.elk.es.announce.ESAttrType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description 实体类与map互转工具类
 * @date 2022/11/26 17:59:57
 */
public class EntityMapTransUtil {

    private static final Logger logger = LoggerFactory.getLogger(EntityMapTransUtil.class);
    /**
     * Java实体类转Map：方法一
     * @param obj
     * @return
     */
    public static Map<String, Object> entityToMap1(Object obj){
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> clazz = obj.getClass();
        for(Field field : clazz.getDeclaredFields()){
            field.setAccessible(true);
            String fieldName = field.getName();
            Object object = null;
            try {
                object = field.get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.info(e.getMessage());
            }
            map.put(fieldName, object);
        }
        return map;
    }

    /**
     * Java实体类转Map：方法二
     * @param obj
     * @return
     */
    public static Map<String, Object> entityToMap2(Object obj){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return map;
    }

    /**
     * Map转实体类：要转换的Map的key跟实体类属性名相同的数据会转过去，不相同的字段会为null
     * @param clazz
     * @param map
     * @return
     */
    public static <T> T mapToESFieldList(Class<T> clazz,Map<String, Object> map){
        T obj = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            obj = clazz.newInstance(); // 创建 JavaBean 对象
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            // 给 JavaBean 对象的属性赋值
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {
                    Object value = map.get(propertyName);
                    Object[] args = new Object[1];
                    args[0] = value;
                    try {
                        descriptor.getWriteMethod().invoke(obj, args);
                    } catch (InvocationTargetException e) {
                        logger.info(e.getMessage());
                    }
                }
            }
        } catch (IllegalAccessException e) {
            logger.info(e.getMessage());
        } catch (IntrospectionException e) {
            logger.info(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.info(e.getMessage());
        } catch (InstantiationException e) {
            logger.info(e.getMessage());
        }
        return (T)obj;
    }

    /**
     * Java实体类转Map：方法一
     * @param obj
     * @return
     */
    public static Map<String, Object> entityToMap3(Object obj){
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> clazz = obj.getClass();
        for(Field field : clazz.getDeclaredFields()){
            // 获取ES类型注解
            ESAttrType esAttrType = field.getAnnotation(ESAttrType.class);
            field.setAccessible(true);
            String fieldName = field.getName();
            Object object = null;
            try {
                object = field.get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.info(e.getMessage());
            }
            map.put(fieldName, object);
        }
        return map;
    }
}
