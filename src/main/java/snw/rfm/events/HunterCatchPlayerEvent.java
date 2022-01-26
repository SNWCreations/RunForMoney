package snw.rfm.events;

import org.bukkit.entity.Player;

/**
 * 猎人抓到一个玩家时触发此事件。
 */
public final class HunterCatchPlayerEvent extends BaseEvent {
    private final Player whoBeCatched;
    private final Player catcher;
    private final int playerRemaining;

    public HunterCatchPlayerEvent(Player catched, Player catcher, int playerRemaining) {
        this.whoBeCatched = catched;
        this.catcher = catcher;
        this.playerRemaining = playerRemaining;
    }

    /**
     * 获取被抓捕者。
     *
     * @return 被抓捕者
     */
    public Player getWhoBeCatched() {
        return whoBeCatched;
    }

    /**
     * 获取抓捕者。
     *
     * @return 抓捕者
     */
    public Player getCatcher() {
        return catcher;
    }

    /**
     * 获取剩余的玩家数。
     *
     * @return 剩余的玩家数
     */
    public int getPlayerRemaining() {
        return playerRemaining;
    }
}
