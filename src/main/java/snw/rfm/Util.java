package snw.rfm;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import java.util.logging.Level;

public class Util {
    public static void registerCommand(String cmdName, CommandExecutor executor) {
        RunForMoney rfm = RunForMoney.getInstance();
        PluginCommand cmd = Bukkit.getPluginCommand(cmdName);
        if (cmd == null) {
            rfm.getLogger().log(Level.SEVERE, "命令 " + cmdName + " 注册失败。插件无法加载。");
            Bukkit.getPluginManager().disablePlugin(rfm);
        } else {
            cmd.setExecutor(executor);
        }
    }

    public static void registerTabCompleter(String cmdName, TabCompleter completer) {
        RunForMoney rfm = RunForMoney.getInstance();
        PluginCommand cmd = Bukkit.getPluginCommand(cmdName);
        if (cmd == null) {
            rfm.getLogger().log(Level.SEVERE, "命令 " + cmdName + " 注册失败。插件无法加载。");
            Bukkit.getPluginManager().disablePlugin(rfm);
        } else {
            cmd.setTabCompleter(completer);
        }
    }
}
