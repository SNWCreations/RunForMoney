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
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import snw.rfm.RunForMoney;
import snw.rfm.game.GameProcess;
import snw.rfm.game.TeamHolder;
import snw.rfm.group.GroupHolder;
import snw.rfm.util.SendingActionBarMessage;

public final class HunterReleaseTimer extends BaseCountDownTimer {
    private final GameProcess process;

    public HunterReleaseTimer(int time, GameProcess process) {
        super(time);
        Validate.notNull(process);
        this.process = process;
    }

    @Override
    public BukkitTask start(Plugin plugin) {
        Bukkit.getOnlinePlayers().forEach(IT -> IT.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "全员逃走中", ChatColor.DARK_RED + "" + ChatColor.BOLD + "猎人将在 " + secs + " 秒后放出", 20, 60, 10));
        return super.start(plugin);
    }

    @Override
    protected void onZero() {
        TeamHolder holder = TeamHolder.getInstance();
        GroupHolder gh = GroupHolder.getInstance();

        // 2022/2/6 用 Stream 优化。
        holder.getHunters().stream().filter(IT -> gh.findByPlayer(IT) != null).forEach(holder::addEnabledHunter);

        new SendingActionBarMessage(new TextComponent(ChatColor.DARK_RED + "" + ChatColor.BOLD + "猎人已经放出"), Bukkit.getOnlinePlayers()).start();
        process.setHunterReleaseTimer(null);
        process.getMainTimer().start(RunForMoney.getInstance());
    }

    @Override
    protected void onNewSecond() {
        String text = "";
        if (secs == 30) {
            text = ChatColor.RED + "猎人还有 " + ChatColor.GREEN + ChatColor.BOLD + getTimeLeft() + ChatColor.RESET + ChatColor.RED + " 秒放出";
        } else if (secs == 15) {
            text = ChatColor.RED + "猎人还有 " + ChatColor.YELLOW + ChatColor.BOLD + getTimeLeft() + ChatColor.RESET + ChatColor.RED + " 秒放出";
        } else if (secs <= 10) {
            text = ChatColor.RED + "猎人还有 " + ChatColor.DARK_RED + ChatColor.BOLD + getTimeLeft() + ChatColor.RESET + ChatColor.RED + " 秒放出";
        }
        if (!text.equals("")) {
            new SendingActionBarMessage(new TextComponent(text), Bukkit.getOnlinePlayers()).start();
        }
    }

}
