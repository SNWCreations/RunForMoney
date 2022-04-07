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
import snw.rfm.util.PlaceHolderString;

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
                sender.sendMessage(ChatColor.RED + LanguageSupport.replacePlaceHolder("$commands.operation_failed$ $game.status.not_running$"));
            } else {
                if (args.length == 1) {
                    Player hunterWillBeDisabled = Bukkit.getPlayerExact(args[0]);
                    if (hunterWillBeDisabled == null) {
                        sender.sendMessage(ChatColor.RED + LanguageSupport.replacePlaceHolder("$commands.operation_failed$ $commands.player_not_online$"));
                    } else if (!holder.isHunter(hunterWillBeDisabled)) {
                        sender.sendMessage(ChatColor.RED + LanguageSupport.replacePlaceHolder("$commands.operation_failed$ $commands.hunter.activate.not_hunter$"));
                    } else if (!holder.isHunterEnabled(hunterWillBeDisabled)) {
                        sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.hunter.activate.already_deactivated"));
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
                    sender.sendMessage(ChatColor.GREEN + new PlaceHolderString(LanguageSupport.getTranslation("commands.hunter.deactivate.success_count")).replaceArgument("count", realArgs.toArray().length - failed.toArray().length).toString());
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
                        sender.sendMessage(ChatColor.RED + new PlaceHolderString(LanguageSupport.getTranslation("commands.hunter.deactivate.failed_count")).replaceArgument("count", failed.toArray().length).toString());
                        sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.batch.failed_list_header") + stringBuilder);
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
