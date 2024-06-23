package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * Set类型数据结构：返回集合中的所有成员
 * SMEMBERS key
 */
public class SmembersCommand implements Command {
    @Override
    public String name() {
        return "SMEMBERS";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        return Reply.array(cache.smembers(key));
    }
}
