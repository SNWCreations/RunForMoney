package snw.rfm.events;

import org.bukkit.entity.Player;

/**
 * 玩家弃权事件。
 */
public final class PlayerExitRFMEvent extends BaseEvent {
    private final Player whoExited;

    public PlayerExitRFMEvent(Player whoExited) {
        this.whoExited = whoExited;
    }

    /**
     * 获取弃权者。
     *
     * @return 弃权者
     */
    public Player getWhoExited() {
        return whoExited;
    }
}
