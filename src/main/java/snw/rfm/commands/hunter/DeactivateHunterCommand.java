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
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snw.rfm.RunForMoney;
import snw.rfm.game.GameProcess;
import snw.rfm.game.TeamHolder;
import snw.rfm.util.LanguageSupport;

import java.util.*;
import java.util.stream.Collectors;

public final class DeactivateHunterCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.not_enough_args"));
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
                    } else if (!holder.isHunterEnabled(hunterWillBeDisabled)) {
                        sender.sendMessage(ChatColor.RED + "操作失败。此猎人已经被禁用。");
                    } else {
                        holder.removeEnabledHunter(hunterWillBeDisabled);
                        sender.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.operation_success"));
                    }
                } else {
                    HashSet<String> realArgs = new HashSet<>(Arrays.asList(args)); // 去重防止错误。
                    ArrayList<String> failed = new ArrayList<>();
                    for (String i : realArgs) {
                        Player hunterWillBeDisabled = Bukkit.getPlayerExact(i);
                        if (hunterWillBeDisabled == null || !holder.isHunter(hunterWillBeDisabled) || !holder.isHunterEnabled(hunterWillBeDisabled)) {
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
                        sender.sendMessage(ChatColor.RED + "其中，有 " + failed.toArray().length + " 个玩家因为不存在、不是猎人或已经被禁用而禁用失败。");
                        sender.sendMessage(ChatColor.RED + "禁用失败的有: " + stringBuilder);
                    }
                }
            }
        }
        return true;
    }



    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return (args.length > 0) ? TeamHolder.getInstance().getHunters().stream().filter(IT -> !Arrays.asList(args).contains(IT)).collect(Collectors.toList()) : null;
    }
}
