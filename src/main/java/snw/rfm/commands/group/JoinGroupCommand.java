package snw.rfm.commands.group;

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
import java.util.HashSet;
import java.util.Iterator;

public final class JoinGroupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "参数不足！");
            return false;
        } else {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player player = ((Player) sender);
                    if (!TeamHolder.getInstance().isHunter(player)) {
                        sender.sendMessage(ChatColor.RED + "操作失败。你不是猎人，此命令对你无效。");
                    } else {
                        Group group = GroupHolder.getInstance().findByName(args[0]);
                        if (group == null) {
                            sender.sendMessage(ChatColor.RED + "操作失败。请求的组不存在。");
                        } else {
                            Group anotherGroup = GroupHolder.getInstance().findByPlayer(player);
                            if (anotherGroup != null) {
                                sender.sendMessage(ChatColor.RED + "操作失败。你已经是组 " + anotherGroup.getName() + " 的成员了，请先离开你现在所在的组。");
                            } else {
                                group.add(player);
                                sender.sendMessage(ChatColor.GREEN + "操作成功。");
                            }
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "你必须是个玩家。");
                }
            } else {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "操作失败。批量处理仅管理员可以执行。");
                } else {
                    Group group = GroupHolder.getInstance().findByName(args[0]);
                    if (group == null) {
                        sender.sendMessage(ChatColor.RED + "操作失败。请求的组不存在。");
                    } else {
                        HashSet<String> realArgs = new HashSet<>(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));
                        ArrayList<String> failed = new ArrayList<>();
                        for (String pstr : realArgs) {
                            Player player = Bukkit.getPlayerExact(pstr);
                            if (player == null) {
                                failed.add(pstr);
                            } else {
                                Group anotherGroup = GroupHolder.getInstance().findByPlayer(player);
                                if (anotherGroup != null) {
                                    anotherGroup.remove(player);
                                    player.sendMessage(ChatColor.RED + "因为管理员的操作，你从你所在的组离开了。");
                                } else {
                                    group.add(player);
                                }
                            }
                        }
                        sender.sendMessage(ChatColor.GREEN + "" + (realArgs.toArray().length - failed.toArray().length) + " 具猎人被增加进组 " + args[0] + " 。");
                        if (!failed.isEmpty()) {
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
        }
        return true;
    }
}
