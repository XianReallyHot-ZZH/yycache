package cn.youyou.yycache.core;

/**
 * RESP 协议的数据类型
 */
public enum ReplyType {

    INT,
    ERROR,
    SIMPLE_STRING,
    BULK_STRING,
    ARRAY;

}
