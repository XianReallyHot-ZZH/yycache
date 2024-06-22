package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

public class PingCommand implements Command {
    @Override
    public String name() {
        return "PING";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        String ret = "PONG";
        if(args.length >= 5) {
            ret = args[4];
        }
        return Reply.simpleString(ret);
    }
}
