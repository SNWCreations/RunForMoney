/**
 * This file is part of RunForMoney.
 * <p>
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * <p>
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.game;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.RunForMoney;
import snw.rfm.api.events.GameStopEvent;
import snw.rfm.tasks.BaseCountDownTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static snw.rfm.Util.removeAllPotionEffect;

public final class GameProcess {
    private final ArrayList<BaseCountDownTimer> timers = new ArrayList<>();
    private int noMoveTime = 0;
    private static final TextComponent yes;
    private static final TextComponent no;

    static {
        yes = new TextComponent("[是]");
        yes.setColor(net.md_5.bungee.api.ChatColor.RED);
        yes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("如果您选择此选项，插件将会以您的身份终止游戏。")));
        yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/forcestop"));
        no = new TextComponent(" [否]");
        no.setColor(net.md_5.bungee.api.ChatColor.GRAY);
        no.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("如果您选择此选项，插件会通知所有玩家，并且在终止游戏前您将不能启动新游戏，剩余操作由您进行。")));
        no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/say 游戏暂不终止。"));
    }

    public void start() {
        RunForMoney rfm = RunForMoney.getInstance();
        rfm.getCoinEarned().clear();
        TeamHolder h = TeamHolder.getInstance();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (h.getHunters().contains(p.getName())) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            } else if (!h.getRunners().contains(p.getName())) {
                p.sendMessage(ChatColor.RED + "你没有选择队伍，因此你现在是旁观者。");
                p.setGameMode(GameMode.SPECTATOR);
            }
        }
        Bukkit.getScheduler().runTaskTimer(rfm, () -> {
            int prev = getHunterNoMoveTime();
            if (prev > 0) {
                setHunterNoMoveTime(prev - 1);
            }
        }, 20L, 20L);

        timers.forEach(IT -> IT.start(rfm));
    }

    public void stop() {
        RunForMoney rfm = RunForMoney.getInstance();
        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "游戏结束");
        Bukkit.getScheduler().cancelTasks(rfm);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getInventory().clear();
            removeAllPotionEffect(p);
            p.setGameMode(GameMode.ADVENTURE);
        }
        timers.clear();
        TeamHolder.getInstance().cleanup();
        rfm.setGameProcess(null);
        rfm.setGameController(null);
    }

    public void pause() {
        Bukkit.broadcastMessage(ChatColor.RED + "游戏暂停。");
        timers.forEach(BukkitRunnable::cancel);
    }

    public void resume() {
        Bukkit.broadcastMessage(ChatColor.GREEN + "游戏继续。");
        timers.forEach(IT -> IT.start(RunForMoney.getInstance()));
    }

    public void out(Player player) {
        Validate.notNull(player);
        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
    }

    public void addTimer(BaseCountDownTimer timer) {
        Validate.notNull(timer);
        timers.add(timer);
    }

    public List<BaseCountDownTimer> getTimers() {
        return timers;
    }

    public void setHunterNoMoveTime(int time) {
        noMoveTime = time;
    }

    public boolean isHunterCanMove() {
        return noMoveTime <= 0;
    }

    public int getHunterNoMoveTime() {
        return noMoveTime;
    }

    private void askAdminOnNoRunnerAlive() {
        for (Player op : Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).collect(Collectors.toList())) {
            op.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "现在所有玩家均已不在游戏中(指被捕或被淘汰)，由您决定是否结束游戏。");
            op.spigot().sendMessage(ChatMessageType.CHAT, yes, no);
        }
    }

    public void checkStop() {
        if (TeamHolder.getInstance().getRunners().toArray().length <= 0) {
            if (RunForMoney.getInstance().getConfig().getBoolean("stop_game_on_no_runner_alive", true)) {
                Bukkit.getPluginManager().callEvent(new GameStopEvent());
                stop();
            } else {
                Bukkit.getScheduler().cancelTasks(RunForMoney.getInstance());
                Bukkit.broadcastMessage(ChatColor.RED + "所有玩家均已不在逃走中游戏内，现在由管理员决定是否结束游戏。");
                askAdminOnNoRunnerAlive();
            }
        }
    }
}
