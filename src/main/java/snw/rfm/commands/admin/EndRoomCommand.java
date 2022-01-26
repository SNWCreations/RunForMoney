package snw.rfm.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.game.GameConfiguration;


public final class EndRoomCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        GameConfiguration conf = RunForMoney.getInstance().getGameConfiguration();
        if (args.length == 0 && sender instanceof Player) {
            conf.setEndRoomLocation(((Player) sender).getLocation());
        } else if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "参数不足！");
            return false;
        } else {
            try {
                conf.setEndRoomLocation(new Location(Bukkit.getWorld("world"), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])));
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
