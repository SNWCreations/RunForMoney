package snw.rfm.group;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import snw.rfm.RunForMoney;
import snw.rfm.game.TeamHolder;

import java.util.HashSet;

public final class Group extends HashSet<Player> {
    private final String name;

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void activate() {
        for (Player p : this) {
            TeamHolder.getInstance().addEnabledHunter(p);
        }
    }

    public void deactivate() {
        for (Player p : this) {
            TeamHolder.getInstance().removeEnabledHunter(p);
        }
    }

    @Override
    public void clear() {
        for (Player p : this) {
            p.sendMessage(ChatColor.RED + "你离开了你所在的组。如果你在不知情的情况下看到此消息，则可能是管理员的操作。");
            RunForMoney.getInstance().getGameProcess().out(p);
        }
        super.clear();
    }
}
