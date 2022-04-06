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
import snw.rfm.game.TeamHolder;
import snw.rfm.util.LanguageSupport;

import java.util.Iterator;
import java.util.Set;

public final class TeamListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        TeamHolder holder = TeamHolder.getInstance();
        Set<String> hl = holder.getHunters();
        Set<String> rl = holder.getRunners();

        if (hl.isEmpty()) {
            sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.teamlist.no_hunter"));
        } else {
            StringBuilder hunters = new StringBuilder();
            Iterator<String> h = hl.iterator();
            while (true) {
                hunters.append(h.next());
                if (h.hasNext()) {
                    hunters.append(", ");
                } else {
                    break;
                }
            }
            sender.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.teamlist.hunter_header") + hunters);
        }

        if (rl.isEmpty()) {
            sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.teamlist.no_runner"));
        } else {
            StringBuilder runners = new StringBuilder();
            Iterator<String> r = rl.iterator();
            while (true) {
                runners.append(r.next());
                if (r.hasNext()) {
                    runners.append(", ");
                } else {
                    break;
                }
            }
            sender.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.teamlist.runner_header") + runners);
        }

        return true;
    }
}
