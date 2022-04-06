/*
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import snw.rfm.config.GameConfiguration;
import snw.rfm.util.LanguageSupport;
import snw.rfm.util.PlaceHolderString;

public class RFMSettingsQueryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.rsq.hunter_release_time_default") + GameConfiguration.getReleaseTime());
        Location erl = GameConfiguration.getEndRoomLocation();
        //noinspection ConstantConditions
        sender.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.rsq.endroom_location") +
                (erl != null ?
                        new PlaceHolderString(LanguageSupport.getTranslation("commands.rsq.endroom_part2")).replaceArgument("x", erl.getBlockX()).replaceArgument("y", erl.getBlockY()).replaceArgument("z", erl.getBlockZ()).replaceArgument("worldName", erl.getWorld().getName()).toString()
                        : LanguageSupport.getTranslation("commands.rsq.endroom_not_set")));
        sender.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.rsq.game_time") + GameConfiguration.getGameTime());
        sender.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.rsq.coin_per_second") + GameConfiguration.getCoinPerSecond());
        sender.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.rsq.multiplier") + GameConfiguration.getCoinMultiplierOnBeCatched());
        return true;
    }
}
