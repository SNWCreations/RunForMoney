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
     * 获取获胜者（可能有很多）。当 getStopType() 返回的值不是 GameStopType.RUNNER_WON 时，此方法返回 null 。
     * 其本质是 RunForMoney.getInstance().getGameProcess().getRunners() 。
     *
     * @return 获胜者，如果有。
     */
    @Nullable
    public Set<Player> getWinner() {
        Set<Player> p = TeamHolder.getInstance().getRunners();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }
}
