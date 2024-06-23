package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

/**
 * List数据类型：移出并获取列表的第一个元素,支持获取多个元素
 */
public class LpopCommand implements Command {
    @Override
    public String name() {
        return "LPOP";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        int count = 1;
        if (args.length > 6) {
            String value = getValue(args);
            count = Integer.parseInt(value);
            return Reply.array(cache.lpop(key, count));
        }
        String[] lpop = cache.lpop(key, count);
        return Reply.bulkString(lpop == null ? null : lpop[0]);
    }
}
