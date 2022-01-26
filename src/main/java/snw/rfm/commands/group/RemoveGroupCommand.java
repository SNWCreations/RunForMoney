package snw.rfm.commands.group;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.group.Group;
import snw.rfm.group.GroupHolder;

public final class RemoveGroupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "参数不足或过多！");
            return false;
        }
        GroupHolder holder = RunForMoney.getInstance().getGroups();
        Group group = holder.findByName(args[0]);
        if (group == null) {
            sender.sendMessage(ChatColor.RED + "操作失败。此组不存在。");
        } else {
            sender.sendMessage(ChatColor.GREEN + "操作成功。");
            group.clear();
            holder.remove(group);
        }
        return true;
    }
}
