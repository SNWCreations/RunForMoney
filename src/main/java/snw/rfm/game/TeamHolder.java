/**
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import snw.rfm.group.GroupHolder;
import snw.rfm.util.LanguageSupport;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class TeamHolder {
    private final Set<String> hunters = new HashSet<>();
    private final Set<String> runners = new HashSet<>();
    private final Set<String> out = new HashSet<>();
    private String giveUp = null;
    private final Set<String> enabledHunters = new HashSet<>();
    private static final TeamHolder INSTANCE = new TeamHolder();

    /*
     * TeamHolder 只能有一个实例，这个唯一实例可以通过 getInstance() 获取。
     */
    private TeamHolder() {
        if (INSTANCE != null) {
            throw new UnsupportedOperationException();
        }
    }

    public boolean isRunner(Player player) {
        return runners.contains(player.getName());
    }

    public boolean isHunter(Player player) {
        return isHunter(player.getName());
    }

    public boolean isHunter(String player) {
        return hunters.contains(player);
    }

    public boolean isRunner(String player) {
        return runners.contains(player);
    }

    public boolean isNotInGame(Player player) {
        return isNotInGame(player.getName());
    }

    public boolean isNotInGame(String player) {
        return !hunters.contains(player) && !runners.contains(player);
    }

    public void addHunter(Player player) {
        if (isRunner(player)) {
            removeRunner(player);
            player.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.team.hunter.leave_runner_team"));
        }
        hunters.add(player.getName());
    }

    public void addRunner(Player player) {
        if (isHunter(player)) {
            removeHunter(player);
            player.sendMessage(ChatColor.GREEN + LanguageSupport.getTranslation("commands.team.runner.leave_hunter_team"));
        }
        runners.add(player.getName());
    }

    public void removeHunter(Player player) {
        hunters.remove(player.getName());
        // 2022/2/1 修复移除猎人时未将其从所在组移除的错误。
        // 2022/2/2 改用 GroupHolder 内置方法。
        Optional.ofNullable(GroupHolder.getInstance().findByPlayer(player))
                .ifPresent(IT -> IT.remove(player.getName()));
    }

    public void removeRunner(Player player) {
        runners.remove(player.getName());
    }

    public boolean isHunterEnabled(Player player) {
        return isHunterEnabled(player.getName());
    }

    public boolean isHunterEnabled(String player) {
        if (!isHunter(player)) {
            return false;
        }
        return enabledHunters.contains(player);
    }

    public boolean isNoHunterFound() {
        return Bukkit.getOnlinePlayers().stream().noneMatch(this::isHunter);
    }

    public boolean isNoRunnerFound() {
        return Bukkit.getOnlinePlayers().stream().noneMatch(this::isRunner);
    }

    public void addEnabledHunter(Player player) {
        addEnabledHunter(player.getName()); // 2022/4/26 修复了未检查玩家是不是猎人的问题
        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + LanguageSupport.getTranslation("event.hunter_activated"));
        player.setGameMode(GameMode.ADVENTURE);
    }

    public void addEnabledHunter(String player) {
        if (!isHunter(player)) {
            throw new IllegalStateException(); // you can't enable a non-hunter player.
        }
        enabledHunters.add(player);
    }

    public void setGiveUpPlayer(String player) {
        if (giveUp != null && player != null) { // for some mission, we need to set it to null
            return; // You cannot set another player when giveUp is not null.
        }
        giveUp = player;
        runners.remove(player); // 2022/4/23 if you give up, you should not in the runner team.
    }

    public void addOutPlayer(Player player) {
        addOutPlayer(player.getName());
    }

    public void removeOutPlayer(Player player) {
        removeOutPlayer(player.getName());
    }

    public void addOutPlayer(String player) {
        runners.remove(player);
        out.add(player);
    }

    public void removeOutPlayer(String player) {
        out.remove(player);
    }

    public void removeEnabledHunter(Player player) {
        enabledHunters.remove(player.getName());
        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + LanguageSupport.getTranslation("event.runner_activated"));
        player.setGameMode(GameMode.SPECTATOR);
        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
    }

    public void removeEnabledHunter(String player) {
        enabledHunters.remove(player);
    }

    public void cleanup() {
        hunters.clear();
        runners.clear();
        enabledHunters.clear();
        out.clear();
        giveUp = null;
    }

    public Set<String> getHunters() {
        return hunters;
    }

    public Set<String> getRunners() {
        return runners;
    }

    public Set<String> getOutPlayers() {
        return out;
    }

    public String getGiveUpPlayer() {
        return giveUp;
    }

    public static TeamHolder getInstance() {
        return INSTANCE;
    }
}
