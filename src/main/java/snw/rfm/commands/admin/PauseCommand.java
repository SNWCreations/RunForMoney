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
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.api.GameController;
import snw.rfm.util.LanguageSupport;

public final class PauseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        GameController controller = RunForMoney.getInstance().getGameController();
        if (controller == null) {
            sender.sendMessage(ChatColor.RED + LanguageSupport.replacePlaceHolder("\\$commands.operation_failed\\$ \\$game.status.not_running\\$"));
        } else {
            if (controller.isPaused()) {
                sender.sendMessage(ChatColor.RED + LanguageSupport.replacePlaceHolder("\\$commands.operation_failed\\$ \\$game.status.already_paused\\$"));
            } else {
                controller.pause();
                sender.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.operation_success"));
            }
        }
        return true;
    }
}
