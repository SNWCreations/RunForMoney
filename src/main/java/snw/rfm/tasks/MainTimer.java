/**
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.tasks;

import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;
import snw.rfm.RunForMoney;
import snw.rfm.api.events.GameStopEvent;
import snw.rfm.commands.admin.RFMTimerCommand;
import snw.rfm.game.GameController;
import snw.rfm.game.TeamHolder;
import snw.rfm.util.SendingActionBarMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class MainTimer extends BaseCountDownTimer {
    private final GameController controller;
    private final List<ScheduledRFMTaskImpl> tasks = new ArrayList<>();

    public MainTimer(int secs, GameController controller) {
        super(secs);
        Validate.notNull(controller);
        this.controller = controller;
    }

    @Override
    public void start(Plugin plugin) {
        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "游戏开始");
        super.start(plugin);
    }

    @Override
    protected void onZero() {
        Bukkit.getPluginManager().callEvent(new GameStopEvent());
        RunForMoney.getInstance().getGameProcess().stop();
    }

    @Override
    protected void onNewSecond() {
        Map<String, Double> coinEarned = RunForMoney.getInstance().getCoinEarned();
        for (String i : TeamHolder.getInstance().getRunners()) {
            coinEarned.put(i, Math.max(coinEarned.getOrDefault(i, 0.00) + controller.getCoinPerSecond(), 0.00));
        }
        if (controller.getCoinPerSecond() < 0) {
            secs = secs + 2; // 为什么不是 +1 ? 因为 -1 再 +1 不能实现倒流。
        }

        String sec = String.valueOf(secs % 60);
        new SendingActionBarMessage(new TextComponent("剩余时间: " + (secs / 60) + ":" + (sec.length() == 1 ? ("0" + sec) : sec)), Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).filter(IT -> RFMTimerCommand.getSeePlayers().contains(IT.getName())).collect(Collectors.toList())).start();

        List<ScheduledRFMTaskImpl> executedTask = new ArrayList<>(); // 2022/3/12 针对可能存在的不打算保留对象引用的代码进行优化。
        for (ScheduledRFMTaskImpl task : tasks) {
            if (!task.isCancelled() && !task.isExecuted() && task.getRequiredTime() == getTimeLeft()) {
                try { // 这样可以保证所有计划任务都会被正常执行
                    task.executeItNow();
                } catch (Throwable e) {
                    RunForMoney.getInstance().getLogger().warning("A scheduled task generated an exception.");
                    e.printStackTrace();
                }
                executedTask.add(task);
            }
        }
        executedTask.forEach(tasks::remove);
    }

    @Override
    public int getTimeLeft() {
        return super.getTimeLeft();
    }

    public List<ScheduledRFMTaskImpl> getTasks() {
        return tasks;
    }
}
