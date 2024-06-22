package cn.youyou.yycache.core;

public interface Command {

    String OK = "OK";

    String CRLF = "\r\n";

    String name();

    Reply<?> execute(YYCache cache, String[] args);

    /**
     * 获取指令外，简单key-value形式指令的参数的key
     *
     * @param args
     * @return
     */
    default String getKey(String[] args) {
        return args[4];
    }

    /**
     * 获取指令外，简单key-value形式指令的参数的value
     *
     * @param args
     * @return
     */
    default String getValue(String[] args) {
        return args[6];
    }

    /**
     * 获取指令外，所有输入参数
     *
     * @param args
     * @return
     */
    default String[] getParams(String[] args) {
        int len = (args.length - 3) / 2;
        String[] keys = new String[len];
        for (int i = 0; i < len; i++) {
            keys[i] = args[4 + i * 2];
        }
        return keys;
    }

    /**
     * 获取指令外，除去一个参数后的剩余所有参数
     *
     * @param args
     * @return
     */
    default String[] getParamsNoKey(String[] args) {
        int len = (args.length - 5) / 2;
        String[] keys = new String[len];
        for (int i = 0; i < len; i++) {
            keys[i] = args[6 + i * 2];
        }
        return keys;
    }

    /**
     * 获取指令外，以键值对出现的入参形式，对应的所有key值
     *
     * @param args
     * @return
     */
    default String[] getKeys(String[] args) {
        int len = (args.length - 3) / 4;
        String[] keys = new String[len];
        for (int i = 0; i < len; i++) {
            keys[i] = args[4 + i * 4];
        }
        return keys;
    }

    /**
     * 获取指令外，以键值对出现的入参形式，对应的所有value值
     *
     * @param args
     * @return
     */
    default String[] getVals(String[] args) {
        int len = (args.length - 3) / 4;
        String[] vals = new String[len];
        for (int i = 0; i < len; i++) {
            vals[i] = args[6 + i * 4];
        }
        return vals;
    }

    /**
     * 获取指令外，以键值对出现的入参形式, 缓存数据类型为Hash类型时，对应的所有value值
     *
     * @param args
     * @return
     */
    default String[] getHvals(String[] args) {
        int len = (args.length - 5) / 4;
        String[] vals = new String[len];
        for (int i = 0; i < len; i++) {
            vals[i] = args[8 + i * 4];
        }
        return vals;
    }

    /**
     * 获取指令外，以键值对出现的入参形式, 缓存数据类型为Hash类型时，对应的所有key值
     *
     * @param args
     * @return
     */
    default String[] getHkeys(String[] args) {
        int len = (args.length - 5) / 4;
        String[] keys = new String[len];
        for (int i = 0; i < len; i++) {
            keys[i] = args[6 + i * 4];
        }
        return keys;
    }


}
