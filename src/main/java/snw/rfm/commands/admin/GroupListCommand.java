package snw.rfm.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import snw.rfm.group.Group;
import snw.rfm.group.GroupHolder;

import java.util.Iterator;

public final class GroupListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        GroupHolder holder = GroupHolder.getInstance(); // 2020/1/30 重构 GroupHolder
        int length = holder.toArray().length;
        if (length == 0) {
            sender.sendMessage(ChatColor.RED + "没有已存在的组。");
        } else {
            StringBuilder result = new StringBuilder("现有以下组: ");
            Iterator<Group> i = holder.iterator();
            while (true) {
                result.append(i.next().getName());
                if (i.hasNext()) {
                    result.append(", ");
                } else {
                    break;
                }
            }
            sender.sendMessage(ChatColor.GREEN + result.toString());
            sender.sendMessage(ChatColor.GREEN + "共有 " + length + " 个。");
        }
        return true;
    }
}
