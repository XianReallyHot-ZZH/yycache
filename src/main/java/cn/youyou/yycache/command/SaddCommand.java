package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * Set类型数据结构：向集合添加一个或多个成员
 * SADD key member1 [member2]
 */
public class SaddCommand implements Command {
    @Override
    public String name() {
        return "SADD";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        String[] vals = getParamsNoKey(args);
        return Reply.integer(cache.sadd(key, vals));
    }
}
