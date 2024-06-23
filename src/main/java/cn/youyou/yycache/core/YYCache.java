package cn.youyou.yycache.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Stream;

/**
 * 内存缓存
 */
public class YYCache {

    Map<String, CacheEntry<?>> map = new HashMap<>();

    // ===============  1. String  ===========
    public String get(String key) {
        CacheEntry<String> entry = (CacheEntry<String>) map.get(key);
        if (entry == null)
            return null;
        return entry.getValue();
    }

    public void set(String key, String value) {
        map.put(key, new CacheEntry<>(value));
    }

    /**
     * 批量删除，返回成功删除的个数
     *
     * @param keys
     * @return
     */
    public int del(String... keys) {
        return keys == null ? 0 : (int) Arrays.stream(keys).map(map::remove).filter(Objects::nonNull).count();
    }

    /**
     * 批量判断是否存在
     *
     * @param keys
     * @return
     */
    public int exists(String... keys) {
        return keys == null ? 0 : (int) Arrays.stream(keys).map(map::containsKey).filter(x -> x).count();
    }

    /**
     * 批量获取
     *
     * @param keys
     * @return
     */
    public String[] mget(String... keys) {
        return keys == null ? new String[0] : Arrays.stream(keys).map(this::get).toArray(String[]::new);
    }

    /**
     * 批量设置
     *
     * @param keys
     * @param values
     */
    public void mset(String[] keys, String[] values) {
        if (keys == null || keys.length == 0)
            return;
        for (int i = 0; i < keys.length; i++) {
            this.set(keys[i], values[i]);
        }
    }

    /**
     * 单笔自增
     *
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
     *
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

    public Integer strlen(String key) {
        return get(key) == null ? 0 : get(key).length();
    }

    // ===============  1. String end ===========

    // ===============  2. list  ===========

    public Integer lpush(String key, String... values) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) {
            entry = new CacheEntry<>(new LinkedList<>());
            this.map.put(key, entry);
        }
        LinkedList<String> exist = entry.getValue();
        Arrays.stream(values).forEach(exist::addFirst);
        return exist.size();
    }

    public String[] lpop(String key, int count) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null)
            return null;
        LinkedList<String> exist = entry.getValue();
        if (exist == null)
            return null;
        int len = Math.min(count, exist.size());
        String[] ret = new String[len];
        int index = 0;
        while (index < len) {
            ret[index++] = exist.removeFirst();
        }
        return ret;
    }

    public String[] rpop(String key, int count) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null)
            return null;
        LinkedList<String> exist = entry.getValue();
        if (exist == null)
            return null;
        int len = Math.min(count, exist.size());
        String[] ret = new String[len];
        int index = 0;
        while (index < len) {
            ret[index++] = exist.removeLast();
        }
        return ret;
    }

    public Integer rpush(String key, String... values) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) {
            entry = new CacheEntry<>(new LinkedList<>());
            this.map.put(key, entry);
        }
        LinkedList<String> exist = entry.getValue();
        Arrays.stream(values).forEach(exist::addLast);
        return exist.size();
    }

    public Integer llen(String key) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return 0;
        LinkedList<String> exist = entry.getValue();
        if (exist == null) return 0;
        return exist.size();
    }

    public String lindex(String key, int index) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return null;
        LinkedList<String> exist = entry.getValue();
        if (exist == null) return null;
        if (index >= exist.size()) return null;
        return exist.get(index);
    }

    public String[] lrange(String key, int start, int end) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return null;
        LinkedList<String> exist = entry.getValue();
        if (exist == null) return null;
        int size = exist.size();
        if (start >= size) return null;
        if (end >= size) end = size - 1;
        int len = Math.min(size, end - start + 1);
        String[] ret = new String[len];
        for (int i = 0; i < len; i++) {
            ret[i] = exist.get(start + i);
        }
        return ret;
    }

    // ===============  2. list end ===========

    // ===============  3. set  ===========

    public Integer sadd(String key, String[] vals) {
        CacheEntry<LinkedHashSet<String>> entry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (entry == null) {
            entry = new CacheEntry<>(new LinkedHashSet<>());
            this.map.put(key, entry);
        }
        LinkedHashSet<String> exist = entry.getValue();
        int before = exist.size();
        exist.addAll(Arrays.asList(vals));
        int after = exist.size();
        return after - before;
    }

    public String[] smembers(String key) {
        CacheEntry<LinkedHashSet<String>> entry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (entry == null) return null;
        LinkedHashSet<String> exist = entry.getValue();
        if (exist == null) return null;
        return exist.toArray(String[]::new);
    }

    public Integer srem(String key, String[] vals) {
        CacheEntry<LinkedHashSet<String>> entry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (entry == null) return 0;
        LinkedHashSet<String> exist = entry.getValue();
        if (exist == null) return null;
        return vals == null ? 0 : (int) Arrays.stream(vals).map(exist::remove).filter(x -> x).count();
    }

    public Integer scard(String key) {
        CacheEntry<?> entry = map.get(key);
        if (entry == null) return 0;
        LinkedHashSet<?> exist = (LinkedHashSet<?>) entry.getValue();
        if (exist == null) return 0;
        return exist.size();
    }

    Random random = new Random();

    public String[] spop(String key, int count) {
        CacheEntry<LinkedHashSet<String>> entry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (entry == null) return null;
        LinkedHashSet<String> exist = entry.getValue();
        if (exist == null) return null;
        int len = Math.min(count, exist.size());
        String[] ret = new String[len];
        int index = 0;
        while (index < len) {
            String[] array = exist.toArray(String[]::new);
            String obj = array[random.nextInt(exist.size())];
            exist.remove(obj);
            ret[index++] = obj;
        }
        return ret;
    }

    public Integer sismember(String key, String val) {
        CacheEntry<LinkedHashSet<String>> entry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (entry == null) return 0;
        LinkedHashSet<String> exist = entry.getValue();
        if (exist == null) return null;
        return exist.contains(val) ? 1 : 0;
    }

    // ===============  3. set end ===========

    // ===============  4. hash ===========

    public Integer hset(String key, String[] hkeys, String[] hvals) {
        if (hkeys == null || hkeys.length == 0) return 0;
        if (hvals == null || hvals.length == 0) return 0;
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (entry == null) {
            entry = new CacheEntry<>(new LinkedHashMap<>());
            this.map.put(key, entry);
        }
        LinkedHashMap<String, String> exist = entry.getValue();
        int before = exist.size();
        for (int i = 0; i < hkeys.length; i++) {
            exist.put(hkeys[i], hvals[i]);
        }
        int after = exist.size();
        return after - before;
    }

    public String hget(String key, String hkey) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (entry == null) return null;
        LinkedHashMap<String, String> exist = entry.getValue();
        if (exist == null) return null;
        return exist.get(hkey);
    }

    public String[] hgetall(String key) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (entry == null) return null;
        LinkedHashMap<String, String> exist = entry.getValue();
        if (exist == null) return null;
        return exist.entrySet().stream().flatMap(e -> Stream.of(e.getKey(), e.getValue())).toArray(String[]::new);
    }

    public Integer hlen(String key) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (entry == null) return 0;
        LinkedHashMap<String, String> exist = entry.getValue();
        if (exist == null) return 0;
        return exist.size();
    }

    public Integer hdel(String key, String[] hkeys) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (entry == null) return 0;
        LinkedHashMap<String, String> exist = entry.getValue();
        if (exist == null) return 0;
        return hkeys == null ? 0 : (int) Arrays.stream(hkeys).map(exist::remove).filter(Objects::nonNull).count();
    }

    public Integer hexists(String key, String hkey) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (entry == null) return 0;
        LinkedHashMap<String, String> exist = entry.getValue();
        if (exist == null) return 0;
        return exist.containsKey(hkey) ? 1 : 0;
    }

    public String[] hmget(String key, String[] hkeys) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (entry == null) return null;
        LinkedHashMap<String, String> exist = entry.getValue();
        if (exist == null) return null;
        return hkeys == null ? new String[0] : Arrays.stream(hkeys).map(exist::get).toArray(String[]::new);
    }

    // ===============  4. hash end ===========

    // ===============  5. zset end ===========

    public Integer zadd(String key, String[] values, double[] scores) {
        CacheEntry<LinkedHashSet<ZsetEntry>> entry = (CacheEntry<LinkedHashSet<ZsetEntry>>) map.get(key);
        if (entry == null) {
            entry = new CacheEntry<>(new LinkedHashSet<>());
            this.map.put(key, entry);
        }
        LinkedHashSet<ZsetEntry> exist = entry.getValue();
        int before = exist.size();
        for (int i = 0; i < values.length; i++) {
            String val = values[i];
            // 添加不成功，说明已经有重复的了
            if (!exist.add(new ZsetEntry(values[i], scores[i]))) {
                // 找出来重复的，删掉，然后添加进新值
                exist.removeIf(zsetEntry -> zsetEntry.getValue().equals(val));
                exist.add(new ZsetEntry(values[i], scores[i]));
            }
        }
        int after = exist.size();
        return after - before;
    }

    public Integer zcard(String key) {
        CacheEntry<?> entry = map.get(key);
        if(entry == null) return 0;
        LinkedHashSet<?> exist = (LinkedHashSet<?>) entry.getValue();
        if (exist == null) return 0;
        return exist.size();
    }

    public Double zscore(String key, String val) {
        CacheEntry<?> entry = map.get(key);
        if(entry == null) return null;
        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) entry.getValue();
        if (exist == null) return null;
        return exist.stream().filter(x -> x.getValue().equals(val)).map(ZsetEntry::getScore).findFirst().orElse(null);
    }

    public Integer zrem(String key, String[] vals) {
        CacheEntry<?> entry = map.get(key);
        if(entry == null) return null;
        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) entry.getValue();
        if (exist == null) return null;
        return vals==null ? 0 : (int) Arrays.stream(vals)
                .map(x -> exist.removeIf( y -> y.getValue().equals(x)))
                .filter(x -> x).count();
    }

    public Integer zrank(String key, String val) {
        CacheEntry<?> entry = map.get(key);
        if(entry == null) return null;
        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) entry.getValue();
        if (exist == null) return null;
        Double zscore = zscore(key, val);
        if(zscore == null) return null;
        return (int) exist.stream().filter(x -> x.getScore() < zscore).count();
    }

    public Integer zcount(String key, double min, double max) {
        CacheEntry<?> entry = map.get(key);
        if(entry == null) return 0;
        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) entry.getValue();
        if (exist == null) return 0;
        return (int)exist.stream().filter(x -> x.getScore() >= min && x.getScore() <= max).count();
    }


    // ===============  5. zset end ===========


    /**
     * 缓存对象
     *
     * @param <T>
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CacheEntry<T> {
        private T value;
    }

    /**
     * Zset数据类型对应的存储对象
     */
    @Data
    @AllArgsConstructor
    public static class ZsetEntry {
        private String value;
        private double score;

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ZsetEntry entry) {
                return entry.getValue().equals(value);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

    }
}
