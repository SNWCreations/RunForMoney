/*
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.processor;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import snw.rfm.ItemRegistry;
import snw.rfm.RFMItems;
import snw.rfm.RunForMoney;
import snw.rfm.Util;
import snw.rfm.api.events.HunterCatchPlayerEvent;
import snw.rfm.api.events.PlayerExitRFMEvent;
import snw.rfm.config.GameConfiguration;
import snw.rfm.config.Preset;
import snw.rfm.game.GameProcess;
import snw.rfm.game.TeamHolder;
import snw.rfm.group.Group;

import java.util.Arrays;
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
            if (Preset.isPresetHunter(p)) {
                p.performCommand("hunter");
                p.sendMessage(ChatColor.GREEN + "你被管理员预设为猎人。");
                Group playerWillBeJoined = Preset.getPlayerNotJoinedGroup(p);
                if (playerWillBeJoined != null) {
                    playerWillBeJoined.add(p);
                    p.sendMessage(ChatColor.GREEN + "你被预设加入组 " + playerWillBeJoined.getName());
                }
            } else if (Preset.isPresetRunner(p)) { // 2022/2/6 避免喜欢恶作剧的用代码玩这个插件。。我真是操碎了心啊。。
                p.performCommand("runner");
                p.sendMessage(ChatColor.GREEN + "你被管理员预设为逃走队员。");
            }
            // endregion

            Arrays.stream(PotionEffectType.values()).forEach(p::removePotionEffect); // 2022/2/6 移除药水效果，但是改进了

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

            Location el = GameConfiguration.getEndRoomLocation();
            if (el != null) { // 如果管理员在设置里放置了错误或者不可读的位置 xyz ，就会导致获取到的位置为 null
                catched.teleport(el); // 传送
            }
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

            Util.ifZeroStop(); // 这多方便
        }
    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent event) {
        pauseIfNoPlayerFound();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        pauseIfNoPlayerFound();
    }

    @EventHandler
    public void onPlayerGameModeChanged(PlayerGameModeChangeEvent event) {
        if (RunForMoney.getInstance().getGameProcess() == null) {
            return;
        }

        Player player = event.getPlayer();
        if (event.getNewGameMode() == GameMode.SPECTATOR) {
            Util.removeAllPotionEffect(player); // 2022/2/9 优化一下。
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (RunForMoney.getInstance().getGameProcess() == null) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (ItemRegistry.getByItem(item).stream().anyMatch(IT -> IT.onPlayerUseRequiredItem(player))) {
                item.setAmount(item.getAmount() - 1);
            }
        }

    }


    private void pauseIfNoPlayerFound() {
        GameProcess process = RunForMoney.getInstance().getGameProcess();
        TeamHolder holder = TeamHolder.getInstance();

        if (!(process == null)) {
            if (holder.isNoRunnerFound() || holder.isNoHunterFound()) { // 2022/2/3 v1.1.3 虽然只是一个逻辑判断，却带来了大Bug。
                process.pause();
            }
        }
    }
}
