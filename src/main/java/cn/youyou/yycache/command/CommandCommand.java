package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Commands;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

public class CommandCommand implements Command {
    @Override
    public String name() {
        return "COMMAND";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        return Reply.array(Commands.getCommandNames());
    }
}
