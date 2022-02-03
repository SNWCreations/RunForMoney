package snw.rfm.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import snw.rfm.RunForMoney;
import snw.rfm.tasks.BaseCountDownTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class GameProcess {
    private final ArrayList<BaseCountDownTimer> timers;
    private final GameConfiguration config;

    public GameProcess() {
        config = GameConfiguration.getInstance();
        timers = new ArrayList<>();
    }

    public void start() {
        TeamHolder h = TeamHolder.getInstance();
        Bukkit.broadcastMessage(ChatColor.RED + "游戏即将开始！");
        int releaseTime = config.getReleaseTime(); // 2022/2/2 优化: 一次取值，避免反复调用。
        int gameTime = config.getGameTime();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "全员逃走中", ChatColor.DARK_RED + "" + ChatColor.BOLD + "猎人将在 " + releaseTime + " 秒后放出。", 20, 60, 10);
            if (h.getHunters().contains(p)) {
                // 2022/2/2 改进: 利用原版特性使猎人不能移动
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, gameTime * 20, 255, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, gameTime * 20, 129, false));
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
        RunForMoney rfm = RunForMoney.getInstance();
        Location el = GameConfiguration.getInstance().getEndRoomLocation();
        Bukkit.broadcastMessage(ChatColor.GREEN + "游戏结束！");
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getInventory().clear();
            if (el != null) { // 如果管理员在设置里放置了错误或者不可读的位置 xyz ，就会导致获取到的位置为 null
                p.teleport(el); // 传送
            }
            for (PotionEffectType t : PotionEffectType.values()) {
                p.removePotionEffect(t); // 移除药水效果。
            }
            p.setGameMode(GameMode.ADVENTURE);
        }
        for (BaseCountDownTimer t : timers) {
            t.cancel();
        }
        timers.clear();
        TeamHolder.getInstance().cleanup();
        rfm.setGameProcess(null);
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
