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

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;
import snw.rfm.RunForMoney;
import snw.rfm.game.GameProcess;

public final class DelayedTimer extends BaseCountDownTimer {
    private final GameProcess process;
    private final Runnable runnable;

    public DelayedTimer(int secs, BaseCountDownTimer delayedTimerToStart, GameProcess processToBind) {
        super(secs);
        Validate.notNull(delayedTimerToStart);
        Validate.notNull(processToBind);
        this.runnable = () -> {
            delayedTimerToStart.start(RunForMoney.getInstance());
            processToBind.addTimer(delayedTimerToStart);
            processToBind.getTimers().remove(this);
        };
        this.process = processToBind;
    }

    public DelayedTimer(int secs, Runnable runnable, GameProcess processToBind) {
        super(secs);
        Validate.notNull(runnable);
        Validate.notNull(processToBind);
        this.runnable = runnable;
        this.process = processToBind;
    }

    @Override
    public void start(Plugin plugin) {
        process.addTimer(this);
        super.start(plugin);
    }

    @Override
    protected void onZero() {
        runnable.run();
    }

    @Override
    protected void onNewSecond() {
    }
}
