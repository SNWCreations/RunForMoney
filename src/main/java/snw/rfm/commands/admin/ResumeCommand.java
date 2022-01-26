package snw.rfm.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.game.GameProcess;
import snw.rfm.game.TeamHolder;

public final class ResumeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        RunForMoney rfm = RunForMoney.getInstance();
        GameProcess process = rfm.getGameProcess();
        TeamHolder holder = rfm.getTeamHolder();
        if (process != null) {
            if (holder.isNoHunterFound() && holder.isNoRunnerFound()) {
                sender.sendMessage(ChatColor.RED + "操作失败。因为两个队伍都无人在线。");
            } else {
                process.resume();
                sender.sendMessage(ChatColor.GREEN + "操作成功。");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "操作失败。游戏未在运行。");
        }
        return true;
    }
}
