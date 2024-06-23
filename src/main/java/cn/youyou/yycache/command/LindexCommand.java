package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * List数据类型：通过索引获取列表中的元素
 */
public class LindexCommand implements Command {
    @Override
    public String name() {
        return "LINDEX";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        int index = Integer.parseInt(getValue(args));
        return Reply.bulkString(cache.lindex(key, index));
    }
}
