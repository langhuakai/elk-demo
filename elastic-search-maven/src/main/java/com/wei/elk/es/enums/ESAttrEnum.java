package com.wei.elk.es.enums;

/**
 * @author wei
 * @version 1.0
 * @project elk-maven-demo
 * @description ES数据类型枚举
 * @date 2022/11/26 18:27:04
 */

/**
 * 1）核心数据类型：
 * string字符串：
 * text：文本类型（分词）；
 * keyword：关键字类型（不分词）；
 * numeric-数值类型：
 * long, integer, short, byte, double, float, half_float, scaled_float
 * date-日期类型（存储自unix纪元以来的毫秒数）；
 * date_nanos：日期纳秒类型（存储自unix纪元以来的纳秒数）；
 * boolean-布尔类型；
 * binary-二进制类型；
 * range-范围类型:
 * integer_range, float_range, long_range, double_range, date_range
 * 2）复杂数据类型
 * object：对象类型；存储单个json对象；
 * nested：嵌套类型；存储json对象数组；
 * 3）数组类型（字段数组类型）
 * 数组不需要专用的数据类型。一个字段默认可以包含0个或多个值。然而，数组中的所有值必须是同一种类型；
 * 4）多字段类型：
 * 根据不同目的以不同方式索引同一字段是有帮助的；
 * 举例，字符串字段可以被映射为 text字段以便全文搜索，也可以映射为keword以便于排序或聚合。
 * 5）其他数据类型
 * 如地理位置信息数据类型，ip数据类型等（没有罗列完全）；
 * ————————————————
 * 版权声明：本文为CSDN博主「PacosonSWJTU」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/PacosonSWJTU/article/details/127200021
 */
public enum ESAttrEnum {
    KEY_WORD, TEXT,
    LONG, INTEGER, SHORT, BYTE, DOUBLE, FLOAT, HALF_FLOAT, SCALED_FLOAT,
    DATE,
    DATE_NANOS,
    BOOLEAN,
    BINARY,
    INTEGER_RANGE, FLOAT_RANGE, LONG_RANGE, DOUBLE_RANGE, DATE_RANGE,
    OBJECT, NESTED
}
