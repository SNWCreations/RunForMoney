/**
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static snw.rfm.Util.sortDescend;

public final class CoinListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Map<String, Double> coinEarned = sortDescend(RunForMoney.getInstance().getCoinEarned());
        if (coinEarned.size() == 0) {
            sender.sendMessage(ChatColor.RED + "B币榜为空！");
        } else {
            sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "============ B币榜 ============");
            AtomicInteger a = new AtomicInteger();
            coinEarned.forEach((key, value) -> sender.sendMessage(ChatColor.GREEN + "" + a.addAndGet(1) + ". " + key + ": " + value));
        }
        return true;
    }

}
