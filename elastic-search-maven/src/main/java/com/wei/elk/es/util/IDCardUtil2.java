package com.wei.elk.es.util;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description
 * @date 2022/11/29 03:33:59
 */
public class IDCardUtil2 {

    /*
    校验规则：
        如果为15位，只能是15位数字；前两位满足省/直辖市的行政区划代码。
        如果为18位，允许为18位数字，如出现字母只能在最后一位，且仅能为“X”；
        18位中包含年月的字段满足日期的构成规则；前两位满足省/直辖市的行政区划代码；
        最后一位校验位满足身份证的校验规则（身份证校验规则见附录）。
        附录：身份证校验规则
            公民身份证号码校验公式为RESULT = ∑( A[i] * W[i] ) mod 11。
            其中,i表示号码字符从右至左包括校验码在内的位置序号;A[i]表示第I位置上的数字的数值;W[i]表示第i位置上的加权因子,其值如下:

            i 18 17 16 15 14 13 12 11 10 9 8 7 6 5 4 3 2
            W[i] 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2

            RESULT 0 1 2 3 4 5 6 7 8 9 10
            校验码A[1] 1 0 X 9 8 7 6 5 4 3 2
     */

    private static int[] weight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2}; // 每一位对应的权重
    private static char[] check = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    /***
     * @description 身份证号码生成
     * @param areaCode 6位地区编码
     * @param birthday 出生日期 19980625
     * @param sex   性别：男：1，女：0
     * @return java.lang.String
     * @author
     * @date 2022/12/6 1:56
     */
    private static List<String> generateIdCardNo(String areaCode, String birthday, String sex) {
        List<String> idcardList = new ArrayList<>();
        String combine = areaCode + birthday;
        // 计算前14位加权相乘的和
        char[] areaArray = combine.toCharArray();
        int sumpre = 0;
        int sumAll = 0;
        for (int i = 0; i < 14; i ++) {
            char c = areaArray[i];
            int intValue = c - '0'; // 将字符转数字
            // 按位加权求和
            sumpre += (intValue * weight[i]);
        }
        int j;
        // 女性从0开始加，男性从1开始加
        if (sex == "0") {
            j = 0;
        } else {
            j = 1;
        }
        for (; j < 1000; j = j + 2) {
            String lastString = String.format("%03d", j);
            char[] lastCharArray = lastString.toCharArray();
            int sumLast = 0;
            for (int k = 0; k < 3; k ++) {
                char d = lastCharArray[k];
                int intValue = d - '0'; // 将字符转数字
                sumLast += (intValue * weight[14 + k]);
            }
            sumAll = sumpre + sumLast;
            int reminder = sumAll % 11;
            char checkChar = check[reminder];
            String idcardNo = combine + lastString + checkChar;
            idcardList.add(idcardNo);
            System.out.println(idcardNo);
        }
        return idcardList;
    }

    public static void main(String[] args) {
        generateIdCardNo("360681", "19380726", "1");
    }
}
