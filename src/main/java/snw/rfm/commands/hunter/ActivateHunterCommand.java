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
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snw.rfm.RunForMoney;
import snw.rfm.config.GameConfiguration;
import snw.rfm.game.GameProcess;
import snw.rfm.game.TeamHolder;
import snw.rfm.util.LanguageSupport;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ActivateHunterCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(args.length == 1 || args.length == 4)) {
            sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.not_enough_or_too_many_args"));
            return false;
        } else {
            RunForMoney rfm = RunForMoney.getInstance();
            GameProcess process = rfm.getGameProcess();
            TeamHolder holder = TeamHolder.getInstance();
            if (process == null) {
                sender.sendMessage(ChatColor.RED + LanguageSupport.replacePlaceHolder("$commands.operation_failed$ $game.status.not_running$"));
            } else {
                Player hunterWillBeEnabled = Bukkit.getPlayerExact(args[0]);
                if (hunterWillBeEnabled == null) {
                    sender.sendMessage(ChatColor.RED + LanguageSupport.replacePlaceHolder("$commands.operation_failed$ $commands.player_not_online$"));
                } else if (!holder.isHunter(hunterWillBeEnabled)) {
                    sender.sendMessage(ChatColor.RED + LanguageSupport.replacePlaceHolder("$commands.operation_failed$ $commands.hunter.activate.not_hunter$"));
                } else if (holder.isHunterEnabled(hunterWillBeEnabled)) {
                    sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.hunter.activate.already_activated"));
                } else {
                    if (args.length == 4) {
                        try {
                            hunterWillBeEnabled.teleport(new Location((sender instanceof Player) ? ((Player) sender).getWorld() : GameConfiguration.getGameWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]))); // 2022/1/31 同 EndRoomCommand。
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + LanguageSupport.replacePlaceHolder("$commands.operation_failed$ $commands.invalid_argument$"));
                            return true;
                        }
                    }
                    holder.addEnabledHunter(hunterWillBeEnabled);
                    sender.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.operation_success"));
                }
            }
        }
        return true;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return (args.length == 1) ? TeamHolder.getInstance().getHunters().stream()
                .filter(IT -> !Arrays.asList(args).contains(IT))
                .collect(Collectors.toList()) : null;
    }
}
