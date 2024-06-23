package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * Zset有序集合数据结构：计算在有序集合中指定区间分数的成员数
 * ZCOUNT key min max
 */
public class ZcountCommand implements Command {
    @Override
    public String name() {
        return "ZCOUNT";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        double min = Double.parseDouble(getValue(args));
        double max = Double.parseDouble(args[8]);
        return Reply.integer(cache.zcount(key, min, max));
    }
}
