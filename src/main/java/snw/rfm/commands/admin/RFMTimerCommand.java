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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class RFMTimerCommand implements CommandExecutor {
    private static final Set<String> seePlayers = new HashSet<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (seePlayers.contains(sender.getName())) {
                seePlayers.remove(sender.getName());
                sender.sendMessage(ChatColor.RED + "现在你不会在游戏时看到剩余时间。");
            } else {
                seePlayers.add(sender.getName());
                sender.sendMessage(ChatColor.GREEN + "现在你会在游戏时看到剩余时间。");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "你必须是个玩家。");
        }
        return true;
    }

    public static Set<String> getSeePlayers() {
        return seePlayers;
    }
}
