package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * Hash数据类型：获取存储在哈希表中指定字段的值
 * HGET key field
 */
public class HgetCommand implements Command {
    @Override
    public String name() {
        return "HGET";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        String hkey = getValue(args);
        return Reply.bulkString(cache.hget(key, hkey));
    }
}
