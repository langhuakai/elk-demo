package com.wei.elk.es.common.constant;

import com.wei.elk.es.data.huji.entity.HujiEntity;
import com.wei.elk.es.data.huji.entity.JDEntity;
import com.wei.elk.es.data.huji.entity.TestEntity;
import com.wei.elk.es.data.huji.entity.ZheJiangXueJiEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description 索引名称和实体类对应关系
 * @date 2022/11/28 22:37:18
 */
public class ESIndexEntityMapping {

    public static final Map<String, Class> indexEntityMap;
    static
    {
        indexEntityMap = new HashMap<>();
        indexEntityMap.put("test_index", TestEntity.class);
        indexEntityMap.put("huji", HujiEntity.class);
        indexEntityMap.put("jduser", JDEntity.class);
        indexEntityMap.put("zhejiang_xueji", ZheJiangXueJiEntity.class);
    }

}
