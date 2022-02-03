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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;


public final class HunterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        TeamHolder holder = TeamHolder.getInstance();
        if (!(sender instanceof Player)) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "参数不足！");
                return false;
            } else {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "操作失败。批量操作仅管理员可以执行。");
                } else {
                    ArrayList<String> failed = new ArrayList<>();
                    HashSet<String> realArgs = new HashSet<>(Arrays.asList(args));
                    for (String i : realArgs) {
                        Player playerWillBeAdded = Bukkit.getPlayerExact(i);
                        if (playerWillBeAdded != null) {
                            if (holder.isRunner(playerWillBeAdded)) {
                                holder.removeRunner(playerWillBeAdded);
                                playerWillBeAdded.sendMessage(ChatColor.GREEN + "检测到你在逃走队员队伍里，现已自动离开队伍。");
                            }
                            holder.addHunter(playerWillBeAdded);
                            playerWillBeAdded.sendMessage(ChatColor.GREEN + "因为管理员的操作，你现在是猎人！");
                        } else {
                            failed.add(i);
                        }
                    }
                    sender.sendMessage(ChatColor.GREEN + "" + (realArgs.toArray().length - failed.toArray().length) + " 个玩家成为猎人。");
                    if (!(failed.isEmpty())) {
                        sender.sendMessage(ChatColor.RED + "其中，有 " + failed.toArray().length + " 个玩家因为不存在而添加失败。");
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
            }
        } else {
            if (RunForMoney.getInstance().getGameProcess() != null) {
                sender.sendMessage(ChatColor.RED + "游戏已经开始。");
            } else {
                if (holder.isRunner(((Player) sender))) {
                    holder.removeRunner(((Player) sender));
                    sender.sendMessage(ChatColor.GREEN + "检测到你在逃走队员队伍里，现已自动离开队伍。");
                }
                holder.addHunter(((Player) sender));
                sender.sendMessage(ChatColor.GREEN + "你现在是猎人！");
            }
        }
        return true;
    }
}
