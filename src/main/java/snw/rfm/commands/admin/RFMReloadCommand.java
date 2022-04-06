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
import snw.rfm.ItemRegistry;
import snw.rfm.RunForMoney;
import snw.rfm.config.GameConfiguration;
import snw.rfm.config.Preset;
import snw.rfm.game.GameProcess;
import snw.rfm.processor.EventProcessor;
import snw.rfm.util.LanguageSupport;
import snw.rfm.util.NickSupport;

public final class RFMReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        RunForMoney.getInstance().reloadConfig();
        LanguageSupport.loadLanguage(RunForMoney.getInstance().getConfig().getString("language", "zh_CN"));
        GameConfiguration.init();
        Preset.init();
        NickSupport.init();
        ItemRegistry.unregisterItem("ep");
        ItemRegistry.unregisterItem("hpc");
        RunForMoney.getInstance().registerInternalItems();
        EventProcessor.init();
        GameProcess.init();
        sender.sendMessage(ChatColor.GREEN + "操作成功。");
        return true;
    }
}
