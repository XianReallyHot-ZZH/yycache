package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * Hash数据类型：查看哈希表 key 中，指定的字段是否存在。
 * HEXISTS key field
 *
 */
public class HexistsCommand implements Command {
    @Override
    public String name() {
        return "HEXISTS";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        String hkey = getValue(args);
        return Reply.integer(cache.hexists(key, hkey));
    }
}
