package snw.rfm.commands.hunter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import snw.rfm.group.Group;
import snw.rfm.group.GroupHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public final class JoinGroupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "参数不足！");
            return false;
        } else {
            if (args.length == 2 && !(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "你必须是一个玩家。");
                return true;
            }
            GroupHolder holder = GroupHolder.getInstance();
            Group somebodyWillJoin = holder.findByName(args[0]);
            if (somebodyWillJoin == null) {
                sender.sendMessage(ChatColor.RED + "此组不存在。");
                return true;
            } else if (args.length == 1 && sender instanceof Player) {
                if (somebodyWillJoin.contains((Player) sender)) {
                    sender.sendMessage(ChatColor.RED + "你已经是该组的成员了。");
                } else {
                    ((Player) sender).performCommand("lg");
                    somebodyWillJoin.add((Player) sender);
                    sender.sendMessage(ChatColor.GREEN + "成功地加入了组 " + somebodyWillJoin.getName());
                }
            }

            if (args.length > 2) {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "操作失败。批量操作仅管理员可以执行。");
                } else {
                    ArrayList<String> failed = new ArrayList<>();
                    for (String i : Arrays.copyOfRange(args, 1, args.length)) {
                        Player playerWillJoin = Bukkit.getPlayerExact(i);
                        if (playerWillJoin != null) {
                            playerWillJoin.performCommand("lg");
                            somebodyWillJoin.add(playerWillJoin);
                        } else {
                            failed.add(i);
                        }
                    }
                    sender.sendMessage(ChatColor.GREEN + "" + (args.length - failed.toArray().length) + " 具猎人被增加进组 " + args[0]);
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
        }
        return true;
    }
}
