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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import snw.rfm.ItemRegistry;
import snw.rfm.RunForMoney;
import snw.rfm.api.events.PlayerExitRFMEvent;
import snw.rfm.game.GameProcess;
import snw.rfm.game.TeamHolder;

public final class ExitingPickaxeProcessor implements Listener {

    @EventHandler
    public void onPlayerMining(PlayerItemBreakEvent event) { // 2022/1/27 修复弃权后方块未正常处理的错误
        RunForMoney rfm = RunForMoney.getInstance(); // 一堆 get 。。。
        GameProcess process = rfm.getGameProcess();
        if (process == null) {
            return;
        }

        ItemStack ep;
        if ((ep = ItemRegistry.getRegisteredItemByName("ep")) == null) {
            return;
        }

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

        if (!brokenitem.isSimilar(ep)) { // 2022/1/29 彻底修复27日关于本方法的错误
            return;
        }

        TeamHolder holder = TeamHolder.getInstance();
        Player p = event.getPlayer();
        Bukkit.getPluginManager().callEvent(new PlayerExitRFMEvent(event.getPlayer())); // 触发事件

        holder.removeRunner(p);
        process.out(event.getPlayer());

        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + p.getName() + " 已弃权。"); // 播报
        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "剩余 " + holder.getRunners().toArray().length + " 人。");

        process.checkStop();
    }
}
