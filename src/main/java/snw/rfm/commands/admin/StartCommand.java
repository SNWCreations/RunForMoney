package snw.rfm.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.events.GameStartEvent;
import snw.rfm.game.GameConfiguration;
import snw.rfm.game.GameProcess;
import snw.rfm.game.TeamHolder;
import snw.rfm.tasks.HunterReleaseTimer;

public final class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        RunForMoney rfm = RunForMoney.getInstance();
        if (rfm.getGameProcess() != null) {
            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "游戏已经开始。");
        } else {
            TeamHolder holder = TeamHolder.getInstance();
            if (holder.isNoHunterFound()) {
                sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "没有人在猎人队伍里，因此无法启动游戏。");
            } else if (holder.isNoRunnerFound()) {
                sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "没有人在逃走队员队伍里，因此无法启动游戏。");
            } else {
                GameConfiguration conf = GameConfiguration.getInstance();
                int lh = holder.getHunters().toArray().length;
                int lr = holder.getRunners().toArray().length;
                // 2022/1/30 修复游戏人数检查不严谨的错误
                if (conf.getLeastHunter() < lh) {
                    sender.sendMessage(ChatColor.RED + "猎人数量小于管理员设置的下限值。最少需要 " + lh + " 具。");
                } else if (conf.getLeastRunner() < lr) {
                    sender.sendMessage(ChatColor.RED + "逃走队员数量小于管理员设置的下限值。最少需要 " + lr + " 人。");
                } else {
                    Bukkit.getPluginManager().callEvent(new GameStartEvent());
                    GameProcess newProcess = new GameProcess();
                    newProcess.addTimer(new HunterReleaseTimer());
                    newProcess.start();
                    rfm.setGameProcess(newProcess);
                    sender.sendMessage(ChatColor.GREEN + "游戏已启动。");
                }
            }
        }
        return true;
    }
}
