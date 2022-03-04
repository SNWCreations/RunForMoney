/**
 * This file is part of RunForMoney.
 * <p>
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * <p>
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.api.events.GameStartEvent;
import snw.rfm.config.GameConfiguration;
import snw.rfm.game.GameController;
import snw.rfm.game.GameProcess;
import snw.rfm.game.TeamHolder;
import snw.rfm.tasks.CoinTimer;
import snw.rfm.tasks.HunterReleaseTimer;

public final class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        RunForMoney rfm = RunForMoney.getInstance();
        if (rfm.getGameProcess() != null) {
            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "游戏已经开始。");
        } else {
            TeamHolder holder = TeamHolder.getInstance();
            if (holder.isNoHunterFound()) {
                sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "没有人在猎人队伍里，因此无法启动游戏。");
            } else if (holder.isNoRunnerFound()) {
                sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "没有人在逃走队员队伍里，因此无法启动游戏。");
            } else {
                int lh = holder.getHunters().toArray().length;
                int lr = holder.getRunners().toArray().length;
                // 2022/1/30 修复游戏人数检查不严谨的错误
                if (GameConfiguration.getLeastHunter() < lh) {
                    sender.sendMessage(ChatColor.RED + "猎人数量小于管理员设置的下限值。最少需要 " + lh + " 具。");
                } else if (GameConfiguration.getLeastRunner() < lr) {
                    sender.sendMessage(ChatColor.RED + "逃走队员数量小于管理员设置的下限值。最少需要 " + lr + " 人。");
                } else {
                    Bukkit.getPluginManager().callEvent(new GameStartEvent());

                    try {
                        int time = (args.length > 0) ? Integer.parseInt(args[0]) : GameConfiguration.getReleaseTime();
                        GameProcess newProcess = new GameProcess();
                        if (time > 0) {
                            newProcess.addTimer(new HunterReleaseTimer(time));
                            newProcess.setHunterNoMoveTime(time);
                            Bukkit.broadcastMessage(ChatColor.RED + "游戏即将开始！");
                        } else {
                            newProcess.addTimer(new CoinTimer(GameConfiguration.getGameTime() * 60, GameConfiguration.getCoinPerSecond()));
                        }
                        GameController controller = new GameController(newProcess);
                        newProcess.start();
                        rfm.setGameProcess(newProcess);
                        rfm.setGameController(controller);
                        sender.sendMessage(ChatColor.GREEN + "游戏已启动。");
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "操作失败。提供的倒计时值无效。");
                        return false;
                    }

                }
            }
        }
        return true;
    }
}
