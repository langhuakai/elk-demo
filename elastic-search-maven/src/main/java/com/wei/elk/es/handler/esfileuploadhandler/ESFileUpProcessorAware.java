package com.wei.elk.es.handler.esfileuploadhandler;

import com.wei.elk.es.announce.ESFieldHandlerClass;
import com.wei.elk.es.announce.ESFileUpHandlerAnnounce;
import com.wei.elk.es.enums.ESAttrEnum;
import com.wei.elk.es.handler.esfieldtypehandler.ESFieldAdapter;
import com.wei.elk.es.handler.esfieldtypehandler.ESFieldHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/11/27 14:59:38
 */
@Component
public class ESFileUpProcessorAware implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ESFileUpHandler> orderStrategyMap = applicationContext.getBeansOfType(ESFileUpHandler.class);
        orderStrategyMap.forEach((k,v)->{
            Class<ESFileUpHandler> strategyClass = (Class<ESFileUpHandler>) v.getClass();
            String value = strategyClass.getAnnotation(ESFileUpHandlerAnnounce.class).value();
            List<String> fileTypeList = new ArrayList<>();
            String[] fileTypeArray = value.split(",");
            for (String fileType : fileTypeArray) {
                //将class加入map中,type作为key
                ESFileUpAdapter.esFileUpHandlerMap.put(fileType.trim(),strategyClass);
            }
        });
    }
}
