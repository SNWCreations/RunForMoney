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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snw.rfm.RunForMoney;
import snw.rfm.game.TeamHolder;

import java.util.*;

public final class RFMRespawnCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (RunForMoney.getInstance().getGameProcess() == null) {
            sender.sendMessage(ChatColor.RED + "操作失败。游戏未在运行。");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "参数不足！");
            return false;
        } else {
            Set<String> realArgs = new HashSet<>(Arrays.asList(args));
            List<String> failed = new ArrayList<>();
            TeamHolder th = TeamHolder.getInstance();
            for (String i : realArgs) {
                Player IT = Bukkit.getPlayerExact(i);
                if (IT == null || th.isRunner(IT) || th.isHunter(IT)) {
                    failed.add(i);
                    continue;
                }
                IT.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "你已被复活", "", 20, 40, 10);
                IT.setGameMode(GameMode.ADVENTURE);
                th.getRunners().add(i);
                Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + i + " 已被复活。");
            }
            sender.sendMessage(ChatColor.GREEN + "有 " + (realArgs.toArray().length - failed.toArray().length) + " 个玩家被复活。");
            if (!failed.isEmpty()) {
                StringBuilder f = new StringBuilder();
                Iterator<String> fi = failed.iterator();
                while (true) {
                    f.append(fi.next());
                    if (fi.hasNext()) {
                        f.append(", ");
                    } else {
                        break;
                    }
                }
                sender.sendMessage(ChatColor.RED + "其中，有 " + failed.toArray().length + " 个玩家因为不存在或已在游戏中而操作失败。");
                sender.sendMessage(ChatColor.RED + "操作失败的有: " + f);
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!sender.isOp()) return null;
        List<String> result = new ArrayList<>();
        Bukkit.getOnlinePlayers().stream().filter(IT -> !TeamHolder.getInstance().isHunter(IT)).filter(IT -> !TeamHolder.getInstance().isRunner(IT)).forEach(IT -> result.add(IT.getName()));
        return result;
    }
}
