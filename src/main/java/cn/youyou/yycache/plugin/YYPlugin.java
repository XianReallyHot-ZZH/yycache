package cn.youyou.yycache.plugin;

/**
 * plugin interface
 */
public interface YYPlugin {

    String getName();

    void init();

    void startup();

    void shutdown();

}
