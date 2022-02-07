/*
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import snw.rfm.config.ItemConfiguration;

public final class RFMItems {
    public static ItemStack HUNTER_PAUSE_CARD;
    public static ItemStack EXIT_PICKAXE;
    public static ItemStack LIFE_SAVER;

    public static void init() {
        // region 弃权镐
        ItemStack ep = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta epmeta = ep.getItemMeta();
        //noinspection ConstantConditions
        epmeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "弃权镐");
        Damageable converted_meta = (Damageable) epmeta;
        converted_meta.setDamage(58);
        ep.setItemMeta((ItemMeta) converted_meta);
        NBTItem item = new NBTItem(ep);
        item.getStringList("CanDestroy").add(ItemConfiguration.getExitingPickaxeMinableBlock());
        EXIT_PICKAXE = item.getItem();
        // endregion

        // region 猎人暂停卡 (2022/1/30)
        Material hpctype = Material.matchMaterial(ItemConfiguration.getItemType("hpc"));
        if (hpctype == null) {
            Bukkit.getConsoleSender().sendMessage("[RunForMoney] " + ChatColor.YELLOW + "警告: 创建物品 猎人暂停卡 时出现错误: 未提供一个可用的物品类型。请检查配置！");
        } else {
            ItemStack hpc = new ItemStack(hpctype);
            ItemMeta hpcmeta = hpc.getItemMeta();
            assert hpcmeta != null;
            hpcmeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "猎人暂停卡");
            hpc.setItemMeta(hpcmeta);
            HUNTER_PAUSE_CARD = hpc;
        }
        // endregion

        // region 保命符 (2022/2/5)
        Material sltype = Material.matchMaterial(ItemConfiguration.getItemType("hpc"));
        if (sltype == null) {
            Bukkit.getConsoleSender().sendMessage("[RunForMoney] " + ChatColor.YELLOW + "警告: 创建物品 保命符 时出现错误: 未提供一个可用的物品类型。请检查配置！");
        } else {
            ItemStack sl = new ItemStack(sltype);
            ItemMeta slmeta = sl.getItemMeta();
            assert slmeta != null;
            slmeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "保命符");
            sl.setItemMeta(slmeta);
            LIFE_SAVER = sl;
        }
        // endregion
    }

}
