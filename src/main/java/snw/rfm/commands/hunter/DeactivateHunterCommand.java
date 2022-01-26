package snw.rfm.commands.hunter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.game.GameProcess;
import snw.rfm.game.TeamHolder;

public final class DeactivateHunterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "参数不足或过多！");
            return false;
        } else {
            RunForMoney rfm = RunForMoney.getInstance();
            GameProcess process = rfm.getGameProcess();
            TeamHolder holder = rfm.getTeamHolder();
            if (process == null) {
                sender.sendMessage(ChatColor.RED + "操作失败。游戏未在运行。");
            } else {
                Player hunterWillBeDisabled = Bukkit.getPlayerExact(args[0]);
                if (hunterWillBeDisabled == null) {
                    sender.sendMessage(ChatColor.RED + "操作失败。玩家不在线。");
                } else if (!holder.isHunter(hunterWillBeDisabled)) {
                    sender.sendMessage(ChatColor.RED + "操作失败。该玩家不是猎人。");
                } else {
                    holder.removeEnabledHunter(hunterWillBeDisabled);
                    sender.sendMessage(ChatColor.GREEN + "操作成功。");
                }
            }
        }
        return true;
    }
}
