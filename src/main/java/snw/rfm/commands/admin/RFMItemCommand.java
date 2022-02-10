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
import snw.rfm.ItemRegistry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static snw.rfm.Util.getAllTheStringsStartingWithListInTheList;

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
            new HashSet<>(Arrays.asList(args)).forEach(IT -> {
                ItemStack item;
                if ((item = ItemRegistry.getRegisteredItemByName(IT)) != null) {
                    s.getInventory().addItem(item);
                } else {
                    s.sendMessage(ChatColor.RED + "请求的物品 " + IT + " 不存在。");
                }
            });
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return (sender instanceof Player && sender.isOp()) ? ((args.length > 0) ? getAllTheStringsStartingWithListInTheList(args[args.length - 1], ItemRegistry.getRegisteredItemNames(), false) : null) : null;
    }

}
