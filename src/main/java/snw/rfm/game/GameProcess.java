package snw.rfm.game;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import snw.rfm.RunForMoney;
import snw.rfm.timers.BaseCountDownTimer;

import java.util.*;

public final class GameProcess {
    private final ArrayList<BaseCountDownTimer> timers;
    private final GameConfiguration config;

    public GameProcess() {
        config = RunForMoney.getInstance().getGameConfiguration();
        timers = new ArrayList<>();
    }

    public void start() {
        RunForMoney rfm = RunForMoney.getInstance();
        TeamHolder h = rfm.getTeamHolder();
        Bukkit.broadcastMessage(ChatColor.RED + "游戏即将开始！");
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(ChatColor.RED + "全员逃走中", ChatColor.DARK_RED + "猎人将在 " + config.getReleaseTime() + " 秒后放出。", 20, 60, 10);
            if (h.getHunters().contains(p)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 1, false));
            } else if (!(h.getRunners().contains(p))) {
                p.sendMessage(ChatColor.RED + "因为你没有选择队伍，因此你现在是旁观者。");
                p.setGameMode(GameMode.SPECTATOR);
            }
        }
        for (BaseCountDownTimer t : timers) {
            t.start(RunForMoney.getInstance());
        }
    }

    public void stop() {
        Bukkit.broadcastMessage(ChatColor.GREEN + "游戏结束！");
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getInventory().clear();
            p.teleport(config.getEndRoomLocation());
            p.setGameMode(GameMode.ADVENTURE);
            p.removePotionEffect(PotionEffectType.SPEED);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 0);
        }
        for (BaseCountDownTimer t : timers) {
            t.cancel();
        }
        timers.clear();
        RunForMoney.getInstance().getTeamHolder().cleanup();
        RunForMoney.getInstance().setGameProcess(null);
    }

    public void pause() {
        Bukkit.broadcastMessage(ChatColor.RED + "游戏暂停。");
        for (BaseCountDownTimer t : timers) {
            t.cancel();
        }
    }

    public void resume() {
        Bukkit.broadcastMessage(ChatColor.GREEN + "游戏继续。");
        for (BaseCountDownTimer t : timers) {
            t.start(RunForMoney.getInstance());
        }
    }

    public void out(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();
        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
    }

    public void addTimer(BaseCountDownTimer timer) {
        timers.add(timer);
    }

    public List<BaseCountDownTimer> getTimers() {
        return timers;
    }


}
