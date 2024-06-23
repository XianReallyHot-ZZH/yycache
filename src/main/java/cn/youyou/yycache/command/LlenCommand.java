package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * List数据类型：获取列表长度
 * LLEN key
 */
public class LlenCommand implements Command {
    @Override
    public String name() {
        return "LLEN";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        return Reply.integer(cache.llen(key));
    }
}
