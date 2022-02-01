package snw.rfm.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import snw.rfm.game.GameConfiguration;


public final class EndRoomCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        GameConfiguration conf = GameConfiguration.getInstance();
        if (args.length == 0 && sender instanceof Player) {
            conf.setEndRoomLocation(((Player) sender).getLocation()); // 如果执行者是玩家，但没有提供参数，就把执行者所在位置设置为终止间
        } else if (args.length < 3) { // 参数不够改个毛线哦
            sender.sendMessage(ChatColor.RED + "参数不足！");
            return false;
        } else { // 如果提供了足够参数
            try {
                conf.setEndRoomLocation(new Location((sender instanceof Player) ? ((Player) sender).getWorld() : Bukkit.getWorld("world"), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]))); // 2022/1/31 进行细节优化，如果执行者是玩家，终止间的位置将会位于执行者所在世界。
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "操作失败。提供的位置有误（可能存在非数字，请确定是否为整数）。");
                e.printStackTrace();
                return true;
            }
        }
        sender.sendMessage(ChatColor.GREEN + "操作成功。");
        return true;
    }
}
