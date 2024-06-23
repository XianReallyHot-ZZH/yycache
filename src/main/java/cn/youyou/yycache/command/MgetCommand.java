package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * String数据类型：批量获取指定key的值
 * MGET key1 [key2..]
 */
public class MgetCommand implements Command {
    @Override
    public String name() {
        return "MGET";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String[] key = getParams(args);
        return Reply.array(cache.mget(key));
    }
}
