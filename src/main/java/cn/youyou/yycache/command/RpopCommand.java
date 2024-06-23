package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * List数据类型：移除列表的最后一个元素，返回值为移除的元素。
 */
public class RpopCommand implements Command {
    @Override
    public String name() {
        return "RPOP";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        int count = 1;
        if (args.length > 6) {
            String value = getValue(args);
            count = Integer.parseInt(value);
            return Reply.array(cache.rpop(key, count));
        }
        String[] rpop = cache.rpop(key, count);
        return Reply.bulkString(rpop == null ? null : rpop[0]);
    }
}
