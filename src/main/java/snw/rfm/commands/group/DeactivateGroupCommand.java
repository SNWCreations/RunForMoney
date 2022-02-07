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

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snw.rfm.RunForMoney;
import snw.rfm.group.Group;
import snw.rfm.group.GroupHolder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static snw.rfm.Util.getAllTheStringsStartingWithListInTheList;

public final class DeactivateGroupCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "参数不足！");
            return false;
        }
        if (RunForMoney.getInstance().getGameProcess() == null) {
            sender.sendMessage(ChatColor.RED + "操作失败。游戏未运行。");
        } else {
            if (args.length > 1) {
                for (String i : args) {
                    Group groupWillBeDeactivated = GroupHolder.getInstance().findByName(i);
                    if (groupWillBeDeactivated != null) {
                        groupWillBeDeactivated.deactivate();
                        sender.sendMessage(ChatColor.RED + "成功地禁用了组 " + groupWillBeDeactivated.getName() + " 。");
                    } else {
                        sender.sendMessage(ChatColor.RED + "禁用组 " + i + " 时失败。因为此组不存在。");
                    }
                }
            } else {
                Group group = GroupHolder.getInstance().findByName(args[0]);
                if (group == null) {
                    sender.sendMessage(ChatColor.RED + "操作失败。此组不存在。");
                } else {
                    group.deactivate(); // 2022/2/5 修复了只禁用一个组时进行了启用的误操作。
                    sender.sendMessage(ChatColor.GREEN + "操作成功。");
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return (args.length > 0) ? ((sender instanceof Player && sender.isOp()) ? getAllTheStringsStartingWithListInTheList(args[args.length - 1], GroupHolder.getInstance().getGroupNames().stream().filter(IT -> !Arrays.asList(args).contains(IT)).collect(Collectors.toList()), false) : null) : null;
    }
}
