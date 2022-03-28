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

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import snw.rfm.ItemRegistry;
import snw.rfm.RunForMoney;
import snw.rfm.api.GameController;
import snw.rfm.api.ItemEventListener;
import snw.rfm.api.events.HunterCatchPlayerEvent;
import snw.rfm.config.GameConfiguration;
import snw.rfm.config.Preset;
import snw.rfm.game.GameProcess;
import snw.rfm.game.TeamHolder;
import snw.rfm.group.Group;

import java.util.Objects;

import static snw.rfm.Util.removeAllPotionEffect;

public final class EventProcessor implements Listener {
    private static final TextComponent mcbbsHomeText;
    private static final TextComponent bilibiliHomeText;

    static {
        // 2022/2/19 增加亿点有关我的内容
        bilibiliHomeText = new TextComponent("B站: @ZX夏夜之风 (可点!)");
        bilibiliHomeText.setUnderlined(true);
        bilibiliHomeText.setColor(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE);
        bilibiliHomeText.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://space.bilibili.com/57486712"));
        bilibiliHomeText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("访问我的 B站 主页!")));

        mcbbsHomeText = new TextComponent("MCBBS: @ZX夏夜之风 (可点!)");
        mcbbsHomeText.setUnderlined(true);
        mcbbsHomeText.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        mcbbsHomeText.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.mcbbs.net/home.php?mod=space&uid=2190885"));
        mcbbsHomeText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("访问我的 MCBBS 主页!")));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        RunForMoney rfm = RunForMoney.getInstance();
        p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "================== 欢迎! ===================");
        p.sendMessage(ChatColor.GREEN + "此服务器正在运行 全员逃走中 插件, 版本 " + rfm.getDescription().getVersion());
        p.sendMessage(ChatColor.GOLD + "插件作者: ZX夏夜之风 (SNWCreations) @ MCBBS.NET");
        p.spigot().sendMessage(ChatMessageType.CHAT, bilibiliHomeText);
        p.spigot().sendMessage(ChatMessageType.CHAT, mcbbsHomeText);
        p.sendMessage("");

        GameProcess process = rfm.getGameProcess();
        if (process != null) { // 如果游戏正在进行
            TeamHolder holder = TeamHolder.getInstance();
            if (!(holder.isHunter(p) || holder.isRunner(p))) { // 如果既不是逃走队员也不是猎人
                p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "游戏" + (rfm.getGameController().isPaused() ? "已经暂停" : "正在进行") + "。" + "您以旁观者身份加入。");
                p.setHealth(Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
            }
        } else {
						p.setGameMode(GameMode.ADVENTURE);
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

            removeAllPotionEffect(p); // 2022/2/6 移除药水效果，但是改进了; 2022/2/20 改用 Util 类内置方法。

            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 0); // 感觉没什么用
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            GameProcess process = RunForMoney.getInstance().getGameProcess();
            TeamHolder holder = TeamHolder.getInstance();

            Entity entity = event.getEntity();
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();

            // 检查
            if (!(process != null && entity instanceof Player && damager instanceof Player)) {
                return;
            }

            event.setDamage(0.00); // 万一猎人有力量的药水效果，把玩家一下打没了呢？

            Player player = (Player) entity;
            Player hunter = (Player) damager;
            if (holder.isRunner(player) && holder.isHunter(hunter) && holder.isHunterEnabled(hunter)) {
                holder.removeRunner(player);
                player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
                process.checkStop();

                int player_remaining = holder.getRunners().toArray().length;
                HunterCatchPlayerEvent catchPlayerEvent = new HunterCatchPlayerEvent(player, hunter, player_remaining);
                Bukkit.getPluginManager().callEvent(catchPlayerEvent);
                if (catchPlayerEvent.isCancelled()) { // 2022/3/1 修复未对 HunterCatchPlayerEvent#isCancelled 方法的返回值做出处理的错误
                    return;
                }

                RunForMoney.getInstance().getCoinEarned().put(player.getName(), catchPlayerEvent.getCoinEarned(true)); // 2022/3/13 省的我再算一遍了 hhhhc

                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + player.getName() + " 被捕。");
                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "剩余 " + player_remaining + " 人。");

                Location el = GameConfiguration.getEndRoomLocation();
                if (el != null) { // 如果管理员在设置里放置了错误或者不可读的位置 xyz ，就会导致获取到的位置为 null
                    player.teleport(el); // 传送
                }
            }
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent event) {
        pauseIfNoPlayerFound();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        pauseIfNoPlayerFound();
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
            // region 2022/2/10 改用最稳定的方法
            boolean remove = false;
            for (ItemEventListener iep : ItemRegistry.getProcessorByItem(item)) {
                try { // 2022/3/12 保证所有此接口的实现都能被正常调用
                    boolean a = iep.onPlayerUseRequiredItem(player);
                    if (!remove && a) {
                        remove = true;
                    }
                } catch (Exception e) { // 2022/3/29 一个程序不应该尝试 catch 一个 Error ，所以从 Throwable 改为 Exception
                    RunForMoney.getInstance().getLogger().warning("An ItemEventListener generated an exception.");
                    e.printStackTrace();
                }
            }
            if (remove) {
                item.setAmount(item.getAmount() - 1);
            }
            // endregion
        }

    }


    private void pauseIfNoPlayerFound() {
        GameController gameController = RunForMoney.getInstance().getGameController();
        TeamHolder holder = TeamHolder.getInstance();

        if (!(gameController == null) && !gameController.isPaused() && (holder.isNoRunnerFound() || holder.isNoHunterFound())) {
            gameController.pause();
        }
    }
}
