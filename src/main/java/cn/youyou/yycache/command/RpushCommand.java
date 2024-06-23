package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * List数据类型：在列表中添加一个或多个值到列表尾部
 */
public class RpushCommand implements Command {
    @Override
    public String name() {
        return "RPUSH";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        String[] values = getParamsNoKey(args);
        return Reply.integer(cache.rpush(key, values));
    }
}
