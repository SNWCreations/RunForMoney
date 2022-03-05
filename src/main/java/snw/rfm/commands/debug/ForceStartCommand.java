/**
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.commands.debug;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.api.events.GamePostStartEvent;
import snw.rfm.api.events.GamePreStartEvent;
import snw.rfm.api.events.GameStartEvent;
import snw.rfm.config.GameConfiguration;
import snw.rfm.game.GameController;
import snw.rfm.game.GameProcess;
import snw.rfm.tasks.MainTimer;
import snw.rfm.tasks.HunterReleaseTimer;

public final class ForceStartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        RunForMoney rfm = RunForMoney.getInstance();
        if (rfm.getGameProcess() != null) {
            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "游戏已经开始。");
        } else {
            Bukkit.getPluginManager().callEvent(new GameStartEvent());
            Bukkit.getPluginManager().callEvent(new GamePreStartEvent());

            try {
                int time = (args.length > 0) ? Integer.parseInt(args[0]) : GameConfiguration.getReleaseTime();
                GameProcess newProcess = new GameProcess();
                GameController controller = new GameController(newProcess, GameConfiguration.getCoinPerSecond());
                if (time > 0) {
                    newProcess.setHunterReleaseTimer(new HunterReleaseTimer(time, newProcess));
                    newProcess.setHunterNoMoveTime(time);
                    Bukkit.broadcastMessage(ChatColor.RED + "游戏即将开始！");
                }
                newProcess.setMainTimer(new MainTimer(GameConfiguration.getGameTime() * 60, controller));
                newProcess.start();
                rfm.setGameProcess(newProcess);
                rfm.setGameController(controller);
                sender.sendMessage(ChatColor.GREEN + "游戏已启动。");

                Bukkit.getPluginManager().callEvent(new GamePostStartEvent());
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "操作失败。提供的倒计时值无效。");
                return false;
            }

        }
        return true;
    }
}
