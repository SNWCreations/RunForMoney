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
import snw.rfm.util.LanguageSupport;
import snw.rfm.util.PlaceHolderString;

import java.util.*;


public final class HunterCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (RunForMoney.getInstance().getGameProcess() != null) {
            sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("game.status.already_running"));
        } else {
            TeamHolder holder = TeamHolder.getInstance();
            if (args.length == 0) {
                if (sender instanceof Player) {
                    holder.addHunter((Player) sender);
                    sender.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.team.hunter.success"));
                } else {
                    sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.not_enough_args"));
                    return false;
                }
            } else {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + LanguageSupport.replacePlaceHolder("$commands.operation_failed$ $commands.batch.op_required$"));
                } else {
                    ArrayList<String> failed = new ArrayList<>();
                    HashSet<String> realArgs = new HashSet<>(Arrays.asList(args));
                    for (String i : realArgs) {
                        Player playerWillBeAdded = Bukkit.getPlayerExact(i);
                        if (playerWillBeAdded != null) {
                            holder.addHunter(playerWillBeAdded);
                            playerWillBeAdded.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.team.hunter.success_admin"));
                        } else {
                            failed.add(i);
                        }
                    }
                    sender.sendMessage(ChatColor.GREEN + new PlaceHolderString(LanguageSupport.getTranslation("commands.team.hunter.success_count")).replaceArgument("count", realArgs.toArray().length - failed.toArray().length).toString());
                    if (!(failed.isEmpty())) {
                        sender.sendMessage(ChatColor.RED + new PlaceHolderString(LanguageSupport.getTranslation("commands.batch.failed_not_exists")).replaceArgument("count", failed.toArray().length).toString());
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
                        sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.batch.failed_list_header") + builder);
                    }
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
