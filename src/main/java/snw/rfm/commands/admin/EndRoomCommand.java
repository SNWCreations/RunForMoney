/**
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.commands.admin;

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
import snw.rfm.config.GameConfiguration;
import snw.rfm.util.LanguageSupport;
import snw.rfm.util.PlaceHolderString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class EndRoomCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0 && sender instanceof Player) {
            GameConfiguration.setEndRoomLocation(((Player) sender).getLocation()); // 如果执行者是玩家，但没有提供参数，就把执行者所在位置设置为终止间
        } else if (args.length < 3) { // 参数不够改个毛线哦
            sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.not_enough_args"));
            return false;
        } else { // 如果提供了足够参数
            try {
                GameConfiguration.setEndRoomLocation(new Location(
                        (args.length == 4) ? Bukkit.getWorld(args[3]) : GameConfiguration.getGameWorld()
                        , Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])));
                // 2022/1/31 进行细节优化，如果执行者是玩家，终止间的位置将会位于执行者所在世界。
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + new PlaceHolderString("\\$commands.operation_failed\\$ \\$commands.invalid_argument\\$").replaceTranslate().toString());
                e.printStackTrace();
                return true;
            }
        }
        sender.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.operation_success"));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        Location loc = ((Player) sender).getLocation();
        switch (args.length) {
            case 1:
                return Collections.singletonList(String.valueOf(loc.getBlockX()));
            case 2:
                return Collections.singletonList(String.valueOf(loc.getBlockY()));
            case 3:
                return Collections.singletonList(String.valueOf(loc.getBlockZ()));
            case 4:
                List<String> worldNames = new ArrayList<>();
                Bukkit.getWorlds().forEach(IT -> worldNames.add(IT.getName()));
                return worldNames;
            default:
                return null;
        }
    }
}
