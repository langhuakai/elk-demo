package com.wei.elk.es.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/12/3 04:05:56
 */
public class IdUtil {

    /**
     * 生成随机ID：当前年月日时分秒 +五位随机数
     */
    public static String getRandomID() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        return str + rannum;// 当前时间
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {

        System.out.println(IdUtil.getRandomID());
    }
}
