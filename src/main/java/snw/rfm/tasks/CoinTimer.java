/**
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import snw.rfm.RunForMoney;
import snw.rfm.events.GameStopEvent;
import snw.rfm.game.TeamHolder;

import java.util.Map;

public final class CoinTimer extends BaseCountDownTimer {
    private final int coinPerSecond;
    private final Map<Player, Double> coinEarned;

    public CoinTimer(int secs, int coinPerSecond, Map<Player, Double> coinEarned) {
        super(secs);
        this.coinPerSecond = coinPerSecond;
        this.coinEarned = coinEarned;
    }

    @Override
    protected void onZero() {
        Bukkit.getPluginManager().callEvent(new GameStopEvent());
        RunForMoney.getInstance().getGameProcess().stop();
    }

    @Override
    protected void onNewSecond() {
        TeamHolder.getInstance().getRunners().forEach(IT -> coinEarned.put(IT, (coinEarned.getOrDefault(IT, 0.00)) + coinPerSecond));
    }
}
