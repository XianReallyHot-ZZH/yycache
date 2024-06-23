package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * Hash数据类型：获取在哈希表中指定 key 的所有字段和值
 * HGETALL key
 */
public class HgetallCommand implements Command {
    @Override
    public String name() {
        return "HGETALL";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        return Reply.array(cache.hgetall(key));
    }
}
