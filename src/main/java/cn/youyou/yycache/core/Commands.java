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
        register(new LpushCommand());
        register(new LpopCommand());
        register(new RpopCommand());
        register(new RpushCommand());
        register(new LlenCommand());
        register(new LindexCommand());
        register(new LrangeCommand());

        // set
//        register(new SaddCommand());
//        register(new SmembersCommand());
//        register(new SremCommand());
//        register(new ScardCommand());
//        register(new SpopCommand());
//        register(new SismemberCommand());

        // hash
//        register(new HsetCommand());
//        register(new HgetCommand());
//        register(new HgetallCommand());
//        register(new HlenCommand());
//        register(new HdelCommand());
//        register(new HexistsCommand());
//        register(new HmgetCommand());

        // zset
//        register(new ZaddCommand());
//        register(new ZcardCommand());
//        register(new ZscoreCommand());
//        register(new ZremCommand());
//        register(new ZrankCommand());
//        register(new ZcountCommand());

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
