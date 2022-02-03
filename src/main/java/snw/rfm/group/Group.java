package snw.rfm.group;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import snw.rfm.RunForMoney;
import snw.rfm.game.GameProcess;
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
        GameProcess process = RunForMoney.getInstance().getGameProcess();
        for (Player p : this) {
            p.sendMessage(ChatColor.RED + "你离开了你所在的组。如果你在不知情的情况下看到此消息，则可能是管理员的操作。");
            if (process != null) { // 2022/2/2 修复了在游戏开始前移出有玩家在内的组会导致玩家变为旁观者模式的错误。
                process.out(p);
            }
        }
        super.clear();
    }
}
