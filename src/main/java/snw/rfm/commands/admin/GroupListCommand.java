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
import snw.rfm.util.LanguageSupport;
import snw.rfm.util.PlaceHolderString;

import java.util.stream.Collectors;

public final class GroupListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        GroupHolder holder = GroupHolder.getInstance(); // 2020/1/30 重构 GroupHolder
        int length = holder.toArray().length;
        if (length == 0) {
            sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.grouplist.empty"));
        } else {
            sender.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.grouplist.header") + holder.stream().map(Group::getName).collect(Collectors.joining(", ")));
            sender.sendMessage(ChatColor.GREEN + new PlaceHolderString(LanguageSupport.getTranslation("commands.grouplist.count")).replaceArgument("count", length).toString());
        }
        return true;
    }
}
