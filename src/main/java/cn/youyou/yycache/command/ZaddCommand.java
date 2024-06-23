package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

import java.util.Arrays;

/**
 * Zset有序集合数据结构：向有序集合添加一个或多个成员，或者更新已存在成员的分数
 * ZADD key score1 member1 [score2 member2]
 *
 */
public class ZaddCommand implements Command {
    @Override
    public String name() {
        return "ZADD";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String key = getKey(args);
        String[] scores = getHkeys(args);
        String[] vals = getHvals(args);
        return Reply.integer(cache.zadd(key, vals, toDouble(scores)));
    }

    double[] toDouble(String[] scores) {
        return Arrays.stream(scores).mapToDouble(Double::parseDouble).toArray();
    }
}
