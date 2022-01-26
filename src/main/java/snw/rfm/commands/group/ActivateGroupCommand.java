package snw.rfm.commands.group;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.group.Group;

public final class ActivateGroupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "参数不足或过多！");
            return false;
        }
        if (RunForMoney.getInstance().getGameProcess() == null) {
            sender.sendMessage(ChatColor.RED + "操作失败。游戏未运行。");
            return true;
        }
        Group group = RunForMoney.getInstance().getGroups().findByName(args[0]);
        if (group == null) {
            sender.sendMessage(ChatColor.RED + "操作失败。此组不存在。");
        } else {
            group.activate();
            sender.sendMessage(ChatColor.GREEN + "操作成功。");
        }
        return true;
    }
}
