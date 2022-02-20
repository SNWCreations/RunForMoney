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
import snw.rfm.RunForMoney;
import snw.rfm.api.GameController;
import snw.rfm.game.GameProcess;
import snw.rfm.game.TeamHolder;

public final class ResumeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        RunForMoney rfm = RunForMoney.getInstance();
        GameProcess process = rfm.getGameProcess();
        TeamHolder holder = TeamHolder.getInstance();
        if (process != null) {
            if (holder.isNoHunterFound() || holder.isNoRunnerFound()) { // 2022/2/3 v1.1.3 只是一个逻辑判断，却带来了大Bug。
                sender.sendMessage(ChatColor.RED + "操作失败。因为两个队伍都无人在线。");
            } else {
                GameController gameController = RunForMoney.getInstance().getGameController();
                if (gameController.isPaused()) {
                    gameController.resume();
                    sender.sendMessage(ChatColor.GREEN + "操作成功。");
                } else {
                    sender.sendMessage(ChatColor.RED + "操作失败。游戏已在运行。");
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "操作失败。游戏未在运行。");
        }
        return true;
    }
}
