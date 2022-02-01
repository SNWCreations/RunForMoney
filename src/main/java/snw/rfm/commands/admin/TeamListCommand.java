package snw.rfm.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import snw.rfm.game.TeamHolder;

import java.util.Iterator;
import java.util.Set;

public class TeamListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        TeamHolder holder = TeamHolder.getInstance();
        Set<Player> hl = holder.getHunters();
        Set<Player> rl = holder.getRunners();

        if (hl.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "猎人队伍没有成员。");
        } else {
            StringBuilder hunters = new StringBuilder();
            Iterator<Player> h = hl.iterator();
            while (true) {
                hunters.append(h.next().getName());
                if (h.hasNext()) {
                    hunters.append(", ");
                } else {
                    break;
                }
            }
            sender.sendMessage(ChatColor.GREEN + "猎人队伍: " + hunters);
        }

        if (rl.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "逃走队员队伍没有成员。");
        } else {
            StringBuilder runners = new StringBuilder();
            Iterator<Player> r = rl.iterator();
            while (true) {
                runners.append(r.next().getName());
                if (r.hasNext()) {
                    runners.append(", ");
                } else {
                    break;
                }
            }
            sender.sendMessage(ChatColor.GREEN + "逃走队员队伍: " + runners);
        }

        return true;
    }
}
