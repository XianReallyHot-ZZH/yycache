package cn.youyou.yycache.command;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Reply;
import cn.youyou.yycache.core.YYCache;

public class InfoCommand implements Command {

    // redis协议中响应服务器信息
    private static final String SERVER_INFO = "YYCache Server[v1.0.1], created by XianReallyHot-ZZH." + CRLF
            + "Mock Redis Server, at 2024-06-22 in HangZhou." + CRLF;


    @Override
    public String name() {
        return "INFO";
    }

    @Override
    public Reply<?> execute(YYCache cache, String[] args) {
        return Reply.bulkString(SERVER_INFO);
    }
}
