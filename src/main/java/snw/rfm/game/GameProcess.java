/**
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.RunForMoney;
import snw.rfm.Util;
import snw.rfm.config.GameConfiguration;
import snw.rfm.tasks.BaseCountDownTimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class GameProcess {
    private final ArrayList<BaseCountDownTimer> timers = new ArrayList<>();

    public void start() {
        RunForMoney.getInstance().getCoinEarned().clear();
        TeamHolder h = TeamHolder.getInstance();
        Bukkit.broadcastMessage(ChatColor.RED + "游戏即将开始！");
        int releaseTime = GameConfiguration.getReleaseTime(); // 2022/2/2 优化: 一次取值，避免反复调用。
        int gameTimeOnTick = GameConfiguration.getGameTime() * 60 * 20; // 2022/2/6 再次优化
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "全员逃走中", ChatColor.DARK_RED + "" + ChatColor.BOLD + "猎人将在 " + releaseTime + " 秒后放出。", 20, 60, 10);
            if (h.getHunters().contains(p)) {
                // 2022/2/2 改进: 利用原版特性使猎人不能移动
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, gameTimeOnTick, 255, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, gameTimeOnTick, 129, false));
            } else if (!h.getRunners().contains(p)) {
                p.sendMessage(ChatColor.RED + "你没有选择队伍，因此你现在是旁观者。");
                p.setGameMode(GameMode.SPECTATOR);
            }
        }
        timers.forEach(BaseCountDownTimer::start);
    }

    public void stop() {
        RunForMoney rfm = RunForMoney.getInstance();
        Location el = GameConfiguration.getEndRoomLocation();
        Bukkit.broadcastMessage(ChatColor.GREEN + "游戏结束！");
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getInventory().clear();
            if (el != null) { // 如果管理员在设置里放置了错误或者不可读的位置 xyz ，就会导致获取到的位置为 null
                p.teleport(el); // 传送
            }
            Arrays.stream(PotionEffectType.values()).forEach(p::removePotionEffect); // 2022/2/6 移除药水效果，但是改进了
            p.setGameMode(GameMode.ADVENTURE);
        }
        timers.forEach(BukkitRunnable::cancel);
        timers.clear();
        TeamHolder.getInstance().cleanup();
        rfm.setGameProcess(null);
    }

    public void pause() {
        Bukkit.broadcastMessage(ChatColor.RED + "游戏暂停。");
        timers.forEach(BukkitRunnable::cancel);
    }

    public void resume() {
        Bukkit.broadcastMessage(ChatColor.GREEN + "游戏继续。");
        timers.forEach(BaseCountDownTimer::start);
    }

    public void out(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();
        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        Util.ifZeroStop();
    }

    public void addTimer(BaseCountDownTimer timer) {
        timers.add(timer);
    }

    public List<BaseCountDownTimer> getTimers() {
        return timers;
    }
}
