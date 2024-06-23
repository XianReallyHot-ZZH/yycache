package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * Zset有序集合数据结构：返回有序集中，成员的分数值
 * ZSCORE key member
 */
public class ZscoreCommand implements Command {
    @Override
    public String name() {
        return "ZSCORE";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        String val = getValue(args);
        Double zscore = cache.zscore(key, val);
        return Reply.simpleString(zscore == null ? null : zscore.toString());
    }
}
