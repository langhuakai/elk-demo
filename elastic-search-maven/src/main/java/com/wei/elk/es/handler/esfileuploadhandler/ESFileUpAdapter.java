package com.wei.elk.es.handler.esfileuploadhandler;

import com.wei.elk.es.enums.ESAttrEnum;
import com.wei.elk.es.handler.esfieldtypehandler.ESFieldHandler;
import com.wei.elk.es.util.SpringBeanUtil;
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
public class ESFileUpAdapter {

    /**
     * ES导入文件类型对应处理器map
     */
    public static Map<String, Class<ESFileUpHandler>> esFileUpHandlerMap = new HashMap<>();

    public static ESFileUpHandler getHandler(String fileType){
        Class<ESFileUpHandler> strategyClass = esFileUpHandlerMap.get(fileType);
        if(strategyClass==null) {
            return null;
        }
        //从容器中获取对应的策略Bean
        return SpringBeanUtil.getBean(strategyClass);
    }


}
