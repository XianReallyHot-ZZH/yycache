package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * Hash数据类型：获取所有给定字段的值
 * HMGET key field1 [field2]
 */
public class HmgetCommand implements Command {
    @Override
    public String name() {
        return "HMGET";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        String[] hkeys = getParamsNoKey(args);
        return Reply.array(cache.hmget(key, hkeys));
    }
}
