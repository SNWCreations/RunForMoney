package snw.rfm.events;

/**
 * 游戏结束的类型。
 */
public enum GameStopType {
    /**
     * 逃走队员胜利。这意味着有人存活。
     */
    RUNNER_WON,

    /**
     * 猎人胜利，这意味着所有逃走队员均被抓捕。
     */
    HUNTER_WON,

    /**
     * 管理员用指令终止游戏。
     */
    ADMIN_TERMINATE_GAME
}
