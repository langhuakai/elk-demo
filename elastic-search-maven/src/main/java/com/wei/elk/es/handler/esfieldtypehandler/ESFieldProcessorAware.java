package com.wei.elk.es.handler.esfieldtypehandler;

import com.wei.elk.es.announce.ESFieldHandlerClass;
import com.wei.elk.es.enums.ESAttrEnum;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/11/27 14:59:38
 */
@Component
public class ESFieldProcessorAware implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ESFieldHandler> orderStrategyMap = applicationContext.getBeansOfType(ESFieldHandler.class);
        orderStrategyMap.forEach((k,v)->{
            Class<ESFieldHandler> strategyClass = (Class<ESFieldHandler>) v.getClass();
            ESAttrEnum value = strategyClass.getAnnotation(ESFieldHandlerClass.class).value();
            //将class加入map中,type作为key
            ESFieldAdapter.esFieldHandlerMap.put(value,strategyClass);
        });
    }
}
