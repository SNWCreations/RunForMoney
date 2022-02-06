/**
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public final class DeactivateHunterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "参数不足！");
            return false;
        } else {
            RunForMoney rfm = RunForMoney.getInstance();
            GameProcess process = rfm.getGameProcess();
            TeamHolder holder = TeamHolder.getInstance();
            if (process == null) {
                sender.sendMessage(ChatColor.RED + "操作失败。游戏未在运行。");
            } else {
                if (args.length == 1) {
                    Player hunterWillBeDisabled = Bukkit.getPlayerExact(args[0]);
                    if (hunterWillBeDisabled == null) {
                        sender.sendMessage(ChatColor.RED + "操作失败。玩家不在线。");
                    } else if (!holder.isHunter(hunterWillBeDisabled)) {
                        sender.sendMessage(ChatColor.RED + "操作失败。该玩家不是猎人。");
                    } else {
                        holder.removeEnabledHunter(hunterWillBeDisabled);
                        sender.sendMessage(ChatColor.GREEN + "操作成功。");
                    }
                } else {
                    HashSet<String> realArgs = new HashSet<>(Arrays.asList(args)); // 去重防止错误。
                    ArrayList<String> failed = new ArrayList<>();
                    for (String i : realArgs) {
                        Player hunterWillBeDisabled = Bukkit.getPlayerExact(i);
                        if (hunterWillBeDisabled == null || !holder.isHunter(hunterWillBeDisabled)) {
                            failed.add(i);
                        } else {
                            holder.removeEnabledHunter(hunterWillBeDisabled);
                        }
                    }
                    sender.sendMessage(ChatColor.GREEN + "有 " + (realArgs.toArray().length - failed.toArray().length) + " 具猎人被禁用。");
                    if (!failed.isEmpty()) {
                        StringBuilder stringBuilder = new StringBuilder();
                        Iterator<String> iterator = failed.iterator();
                        while (true) {
                            stringBuilder.append(iterator.next());
                            if (iterator.hasNext()) {
                                stringBuilder.append(", ");
                            } else {
                                break;
                            }
                        }
                        sender.sendMessage(ChatColor.RED + "其中，有 " + failed.toArray().length + " 个玩家因为不存在而禁用失败。");
                        sender.sendMessage(ChatColor.RED + "禁用失败的有: " + stringBuilder);
                    }
                }
            }
        }
        return true;
    }
}
