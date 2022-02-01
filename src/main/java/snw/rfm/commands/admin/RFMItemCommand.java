package snw.rfm.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snw.rfm.item.RFMItems;

import java.util.ArrayList;
import java.util.List;

public class RFMItemCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "你必须是个玩家。");
        } else if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "参数不足！");
            return false;
        } else {
            Player s = (Player) sender;
            if (args[0].equalsIgnoreCase("hpc")) {
                addItemIfNotNull(RFMItems.HUNTER_PAUSE_CARD, s);
            } else if (args[0].equalsIgnoreCase("ep")) {
                addItemIfNotNull(RFMItems.EXIT_PICKAXE, s);
            } else {
                sender.sendMessage(ChatColor.RED + "物品不存在。");
                return false;
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> result = new ArrayList<>();

            // region 添加可选项
            result.add("hpc"); // 猎人暂停卡
            result.add("ep"); // 弃权镐
            // endregion

            return result;
        } else {
            return null;
        }
    }

    private void addItemIfNotNull(@Nullable ItemStack item, Player player) {
        if (item != null) {
            player.getInventory().addItem(item);
        } else {
            player.sendMessage(ChatColor.RED + "操作失败。因为请求的物品未被正确创建，请联系管理员获取原因。");
        }
    }
}
