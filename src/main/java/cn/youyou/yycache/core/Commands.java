package cn.youyou.yycache.core;

import cn.youyou.yycache.command.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 所有Command实现类的集合
 */
public class Commands {

    private static Map<String, Command> ALL = new LinkedHashMap<>();

    static {
        registryCommands();
    }

    /**
     * 注册所有Command实现类
     */
    private static void registryCommands() {
        // common commands
        register(new CommandCommand());
        register(new InfoCommand());
        register(new PingCommand());

        // string
        register(new SetCommand());
        register(new GetCommand());
        register(new StrlenCommand());
        register(new DelCommand());
        register(new ExistsCommand());
        register(new IncrCommand());
        register(new DecrCommand());
        register(new MsetCommand());
        register(new MgetCommand());

        // list

        // set

        // hash

        // zset

    }

    public static Command get(String commandName) {
        return ALL.get(commandName);
    }

    public static void register(Command command) {
        ALL.put(command.name(), command);
    }

    public static String[] getCommandNames() {
        return ALL.keySet().toArray(new String[0]);
    }

}
