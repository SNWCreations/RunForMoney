package snw.rfm.commands.team;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.game.TeamHolder;
import snw.rfm.group.Group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public final class LeaveTeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "你必须是一个玩家！");
            } else {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "操作失败。批量操作仅管理员可以执行。");
                    return true;
                }
                ArrayList<String> failed = new ArrayList<>();
                for (String i : Arrays.copyOfRange(args, 1, args.length)) {
                    Player playerWillBeRemoved = Bukkit.getPlayerExact(i);
                    if (playerWillBeRemoved != null) {
                        for (Group g : RunForMoney.getInstance().getGroups()) {
                            g.remove(playerWillBeRemoved);
                        }
                        TeamHolder holder = RunForMoney.getInstance().getTeamHolder();
                        holder.removeHunter(playerWillBeRemoved);
                        holder.removeRunner(playerWillBeRemoved);
                    } else {
                        failed.add(i);
                    }
                }
                sender.sendMessage(ChatColor.GREEN + "" + (args.length - failed.toArray().length) + " 个玩家被移除其所在队伍。");
                if (!(failed.isEmpty())) {
                    sender.sendMessage(ChatColor.RED + "其中，有 " + failed.toArray().length + " 个玩家因为不存在而移除失败。");
                    StringBuilder builder = new StringBuilder();
                    Iterator<String> fi = failed.iterator();
                    while (true) {
                        builder.append(fi.next());
                        if (fi.hasNext()) {
                            builder.append(", ");
                        } else {
                            break;
                        }
                    }
                    sender.sendMessage(ChatColor.RED + "添加失败的有: " + builder);
                }
            }
        } else {
            if (RunForMoney.getInstance().getGameProcess() != null) {
                sender.sendMessage(ChatColor.RED + "操作失败。游戏已经开始。");
            } else {
                TeamHolder holder = RunForMoney.getInstance().getTeamHolder();
                if (holder.isHunter((Player) sender) || holder.isRunner((Player) sender)) {
                    sender.sendMessage(ChatColor.RED + "操作失败。你不在任何组里。");
                } else {
                    holder.removeHunter((Player) sender);
                    holder.removeRunner((Player) sender);
                    sender.sendMessage(ChatColor.GREEN + "你离开了你所在的队伍，这意味着当游戏开始时你将会成为旁观者。");
                }
            }
        }
        return true;
    }
}
