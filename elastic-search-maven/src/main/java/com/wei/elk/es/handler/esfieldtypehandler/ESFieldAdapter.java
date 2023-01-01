package com.wei.elk.es.handler.esfieldtypehandler;

import com.wei.elk.es.enums.ESAttrEnum;
import com.wei.elk.es.temp.IdentityStrategy;
import com.wei.elk.es.temp.IdentityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/11/27 15:00:08
 */
@Component
public class ESFieldAdapter {

    /**
     * ES数据类型对应的解析处理器map
     */
    public static Map<ESAttrEnum, Class<ESFieldHandler>> esFieldHandlerMap = new HashMap<>();
    /**
     * ES索引名称和对应实体类map
     */
    public static Map<String, String> esIndexEntityMap = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    public ESFieldHandler getHandler(ESAttrEnum type){
        Class<ESFieldHandler> strategyClass = esFieldHandlerMap.get(type);
        if(strategyClass==null) {
            return null;
        }
        //从容器中获取对应的策略Bean
        return applicationContext.getBean(strategyClass);
    }


}
