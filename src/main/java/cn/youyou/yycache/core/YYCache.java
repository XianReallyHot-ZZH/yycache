package cn.youyou.yycache.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 内存缓存
 */
public class YYCache {

    Map<String, String> map = new HashMap<>();

    public String get(String key) {
        return map.get(key);
    }

    public void set(String key, String value) {
        map.put(key, value);
    }

    /**
     * 批量删除，返回成功删除的个数
     * @param keys
     * @return
     */
    public int del(String... keys) {
        return keys == null ? 0 : (int) Arrays.stream(keys).map(map::remove).filter(Objects::nonNull).count();
    }

    /**
     * 批量判断是否存在
     * @param keys
     * @return
     */
    public int exists(String... keys) {
        return keys == null ? 0 : (int) Arrays.stream(keys).map(map::containsKey).filter(x -> x).count();
    }

    /**
     * 批量获取
     * @param keys
     * @return
     */
    public String[] mget(String... keys) {
        return keys == null ? new String[0] : Arrays.stream(keys).map(map::get).toArray(String[]::new);
    }

    /**
     * 批量设置
     * @param keys
     * @param values
     */
    public void mset(String[] keys, String[] values) {
        if (keys == null || keys.length == 0)
            return;
        for (int i = 0; i < keys.length; i++) {
            set(keys[i], values[i]);
        }
    }

    /**
     * 单笔自增
     * @param key
     * @return
     */
    public int incr(String key) {
        String value = get(key);
        int val = 0;
        try {
            if (value != null) {
                val = Integer.parseInt(value);
            }
            val++;
            set(key, String.valueOf(val));
        } catch (NumberFormatException e) {
            throw e;
        }
        return val;
    }

    /**
     * 单笔自减
     * @param key
     * @return
     */
    public int decr(String key) {
        String value = get(key);
        int val = 0;
        try {
            if (value != null) {
                val = Integer.parseInt(value);
            }
            val--;
            set(key, String.valueOf(val));
        } catch (NumberFormatException e) {
            throw e;
        }
        return val;
    }
}
