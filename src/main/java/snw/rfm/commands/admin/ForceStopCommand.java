package snw.rfm.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.events.GameStopEvent;
import snw.rfm.events.GameStopType;
import snw.rfm.game.GameProcess;


public final class ForceStopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        GameProcess process = RunForMoney.getInstance().getGameProcess();
        if (process == null) {
            sender.sendMessage(ChatColor.RED + "操作失败。游戏未在运行。");
        } else {
            Bukkit.getPluginManager().callEvent(new GameStopEvent(GameStopType.ADMIN_TERMINATE_GAME));
            process.stop();
            Bukkit.broadcastMessage(ChatColor.RED + "游戏被管理员强制终止。");
        }
        return true;
    }
}
