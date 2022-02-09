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
