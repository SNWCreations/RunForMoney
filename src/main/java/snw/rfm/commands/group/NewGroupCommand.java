package snw.rfm.commands.group;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import snw.rfm.group.Group;
import snw.rfm.group.GroupHolder;

public final class NewGroupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "参数不足或过多！");
            return false;
        }
        GroupHolder groups = GroupHolder.getInstance();
        if (groups.findByName(args[0]) != null) {
            sender.sendMessage(ChatColor.RED + "操作失败。此组已存在。");
        } else {
            groups.add(new Group(args[0]));
            sender.sendMessage(ChatColor.GREEN + "成功地创建了组 " + args[0]);
        }
        return true;
    }
}
