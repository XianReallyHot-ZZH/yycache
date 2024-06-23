package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * Zset有序集合数据结构：返回有序集合中指定成员的索引
 * ZRANK key member
 */
public class ZrankCommand implements Command {
    @Override
    public String name() {
        return "ZRANK";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        String val = getValue(args);
        return Reply.integer(cache.zrank(key, val));
    }
}
