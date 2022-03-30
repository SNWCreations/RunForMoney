/**
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.commands.group;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snw.rfm.game.TeamHolder;
import snw.rfm.group.Group;
import snw.rfm.group.GroupHolder;

import java.util.*;
import java.util.stream.Collectors;

public final class LeaveGroupCommand implements CommandExecutor, TabCompleter {
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
                        willLeave.remove(sender.getName());
                        sender.sendMessage(ChatColor.GREEN + "操作成功。");
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "参数不足！");
                return false;
            }
        } else {
            if (!sender.isOp()) { // 防止熊孩子乱来hhhhc
                sender.sendMessage(ChatColor.RED + "操作失败。批量操作仅管理员可以执行。");
            } else {
                ArrayList<String> failed = new ArrayList<>();
                HashSet<String> realArgs = new HashSet<>(Arrays.asList(args));
                for (String i : realArgs) {
                    Player playerWillLeave = Bukkit.getPlayerExact(i);
                    if (playerWillLeave != null) {
                        Group g = holder.findByPlayer(playerWillLeave); // 2022/2/2 规避 ConstantConditions 警告。
                        if (g != null) {
                            g.remove(playerWillLeave.getName());
                            playerWillLeave.sendMessage(ChatColor.RED + "因为管理员的操作，你从你所在的组离开了。");
                        } else {
                            sender.sendMessage(ChatColor.YELLOW + playerWillLeave.getName() + " 并不在任何组里。");
                        }
                    } else {
                        failed.add(i);
                    }
                }
                sender.sendMessage(ChatColor.GREEN + "" + (realArgs.toArray().length - failed.toArray().length) + " 具猎人离开了其所在组。");
                if (!failed.isEmpty()) {
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
                    sender.sendMessage(ChatColor.RED + "移除失败的有: " + builder);
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                if (sender.isOp()) {
                    return TeamHolder.getInstance().getHunters().stream()
                            .filter(IT -> !Arrays.asList(args).contains(IT))
                            .collect(Collectors.toList());
                } else if (args.length == 1) {
                    return Collections.singletonList(sender.getName());
                }
            }
        }
        return null;
    }
}
