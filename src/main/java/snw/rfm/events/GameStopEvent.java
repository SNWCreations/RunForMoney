package snw.rfm.events;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import snw.rfm.game.TeamHolder;

import java.util.Set;

/**
 * 游戏停止事件。
 */
public final class GameStopEvent extends BaseEvent {
    /**
     * 获取获胜者集合。
     * <p>
     * 其本质是 TeamHolder.getInstance().getRunners() 。
     *
     * @return 获胜者集合。
     */
    @Nullable
    public Set<Player> getWinner() {
        return TeamHolder.getInstance().getRunners();
    }
}
