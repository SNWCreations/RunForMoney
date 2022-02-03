package snw.rfm.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import snw.rfm.RunForMoney;
import snw.rfm.group.Group;
import snw.rfm.group.GroupHolder;

import java.util.HashSet;
import java.util.Set;

public final class TeamHolder {
    private final Set<Player> hunters = new HashSet<>();
    private final Set<Player> runners = new HashSet<>();
    private final Set<Player> enabledHunters = new HashSet<>();
    private static final TeamHolder INSTANCE = new TeamHolder();

    public boolean isRunner(Player player) {
        return runners.contains(player);
    }

    public boolean isHunter(Player player) {
        return hunters.contains(player);
    }

    public void addHunter(Player player) {
        hunters.add(player);
    }

    public void addRunner(Player player) {
        runners.add(player);
    }

    public void removeHunter(Player player) {
        hunters.remove(player);
        // 2022/2/1 修复移除猎人时未将其从所在组移除的错误。
        // 2022/2/2 改用 GroupHolder 内置方法。
        Group g = GroupHolder.getInstance().findByPlayer(player);
        if (g != null) {
            g.remove(player);
        }
    }

    public void removeRunner(Player player) {
        runners.remove(player);
    }

    public boolean isHunterEnabled(Player player) {
        return enabledHunters.contains(player);
    }

    public boolean isNoHunterFound() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (isHunter(p)) {
                return false;
            }
        }
        return true;
    }

    public boolean isNoRunnerFound() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (isRunner(p)) {
                return false;
            }
        }
        return true;
    }

    public void addEnabledHunter(Player player) {
        enabledHunters.add(player);
        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "你已被启用");
        player.setGameMode(GameMode.ADVENTURE);
    }

    public void removeEnabledHunter(Player player) {
        enabledHunters.remove(player);
        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "你已被禁用");
        RunForMoney.getInstance().getGameProcess().out(player);
    }

    public void cleanup() {
        hunters.clear();
        runners.clear();
        enabledHunters.clear();
    }

    public Set<Player> getHunters() {
        return hunters;
    }

    public Set<Player> getRunners() {
        return runners;
    }

    public static TeamHolder getInstance() {
        return INSTANCE;
    }
}
