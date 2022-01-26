package snw.rfm.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.events.GameStartEvent;
import snw.rfm.game.GameProcess;
import snw.rfm.game.TeamHolder;
import snw.rfm.timers.HunterReleaseTimer;

public final class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        RunForMoney rfm = RunForMoney.getInstance();
        if (rfm.getGameProcess() != null) {
            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "游戏已经开始。");
        } else {
            TeamHolder holder = rfm.getTeamHolder();
            if (holder.isNoHunterFound() && holder.isNoRunnerFound()) {
                sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "没有人在队伍里，因此无法启动游戏。");
            } else {
                Bukkit.getPluginManager().callEvent(new GameStartEvent());
                GameProcess newProcess = new GameProcess();
                newProcess.addTimer(new HunterReleaseTimer());
                rfm.setGameProcess(newProcess);
                newProcess.start();
                sender.sendMessage(ChatColor.GREEN + "游戏已启动。");
            }
        }
        return true;
    }
}
