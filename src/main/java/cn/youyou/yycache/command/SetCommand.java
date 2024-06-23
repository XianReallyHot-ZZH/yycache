package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * String数据类型：设置键值对
 * SET key value
 */
public class SetCommand implements Command {
    @Override
    public String name() {
        return "SET";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        cache.set(getKey(args), getValue(args));
        return Reply.simpleString(OK);
    }
}
