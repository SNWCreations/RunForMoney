package snw.rfm.timers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import snw.rfm.RunForMoney;

import java.util.ArrayList;

public final class AFKNotifyTimer extends BaseCountDownTimer {
    private final ArrayList<Player> afkplayers;
    private final int timer;
    private Plugin plugin;

    public AFKNotifyTimer() {
        super(RunForMoney.getInstance().getGameConfiguration().getAFKTime() * 60);
        afkplayers = new ArrayList<>();
        timer = RunForMoney.getInstance().getGameConfiguration().getAFKTime() * 60;
    }

    @Override
    public void start(RunForMoney plugin) {
        this.plugin = plugin;
        afkplayers.addAll(plugin.getTeamHolder().getRunners());
        super.start(plugin);
    }

    @Override
    public void onZero() {
        secs = timer;
        RunForMoney rfm = RunForMoney.getInstance();

        for (Player i : afkplayers) {
            Location loc = i.getLocation();
            if (rfm.getGameConfiguration().isAFKLocationToHunter()) {
                for (Player h : rfm.getTeamHolder().getHunters()) {
                    h.sendMessage(ChatColor.RED + "玩家 " + i.getName() + " 正在挂机，其位置是 " + loc.getX() + " " + loc.getY() + " " + loc.getZ());
                }
            } else {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.isOp()) {
                        p.sendMessage(ChatColor.RED + "玩家 " + i.getName() + " 正在挂机，其位置是 " + loc.getX() + " " + loc.getY() + " " + loc.getZ());
                    }
                }
            }
        }

        start((RunForMoney) plugin);
    }

    @Override
    public void onNewSecond() {
    }

    public void removeAFKPlayer(Player player) {
        afkplayers.remove(player);
    }
}
