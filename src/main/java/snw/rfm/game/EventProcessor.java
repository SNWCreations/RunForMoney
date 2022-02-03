package snw.rfm.game;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import snw.rfm.RunForMoney;
import snw.rfm.events.GameStopEvent;
import snw.rfm.events.HunterCatchPlayerEvent;
import snw.rfm.events.PlayerExitRFMEvent;
import snw.rfm.group.Group;
import snw.rfm.item.RFMItems;

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
        if (process != null) { // 如果游戏正在进行
            TeamHolder holder = TeamHolder.getInstance();
            if (!(holder.isHunter(p) || holder.isRunner(p))) { // 如果既不是逃走队员也不是猎人
                p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "游戏正在进行。你以旁观者身份加入。");
                process.out(p); // 只是处理，因为玩家不在游戏中，所以不是真淘汰。
            }
            // 没有额外处理 lol
        } else {
            // region 预设部分
            Preset preset = Preset.getInstance();
            if (preset.isPresetHunter(p)) {
                p.performCommand("hunter");
                p.sendMessage(ChatColor.GREEN + "你被管理员预设为猎人。");
                Group playerWillBeJoined = preset.getPlayerNotJoinedGroup(p);
                if (playerWillBeJoined != null) {
                    playerWillBeJoined.add(p);
                    p.sendMessage(ChatColor.GREEN + "你被预设加入组 " + playerWillBeJoined.getName());
                }
            }
            if (preset.isPresetRunner(p)) {
                p.performCommand("runner");
                p.sendMessage(ChatColor.GREEN + "你被管理员预设为逃走队员。");
            }
            // endregion

            for (PotionEffectType pe : PotionEffectType.values()) { // 移除该玩家的一切状态效果
                p.removePotionEffect(pe);
            }

            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 0); // 感觉没什么用
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        GameProcess process = RunForMoney.getInstance().getGameProcess();
        TeamHolder holder = TeamHolder.getInstance();

        // 检查
        if (!(process != null && event.getEntity() instanceof Player && event.getDamager() instanceof Player && holder.isRunner((Player) event.getEntity()))) { // 2022/2/2 修复可能导致猎人误抓队友的错误。hhhhhhhc
            return;
        }

        Player catched = ((Player) event.getEntity());
        Player hunter = ((Player) event.getDamager());
        if (holder.isHunter(hunter) && holder.isHunterEnabled(hunter)) {
            process.out(catched);
            holder.removeRunner(catched);

            int player_remaining = holder.getRunners().toArray().length;
            Bukkit.getPluginManager().callEvent(new HunterCatchPlayerEvent(catched, hunter, player_remaining));

            Map<Player, Double> earned = RunForMoney.getInstance().getCoinEarned(); // 2022/2/2 有现成的 get 我不用。。。
            earned.put(catched, earned.get(catched) / 10); // B币设为当时的 1/10

            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + catched.getName() + " 被捕。");
            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "剩余 " + player_remaining + " 人。");

            Location el = GameConfiguration.getInstance().getEndRoomLocation();
            if (el != null) { // 如果管理员在设置里放置了错误或者不可读的位置 xyz ，就会导致获取到的位置为 null
                catched.teleport(el); // 传送
            }

            ifZeroStop(); // 你看这多方便
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        RunForMoney rfm = RunForMoney.getInstance();
        GameProcess process = rfm.getGameProcess();
        TeamHolder holder = TeamHolder.getInstance();
        Player p = event.getPlayer();
        if (process == null) {
            return;
        }
        if (holder.isHunter(p) && holder.isHunterEnabled(p)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMining(PlayerItemBreakEvent event) { // 2022/1/27 修复弃权后方块未正常处理的错误
        // region 检查前的预处理
        ItemStack brokenitem = event.getBrokenItem().clone();
        if (brokenitem.getType() != Material.WOODEN_PICKAXE) {
            return;
        }
        ItemMeta meta = brokenitem.getItemMeta();
        assert meta != null; // 我知道这个不可能是 null 但是 IDEA 就是想让我写 assert
        ((Damageable) meta).setDamage(58); // 不这么改一下会出现因为耐久度不同而判定出错的问题
        brokenitem.setItemMeta(meta);
        // endregion

        if (!brokenitem.isSimilar(RFMItems.EXIT_PICKAXE)) { // 2022/1/29 彻底修复27日关于本方法的错误
            return;
        }
        RunForMoney rfm = RunForMoney.getInstance(); // 一堆 get 。。。
        GameProcess process = rfm.getGameProcess();
        TeamHolder holder = TeamHolder.getInstance();
        Player p = event.getPlayer();
        if (!(process == null)) {
            Bukkit.getPluginManager().callEvent(new PlayerExitRFMEvent(event.getPlayer())); // 触发事件

            process.out(event.getPlayer()); // 淘汰"处理"
            holder.removeRunner(p); // 这才是真淘汰

            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + p.getName() + " 已弃权。"); // 播报
            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "剩余 " + holder.getRunners().toArray().length + " 人。");

            ifZeroStop(); // 这多方便
        }
    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent event) {
        RunForMoney rfm = RunForMoney.getInstance();
        GameProcess process = rfm.getGameProcess();
        TeamHolder holder = TeamHolder.getInstance();

        if (!(process == null)) {
            if (holder.isNoRunnerFound() || holder.isNoHunterFound()) { // 2022/2/3 v1.1.3 虽然只是一个逻辑判断，却带来了大Bug。
                process.pause();
            }
        }
    }

    @EventHandler
    public void onPlayerGameModeChanged(PlayerGameModeChangeEvent event) {
        if (RunForMoney.getInstance().getGameProcess() == null) {
            return;
        }

        Player player = event.getPlayer();
        if (event.getNewGameMode() == GameMode.SPECTATOR) {
            for (PotionEffectType t : PotionEffectType.values()) {
                player.removePotionEffect(t);
            }
        }
    }

    private void ifZeroStop() {
        if (TeamHolder.getInstance().getRunners().toArray().length == 0) {
            Bukkit.getPluginManager().callEvent(new GameStopEvent());
            RunForMoney.getInstance().getGameProcess().stop();
        }
    }
}
