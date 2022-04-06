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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.game.TeamHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public final class LeaveTeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        TeamHolder holder = TeamHolder.getInstance(); // 2022/2/2 避免了重复获取。
        if (RunForMoney.getInstance().getGameProcess() != null) {
            sender.sendMessage(ChatColor.RED + "游戏已经开始。");
        } else {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    if (!holder.isHunter((Player) sender) && !holder.isRunner((Player) sender)) {
                        sender.sendMessage(ChatColor.RED + "操作失败。你不在任何队伍里。");
                    } else {
                        holder.removeHunter((Player) sender);
                        holder.removeRunner((Player) sender);
                        sender.sendMessage(ChatColor.GREEN + "你离开了你所在的队伍，这意味着当游戏开始时你将会成为旁观者。");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "你必须是一个玩家！");
                }
            } else {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "操作失败。批量操作仅管理员可以执行。");
                } else {
                    ArrayList<String> failed = new ArrayList<>();
                    HashSet<String> realArgs = new HashSet<>(Arrays.asList(args));
                    for (String i : realArgs) {
                        Player playerWillBeRemoved = Bukkit.getPlayerExact(i);
                        if (playerWillBeRemoved != null) {
                            // 2022/2/2 移除不必要的代码
                            holder.removeHunter(playerWillBeRemoved);
                            holder.removeRunner(playerWillBeRemoved);
                        } else {
                            failed.add(i);
                        }
                    }
                    sender.sendMessage(ChatColor.GREEN + "" + (realArgs.toArray().length - failed.toArray().length) + " 个玩家被移出其所在队伍。");
                    if (!(failed.isEmpty())) {
                        sender.sendMessage(ChatColor.RED + "其中，有 " + failed.toArray().length + " 个玩家因为不存在而操作失败。");
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
                        sender.sendMessage(ChatColor.RED + "操作失败的有: " + builder); // 2022/2/2 抄自己的代码结果没改。。。
                    }
                }
            }
        }
        return true;
    }
}
