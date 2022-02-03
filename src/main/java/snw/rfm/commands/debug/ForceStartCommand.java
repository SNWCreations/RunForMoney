package snw.rfm.commands.debug;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.events.GameStartEvent;
import snw.rfm.game.GameProcess;
import snw.rfm.tasks.HunterReleaseTimer;

public final class ForceStartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        RunForMoney rfm = RunForMoney.getInstance();
        if (rfm.getGameProcess() != null) {
            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "游戏已经开始。");
        } else {
            Bukkit.getPluginManager().callEvent(new GameStartEvent());
            GameProcess newProcess = new GameProcess();
            newProcess.addTimer(new HunterReleaseTimer());
            rfm.setGameProcess(newProcess);
            newProcess.start();
            sender.sendMessage(ChatColor.GREEN + "游戏已启动。");
        }
        return true;
    }
}
