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
import org.jetbrains.annotations.NotNull;

public final class ScheduledRFMTaskImpl extends BaseCountDownTimer implements ScheduledRFMTask {
    private final Runnable runnableToCall;
    private boolean executed = false;

    public ScheduledRFMTaskImpl(int secs, @NotNull Runnable runnable) {
        super(secs);
        Validate.notNull(runnable);
        runnableToCall = runnable;
    }

    @Override
    protected void onZero() {
        executed = true;
        runnableToCall.run();
    }

    @Override
    protected void onNewSecond() {

    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    @Override
    public int getRemainingTime() {
        return getTimeLeft();
    }

    @Override
    public void executeItNow() throws IllegalStateException {
        if (executed) {
            throw new IllegalStateException();
        }
        cancel();
        onZero();
    }
}
