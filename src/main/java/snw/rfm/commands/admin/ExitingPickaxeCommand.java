package snw.rfm.commands.admin;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;

public final class ExitingPickaxeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "你必须是个玩家。");
        } else {
            ItemStack stack = new ItemStack(Material.WOODEN_PICKAXE);
            ItemMeta meta = stack.getItemMeta();
            //noinspection ConstantConditions
            meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "弃权镐");
            Damageable converted_meta = (Damageable) meta;
            converted_meta.setDamage(58);
            stack.setItemMeta((ItemMeta) converted_meta);
            NBTItem item = new NBTItem(stack);
            item.getStringList("CanDestroy").add(RunForMoney.getInstance().getGameConfiguration().getExitingPickaxeMinableBlock());
            ((Player) sender).getInventory().addItem(item.getItem());
        }
        return true;
    }
}
