package snw.rfm.game;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import snw.rfm.RunForMoney;

import java.util.HashSet;
import java.util.Set;

public final class TeamHolder {
    private final Set<Player> hunters;
    private final Set<Player> runners;
    private final Set<Player> enabledHunters;

    public TeamHolder() {
        hunters = new HashSet<>();
        runners = new HashSet<>();
        enabledHunters = new HashSet<>();
    }

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
    }

    public void removeRunner(Player player) {
        runners.remove(player);
    }

    public boolean isHunterEnabled(Player player) {
        return enabledHunters.contains(player);
    }

    public boolean isNoHunterFound() {
        if (hunters.isEmpty()) {
            return true;
        }
        int no_online = 0;
        for (Player i : hunters) {
            if (!i.isOnline()) {
                no_online++;
            }
        }
        return no_online == hunters.toArray().length;
    }

    public boolean isNoRunnerFound() {
        if (runners.isEmpty()) {
            return true;
        }
        int no_online = 0;
        for (Player i : runners) {
            if (!i.isOnline()) {
                no_online++;
            }
        }
        return no_online == runners.toArray().length;
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
}
