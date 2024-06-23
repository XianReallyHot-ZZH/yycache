package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * Hash数据类型：获取哈希表中字段的数量
 * HLEN key
 */
public class HlenCommand implements Command {
    @Override
    public String name() {
        return "HLEN";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        return Reply.integer(cache.hlen(key));
    }
}
