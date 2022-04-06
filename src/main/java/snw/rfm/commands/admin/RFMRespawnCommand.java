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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snw.rfm.RunForMoney;
import snw.rfm.api.GameController;
import snw.rfm.game.TeamHolder;
import snw.rfm.util.LanguageSupport;
import snw.rfm.util.PlaceHolderString;

import java.util.*;

public final class RFMRespawnCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        GameController controller = RunForMoney.getInstance().getGameController();
        if (controller == null) {
            sender.sendMessage(ChatColor.RED + new PlaceHolderString("\\$commands.operation_failed\\$ \\$game.status.not_running\\$").replaceTranslate().toString());
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.not_enough_args"));
            return false;
        } else {
            Set<String> realArgs = new HashSet<>(Arrays.asList(args));
            List<String> failed = new ArrayList<>();
            for (String i : realArgs) {
                Player IT = Bukkit.getPlayerExact(i);
                if (IT == null || !controller.respawn(IT)) {
                    failed.add(i);
                }
            }
            sender.sendMessage(ChatColor.GREEN + new PlaceHolderString(LanguageSupport.getTranslation("commands.rfmrespawn.success_count")).replaceArgument("count", realArgs.toArray().length - failed.toArray().length).toString());
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
                sender.sendMessage(ChatColor.RED + new PlaceHolderString(LanguageSupport.getTranslation("commands.rfmrespawn.failed_count")).replaceArgument("count", failed.toArray().length).toString());
                sender.sendMessage(ChatColor.RED + LanguageSupport.getTranslation("commands.multioperate.failed_list_header") + f);
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!sender.isOp()) return null;
        List<String> result = new ArrayList<>();
        Bukkit.getOnlinePlayers().stream()
                .filter(IT -> !TeamHolder.getInstance().isHunter(IT))
                .filter(IT -> !TeamHolder.getInstance().isRunner(IT))
                .forEach(IT -> result.add(IT.getName()));
        return result;
    }
}
