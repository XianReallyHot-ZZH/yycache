package cn.youyou.yycache.core;

import cn.youyou.yycache.command.CommandCommand;

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
        register(new CommandCommand());

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
