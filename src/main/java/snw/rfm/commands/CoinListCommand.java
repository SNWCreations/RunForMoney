package snw.rfm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CoinListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Map<Player, Double> coinEarned = RunForMoney.getInstance().getCoinEarned();
        if (coinEarned.size() == 0) {
            sender.sendMessage(ChatColor.RED + "B币榜为空！");
        } else {
            sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "============ B币榜 ============");
            Map<Player, Double> coin = sortDescend(coinEarned);
            int a = 0;
            for (Player p : coin.keySet()) {
                sender.sendMessage(ChatColor.GREEN + "" + ++a + "." + p.getName() + ": " + coin.get(p));
            }
        }
        return true;
    }

    // Map的value值降序排序
    public static <K, V extends Comparable<? super V>> Map<K, V> sortDescend(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort((o1, o2) -> {
            int compare = (o1.getValue()).compareTo(o2.getValue());
            return -compare;
        });

        Map<K, V> returnMap = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            returnMap.put(entry.getKey(), entry.getValue());
        }
        return returnMap;
    }
}
