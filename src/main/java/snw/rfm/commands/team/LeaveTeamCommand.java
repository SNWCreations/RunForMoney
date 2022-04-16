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
import snw.rfm.util.LanguageSupport;
import snw.rfm.util.PlaceHolderString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public final class LeaveTeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        TeamHolder holder = TeamHolder.getInstance(); // 2022/2/2 避免了重复获取。
        if (RunForMoney.getInstance().getGameProcess() != null) {
            sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("game.status.already_running"));
        } else {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    if (holder.isNotInGame(((Player) sender))) {
                        sender.sendMessage(ChatColor.RED + LanguageSupport.replacePlaceHolder("$commands.operation_failed$ $commands.team.leave.not_in_team$"));
                    } else {
                        holder.removeHunter((Player) sender);
                        holder.removeRunner((Player) sender);
                        sender.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.team.leave.success"));
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.player_required"));
                }
            } else {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + LanguageSupport.replacePlaceHolder("$commands.operation_failed$ $commands.batch.op_required$"));
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
                    sender.sendMessage(ChatColor.GREEN + new PlaceHolderString(LanguageSupport.getTranslation("commands.team.leave.success_count")).replaceArgument("count", realArgs.size() - failed.size()).toString());
                    if (!failed.isEmpty()) {
                        sender.sendMessage(ChatColor.RED + new PlaceHolderString(LanguageSupport.getTranslation("commands.batch.failed_not_exists")).replaceArgument("count", failed.size()).toString());
                        sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.batch.failed_list_header") + String.join(", ", failed)); // 2022/2/2 抄自己的代码结果没改。。。
                    }
                }
            }
        }
        return true;
    }
}
