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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.game.GameProcess;
import snw.rfm.game.TeamHolder;

public final class ActivateHunterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(args.length == 1 || args.length == 4)) {
            sender.sendMessage(ChatColor.RED + "参数不足或过多！");
            return false;
        } else {
            RunForMoney rfm = RunForMoney.getInstance();
            GameProcess process = rfm.getGameProcess();
            TeamHolder holder = TeamHolder.getInstance();
            if (process == null) {
                sender.sendMessage(ChatColor.RED + "操作失败。游戏未在运行。");
            } else {
                Player hunterWillBeEnabled = Bukkit.getPlayerExact(args[0]);
                if (hunterWillBeEnabled == null) {
                    sender.sendMessage(ChatColor.RED + "操作失败。玩家不在线。");
                } else if (!holder.isHunter(hunterWillBeEnabled)) {
                    sender.sendMessage(ChatColor.RED + "操作失败。该玩家不是猎人。");
                } else {
                    if (args.length == 4) {
                        try {
                            hunterWillBeEnabled.teleport(new Location((sender instanceof Player) ? ((Player) sender).getWorld() : Bukkit.getWorld("world"), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]))); // 2022/1/31 同 EndRoomCommand。
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "操作失败。提供的位置有误（可能存在非数字，请确定是否为整数）。");
                            return true;
                        }
                    }
                    holder.addEnabledHunter(hunterWillBeEnabled);
                    sender.sendMessage(ChatColor.GREEN + "操作成功。");
                }
            }
        }
        return true;
    }
}
