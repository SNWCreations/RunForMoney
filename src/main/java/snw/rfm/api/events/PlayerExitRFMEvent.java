/**
 * This file is part of RunForMoney-API.
 *
 * RunForMoney-API is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney-API is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney-API. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.config.GameConfiguration;

public final class PlayerExitRFMEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    public PlayerExitRFMEvent(Player whoExited) {
        super(whoExited);
    }

    @Deprecated
    public Player getWhoExited() {
        return getPlayer();
    }

    public double getCoinEarned(boolean multiplier) {
        double result = RunForMoney.getInstance().getCoinEarned().get(getPlayer().getName());
        return (multiplier) ? (result * GameConfiguration.getCoinMultiplierOnBeCatched()) : result;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
