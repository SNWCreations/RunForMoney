package snw.rfm.events;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import snw.rfm.RunForMoney;

import java.util.Set;

/**
 * 游戏停止事件。
 */
public final class GameStopEvent extends BaseEvent {
    private final GameStopType type;

    public GameStopEvent(GameStopType type) {
        this.type = type;
    }

    /**
     * 获取游戏停止的原因。
     *
     * @return GameStopType 枚举的一个成员
     */
    public GameStopType getStopType() {
        return type;
    }

    /**
     * 获取获胜者（可能有很多）。当 getStopType() 返回的值不是 GameStopType.RUNNER_WON 时，此方法返回 null 。
     * 其本质是 RunForMoney.getInstance().getGameProcess().getRunners() 。
     *
     * @return 获胜者，如果有。
     */
    @Nullable
    public Set<Player> getWinner() {
        Set<Player> p = RunForMoney.getInstance().getTeamHolder().getRunners();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }
}
