package snw.rfm.game;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import snw.rfm.RunForMoney;
import snw.rfm.events.GameStopEvent;
import snw.rfm.events.HunterCatchPlayerEvent;
import snw.rfm.events.PlayerExitRFMEvent;
import snw.rfm.group.Group;
import snw.rfm.timers.AFKNotifyTimer;
import snw.rfm.timers.BaseCountDownTimer;
import snw.rfm.timers.CoinTimer;

import java.util.Map;

public final class EventProcessor implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        RunForMoney rfm = RunForMoney.getInstance();
        p.setGameMode(GameMode.ADVENTURE);
        p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "================== 欢迎! ===================");
        p.sendMessage(ChatColor.GREEN + "此服务器正在运行 全员逃走中 插件, 版本 " + rfm.getDescription().getVersion());
        p.sendMessage(ChatColor.GREEN + "插件作者: ZX夏夜之风 (SNWCreations) @ MCBBS.NET");
        p.sendMessage(ChatColor.LIGHT_PURPLE + "B站: @ZX夏夜之风 (ID: 57486712)");
        p.sendMessage(ChatColor.GOLD + "MCBBS: @ZX夏夜之风 (ID: 2190885)");
        p.sendMessage("");
        GameProcess process = rfm.getGameProcess();
        if (process != null) {
            TeamHolder holder = rfm.getTeamHolder();
            if (!(holder.isHunter(p) || holder.isRunner(p))) {
                p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "游戏正在进行。你以旁观者身份加入。");
                process.out(p);
            }
        } else {
            Preset preset = rfm.getPreset();
            if (preset.isPresetHunter(p)) {
                p.performCommand("hunter");
                p.sendMessage(ChatColor.GREEN + "你被管理员预设为猎人。");
                Group playerWillBeJoined = rfm.getPreset().getPlayerNotJoinedGroup(p);
                if (playerWillBeJoined != null) {
                    playerWillBeJoined.add(p);
                    p.sendMessage(ChatColor.GREEN + "你被预设加入组 " + playerWillBeJoined.getName());
                }
            }
            if (preset.isPresetRunner(p)) {
                p.performCommand("runner");
                p.sendMessage(ChatColor.GREEN + "你被管理员预设为逃走队员。");
            }
            rfm.getCoinEarned().putIfAbsent(p, 0.00);
            p.removePotionEffect(PotionEffectType.SPEED);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 0); // 感觉没什么用
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        RunForMoney rfm = RunForMoney.getInstance();
        GameProcess process = rfm.getGameProcess();
        TeamHolder holder = rfm.getTeamHolder();
        if (!(process != null && event.getEntity() instanceof Player && event.getDamager() instanceof Player)) {
            return;
        }
        Player catched = ((Player) event.getEntity());
        Player hunter = ((Player) event.getDamager());
        if (holder.isHunter(hunter) && holder.isHunterEnabled(hunter)) {
            process.out(catched);
            holder.removeRunner(catched);

            int player_remaining = holder.getRunners().toArray().length;
            Bukkit.getPluginManager().callEvent(new HunterCatchPlayerEvent(catched, hunter, player_remaining));

            for (BaseCountDownTimer t : process.getTimers()) {
                if (t instanceof CoinTimer) {
                    Map<Player, Double> earned = ((CoinTimer) t).getCoinEarned();
                    earned.put(catched, earned.get(catched) / 10);
                }
            }

            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + catched.getName() + " 被捕。");
            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "剩余 " + player_remaining + " 人。");
            catched.teleport(rfm.getGameConfiguration().getEndRoomLocation());
            if (player_remaining == 0) {
                Bukkit.getPluginManager().callEvent(new GameStopEvent());
                process.stop();
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        RunForMoney rfm = RunForMoney.getInstance();
        GameProcess process = rfm.getGameProcess();
        TeamHolder holder = rfm.getTeamHolder();
        Player p = event.getPlayer();
        if (process == null) {
            return;
        }
        if (holder.isHunter(p) && holder.isHunterEnabled(p)) {
            event.setCancelled(true);
        } else if (holder.isRunner(event.getPlayer())) {
            for (BaseCountDownTimer t : process.getTimers()) {
                if (t instanceof AFKNotifyTimer) {
                    ((AFKNotifyTimer) t).removeAFKPlayer(p);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMining(PlayerItemBreakEvent event) { // 2022/1/27 修复弃权后方块未正常处理的错误
        RunForMoney rfm = RunForMoney.getInstance();
        GameProcess process = rfm.getGameProcess();
        TeamHolder holder = rfm.getTeamHolder();
        Player p = event.getPlayer();
        if (!(process == null)) {
            Bukkit.getPluginManager().callEvent(new PlayerExitRFMEvent(event.getPlayer()));

            process.out(event.getPlayer());
            holder.removeRunner(p);
            int runner_remaining = holder.getRunners().toArray().length;
            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + p.getName() + " 已弃权。");
            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "剩余 " + runner_remaining + " 人。");

            if (runner_remaining == 0) {
                Bukkit.getPluginManager().callEvent(new GameStopEvent());
                process.stop();
            }
        }
    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent event) {
        RunForMoney rfm = RunForMoney.getInstance();
        GameProcess process = rfm.getGameProcess();
        TeamHolder holder = rfm.getTeamHolder();
        
        if (!(process == null)) {
            if (holder.isNoRunnerFound() && holder.isNoHunterFound()) {
                process.pause();
            }
        }
    }
}
