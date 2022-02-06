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

import java.util.Arrays;
import java.util.List;

import static snw.rfm.Util.getAllTheStringsStartingWithListInTheList;

public class RFMItemCommand implements CommandExecutor, TabCompleter {
    private final List<String> possibleChoices = Arrays.asList("hpc", "ep", "sl");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "你必须是个玩家。");
        } else if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "参数不足！");
            return false;
        } else {
            Player s = (Player) sender;
            for (String o : args) {
                switch (o.toLowerCase()) {
                    case "hpc":
                        addItemIfNotNull(RFMItems.HUNTER_PAUSE_CARD, s);
                        break;
                    case "ep": // 如果你的插件配置正常但不能获得弃权镐，请联系作者。
                        addItemIfNotNull(RFMItems.EXIT_PICKAXE, s);
                        break;
                    case "sl":
                        addItemIfNotNull(RFMItems.LIFE_SAVER, s);
                        break;
                    default:
                        sender.sendMessage(ChatColor.RED + "请求的物品 " + o + " 不存在。");
                        break;
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return (args.length > 0) ? getAllTheStringsStartingWithListInTheList(args[args.length - 1], possibleChoices, false) : null;
    }

    private void addItemIfNotNull(@Nullable ItemStack item, Player player) {
        if (item != null) {
            player.getInventory().addItem(item);
        } else {
            player.sendMessage(ChatColor.RED + "操作失败。因为请求的物品未被正确创建，请联系管理员获取原因。");
        }
    }
}
