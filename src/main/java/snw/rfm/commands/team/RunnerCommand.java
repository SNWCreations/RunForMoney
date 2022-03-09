/**
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.commands.team;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snw.rfm.RunForMoney;
import snw.rfm.game.TeamHolder;
import snw.rfm.group.Group;
import snw.rfm.group.GroupHolder;

import java.util.*;

public final class RunnerCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (RunForMoney.getInstance().getGameProcess() != null) {
            sender.sendMessage(ChatColor.RED + "游戏已经开始。");
        } else {
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
                                if (holder.isHunter(playerWillBeAdded)) {
                                    holder.removeHunter(playerWillBeAdded);
                                    playerWillBeAdded.sendMessage(ChatColor.GREEN + "检测到你在猎人队伍里，现已自动离开队伍。");
                                    Group g = GroupHolder.getInstance().findByPlayer(playerWillBeAdded);
                                    if (g != null) {
                                        g.remove(playerWillBeAdded);
                                        playerWillBeAdded.sendMessage(ChatColor.GREEN + "因为管理员的操作，你从你所在的组 " + g.getName() + " 离开了。");
                                    }
                                }
                                TeamHolder.getMainTeam().addEntry(sender.getName());
                                holder.addRunner(playerWillBeAdded);
                                playerWillBeAdded.sendMessage(ChatColor.GREEN + "因为管理员的操作，你现在是逃走队员！");
                            } else {
                                failed.add(i);
                            }
                        }
                        sender.sendMessage(ChatColor.GREEN + "" + (realArgs.toArray().length - failed.toArray().length) + " 个玩家成为逃走队员。");
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
                if (holder.isHunter(((Player) sender))) {
                    sender.sendMessage(ChatColor.GREEN + "检测到你在猎人队伍里，现已自动离开队伍。");
                    holder.removeHunter(((Player) sender));
                }
                Group g = GroupHolder.getInstance().findByPlayer((Player) sender);
                if (g != null) {
                    g.remove((Player) sender);
                    sender.sendMessage(ChatColor.GREEN + "检测到你曾在组 " + g.getName() +" ，因为逃走队员不能在组内，所以你从你所在的组离开了。");
                }
                TeamHolder.getMainTeam().addEntry(sender.getName());
                holder.addRunner(((Player) sender));
                sender.sendMessage(ChatColor.GREEN + "你现在是逃走队员！");
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
                    List<String> filtered = new ArrayList<>();
                    for (Player i : Bukkit.getOnlinePlayers()) {
                        if (!Arrays.asList(args).contains(i.getName())) {
                            filtered.add(i.getName());
                        }
                    }
                    return filtered;
                } else if (args.length == 1) {
                    return Collections.singletonList(sender.getName());
                }
            }
        }
        return null;
    }
}
