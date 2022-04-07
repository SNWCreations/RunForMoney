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
import snw.rfm.Util;
import snw.rfm.game.TeamHolder;
import snw.rfm.group.Group;
import snw.rfm.group.GroupHolder;
import snw.rfm.util.LanguageSupport;
import snw.rfm.util.PlaceHolderString;

import java.util.*;
import java.util.stream.Collectors;

public final class JoinGroupCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.not_enough_args"));
            return false;
        } else {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player player = ((Player) sender);
                    if (!TeamHolder.getInstance().isHunter(player)) {
                        sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.group.not_hunter"));
                    } else {
                        Group group = GroupHolder.getInstance().findByName(args[0]);
                        if (group == null) {
                            sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.group.not_found"));
                        } else {
                            Group anotherGroup = GroupHolder.getInstance().findByPlayer(player);
                            if (anotherGroup != null) {
                                sender.sendMessage(ChatColor.RED + new PlaceHolderString(LanguageSupport.getTranslation("commands.group.join.already_in_a_group")).replaceArgument("groupName", anotherGroup.getName()).toString());
                            } else {
                                group.add(player.getName());
                                sender.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.operation_success"));
                            }
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.player_required"));
                }
            } else {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + LanguageSupport.replacePlaceHolder("$commands.operation_failed$ $commands.batch.op_required$"));
                } else {
                    Group group = GroupHolder.getInstance().findByName(args[0]);
                    if (group == null) {
                        sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.group.not_found"));
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
                                    anotherGroup.remove(player.getName());
                                    player.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.group.left_group_by_admin"));
                                }
                                group.add(player.getName()); // 2022/2/5 修复了只有在玩家已经在某个组内时才能正确是玩家加入组的错误。
                            }
                        }
                        sender.sendMessage(ChatColor.GREEN + new PlaceHolderString(LanguageSupport.getTranslation("commands.group.join.success_count")).replaceArgument("count", realArgs.toArray().length - failed.toArray().length).replaceArgument("groupName", group.getName()).toString());
                        if (!failed.isEmpty()) {
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
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length > 0) {
            if (args.length == 1) {
                return Util.getAllTheStringsStartingWithListInTheList(args[0], GroupHolder.getInstance().getGroupNames(), false);
            } else if (sender.isOp()) {
                return TeamHolder.getInstance().getHunters().stream()
                        .filter(IT -> !Arrays.asList(Arrays.copyOfRange(args, 1, args.length))
                                .contains(IT))
                        .collect(Collectors.toList());
            }
        }
        return null;
    }
}
