package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * Hash数据类型：将哈希表 key 中的字段 field 的值设为 value
 * HSET key field value
 */
public class HsetCommand implements Command {
    @Override
    public String name() {
        return "HSET";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        String[] hkeys = getHkeys(args);
        String[] hvals = getHvals(args);
        return Reply.integer(cache.hset(key, hkeys, hvals));
    }
}
