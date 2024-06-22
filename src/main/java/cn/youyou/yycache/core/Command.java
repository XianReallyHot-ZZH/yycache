package cn.youyou.yycache.core;

public interface Command {

    String name();

    Reply<?> execute(YYCache cache, String[] args);

}
