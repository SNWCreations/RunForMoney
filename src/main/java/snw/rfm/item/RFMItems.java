package snw.rfm.item;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public final class RFMItems {
    public static ItemStack HUNTER_PAUSE_CARD;
    public static ItemStack EXIT_PICKAXE;

    public static void init() {
        ItemConfiguration iconf = ItemConfiguration.getInstance();


        // region 弃权镐
        ItemStack ep = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta epmeta = ep.getItemMeta();
        //noinspection ConstantConditions
        epmeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "弃权镐");
        Damageable converted_meta = (Damageable) epmeta;
        converted_meta.setDamage(58);
        ep.setItemMeta((ItemMeta) converted_meta);
        NBTItem item = new NBTItem(ep);
        item.getStringList("CanDestroy").add(iconf.getExitingPickaxeMinableBlock());
        EXIT_PICKAXE = item.getItem();
        // endregion

        // region 猎人暂停卡 (2022/1/30)
        Material hpctype = Material.matchMaterial(iconf.getItemType("hpc"));
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
    }

}
