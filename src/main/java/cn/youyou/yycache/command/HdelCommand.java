package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * Hash数据类型：删除一个或多个哈希表字段
 * HDEL key field1 [field2]
 */
public class HdelCommand implements Command {
    @Override
    public String name() {
        return "HDEL";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        String[] hkeys = getParamsNoKey(args);
        return Reply.integer(cache.hdel(key, hkeys));
    }
}
