/*
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.tasks;

import snw.rfm.RunForMoney;
import snw.rfm.game.GameProcess;

public final class DelayedTimer extends BaseCountDownTimer {
    private final BaseCountDownTimer dt;
    private final GameProcess process;

    public DelayedTimer(int secs, BaseCountDownTimer delayedTimerToStart, GameProcess processToBind) {
        super(secs);
        this.dt = delayedTimerToStart;
        this.process = processToBind;
    }

    @Override
    protected void onZero() {
        dt.start(RunForMoney.getInstance());
        process.getTimers().add(dt);
        process.getTimers().remove(this);
    }

    @Override
    protected void onNewSecond() {
    }
}
