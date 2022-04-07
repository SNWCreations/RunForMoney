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
import snw.rfm.util.LanguageSupport;
import snw.rfm.util.PlaceHolderString;

import java.util.*;
import java.util.stream.Collectors;

public final class LeaveGroupCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        GroupHolder holder = GroupHolder.getInstance();
        if (args.length == 0) {
            if (sender instanceof Player) {
                if (!TeamHolder.getInstance().isHunter(sender.getName())) {
                    sender.sendMessage(ChatColor.RED + LanguageSupport.replacePlaceHolder("$commands.operation_failed$ $commands.group.not_hunter$"));
                } else {
                    Group willLeave = holder.findByPlayer((Player) sender);
                    if (willLeave == null) {
                        sender.sendMessage(ChatColor.RED + LanguageSupport.replacePlaceHolder("$commands.operation_failed$ $commands.group.leave.not_in_a_group.single$"));
                    } else {
                        willLeave.remove(sender.getName());
                        sender.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.operation_success"));
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.not_enough_args"));
                return false;
            }
        } else {
            if (!sender.isOp()) { // 防止熊孩子乱来hhhhc
                sender.sendMessage(ChatColor.RED + LanguageSupport.replacePlaceHolder("$commands.operation_failed$ $commands.batch.op_required$"));
            } else {
                ArrayList<String> failed = new ArrayList<>();
                HashSet<String> realArgs = new HashSet<>(Arrays.asList(args));
                for (String i : realArgs) {
                    Player playerWillLeave = Bukkit.getPlayerExact(i);
                    if (playerWillLeave != null) {
                        Group g = holder.findByPlayer(playerWillLeave); // 2022/2/2 规避 ConstantConditions 警告。
                        if (g != null) {
                            g.remove(playerWillLeave.getName());
                            playerWillLeave.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.group.left_group_by_admin"));
                        } else {
                            sender.sendMessage(ChatColor.YELLOW + new PlaceHolderString(LanguageSupport.getTranslation("commands.group.leave.not_in_a_group.multi")).replaceArgument("playerName", playerWillLeave.getName()).toString());
                        }
                    } else {
                        failed.add(i);
                    }
                }
                sender.sendMessage(ChatColor.GREEN + new PlaceHolderString(LanguageSupport.getTranslation("commands.group.leave.success_count")).replaceArgument("count", realArgs.toArray().length - failed.toArray().length).toString());
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
