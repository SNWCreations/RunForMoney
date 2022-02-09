/**
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.api.events;

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
