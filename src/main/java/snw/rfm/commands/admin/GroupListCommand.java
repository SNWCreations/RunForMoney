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

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import snw.rfm.group.Group;
import snw.rfm.group.GroupHolder;

import java.util.Iterator;

public final class GroupListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        GroupHolder holder = GroupHolder.getInstance(); // 2020/1/30 重构 GroupHolder
        int length = holder.toArray().length;
        if (length == 0) {
            sender.sendMessage(ChatColor.RED + "没有已存在的组。");
        } else {
            StringBuilder result = new StringBuilder("现有以下组: ");
            Iterator<Group> i = holder.iterator();
            while (true) {
                result.append(i.next().getName());
                if (i.hasNext()) {
                    result.append(", ");
                } else {
                    break;
                }
            }
            sender.sendMessage(ChatColor.GREEN + result.toString());
            sender.sendMessage(ChatColor.GREEN + "共有 " + length + " 个。");
        }
        return true;
    }
}
