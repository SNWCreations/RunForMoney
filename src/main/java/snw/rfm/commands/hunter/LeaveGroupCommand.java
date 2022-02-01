package snw.rfm.commands.hunter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import snw.rfm.game.TeamHolder;
import snw.rfm.group.Group;
import snw.rfm.group.GroupHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public final class LeaveGroupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        GroupHolder holder = GroupHolder.getInstance();
        if (args.length == 0) {
            if (sender instanceof Player) {
                if (!TeamHolder.getInstance().isHunter(((Player) sender))) {
                    sender.sendMessage(ChatColor.RED + "操作失败。你不是猎人，此命令对你没有作用。");
                } else {
                    Group willLeave = holder.findByPlayer((Player) sender);
                    if (willLeave == null) {
                        sender.sendMessage(ChatColor.RED + "操作失败。你不在任何组里。");
                    } else {
                        willLeave.remove((Player) sender);
                        sender.sendMessage(ChatColor.GREEN + "操作成功。");
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "参数不足！");
                return false;
            }
        } else {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "操作失败。批量操作仅管理员可以执行。");
            } else {
                ArrayList<String> failed = new ArrayList<>();
                for (String i : Arrays.copyOfRange(args, 1, args.length)) {
                    Player playerWillLeave = Bukkit.getPlayerExact(i);
                    if (playerWillLeave != null) {
                        if (holder.findByPlayer(playerWillLeave) != null) {
                            //noinspection ConstantConditions
                            holder.findByPlayer(playerWillLeave).remove(playerWillLeave);
                        } else {
                            sender.sendMessage(ChatColor.YELLOW + playerWillLeave.getName() + " 并不在任何组里。");
                        }
                    } else {
                        failed.add(i);
                    }
                }
                sender.sendMessage(ChatColor.GREEN + "" + (args.length - failed.toArray().length) + " 具猎人离开了其所组。");
                if (!(failed.isEmpty())) {
                    sender.sendMessage(ChatColor.RED + "其中，有 " + failed.toArray().length + " 个玩家因为不存在而移除失。");
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
                    sender.sendMessage(ChatColor.RED + "移除失败的有: " + builder);
                }
            }
        }
        return true;
    }
}
