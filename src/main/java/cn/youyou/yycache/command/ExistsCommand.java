package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * String数据类型：判断某个key是否存在，支持多个key
 * 	EXISTS key [key ...]
 */
public class ExistsCommand implements Command {
    @Override
    public String name() {
        return "EXISTS";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String[] key = getParams(args);
        return Reply.integer(cache.exists(key));
    }
}
