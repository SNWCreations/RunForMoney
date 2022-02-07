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
import snw.rfm.events.GameStartEvent;
import snw.rfm.game.GameProcess;
import snw.rfm.tasks.HunterReleaseTimer;

public final class ForceStartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        RunForMoney rfm = RunForMoney.getInstance();
        if (rfm.getGameProcess() != null) {
            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "游戏已经开始。");
        } else {
            Bukkit.getPluginManager().callEvent(new GameStartEvent()); // 事件系统的事
            GameProcess newProcess = new GameProcess(); // 新建实例
            newProcess.addTimer(new HunterReleaseTimer()); // 添加猎人释放倒计时
            rfm.setGameProcess(newProcess);
            newProcess.start();
            sender.sendMessage(ChatColor.GREEN + "游戏已启动。");
        }
        return true;
    }
}
