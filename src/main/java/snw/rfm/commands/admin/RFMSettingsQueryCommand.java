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

public class RFMSettingsQueryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(ChatColor.GREEN + "猎人默认释放时间 (即使用 /start 命令且不提供释放时间时的默认值, 秒为单位): " + GameConfiguration.getReleaseTime());
        Location erl = GameConfiguration.getEndRoomLocation();
        sender.sendMessage(ChatColor.GREEN + "终止间位置: " + (erl != null ? erl.getBlockX() + " " + erl.getBlockY() + " " + erl.getBlockZ() : "暂未设置"));
        sender.sendMessage(ChatColor.GREEN + "游戏时间 (分钟为单位): " + GameConfiguration.getGameTime());
        sender.sendMessage(ChatColor.GREEN + "每秒增加的 B币 数量: " + GameConfiguration.getCoinPerSecond());
        return true;
    }
}
