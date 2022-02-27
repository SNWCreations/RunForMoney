/*
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.game;

import org.apache.commons.lang.Validate;
import snw.rfm.RunForMoney;
import snw.rfm.tasks.BaseCountDownTimer;
import snw.rfm.tasks.CoinTimer;

import java.util.Map;

public final class GameController implements snw.rfm.api.GameController {
    private boolean isReversed = false;
    private boolean pause = false;
    private final GameProcess gameProcess;

    public GameController(GameProcess process) {
        Validate.notNull(process);
        this.gameProcess = process;
    }

    @Override
    public void setCoinPerSecond(int cps) throws IllegalArgumentException {
        if (cps == 0) {
            throw new IllegalArgumentException();
        }
        gameProcess.getTimers().stream().filter(IT -> IT instanceof CoinTimer).forEach(IT -> ((CoinTimer) IT).setCoinPerSecond(cps));
    }

    @Override
    public int getCoinPerSecond() {
        for (BaseCountDownTimer t : gameProcess.getTimers()) {
            if (t instanceof CoinTimer) {
                return ((CoinTimer) t).getCoinPerSecond();
            }
        }
        return 0;
    }

    @Deprecated
    @Override
    public void setGameReversed(boolean isGameReversed) {
        isReversed = isGameReversed;
        setCoinPerSecond(-getCoinPerSecond());
    }

    @Deprecated
    @Override
    public boolean isGameReversed() {
        return isReversed;
    }

    @Override
    public void pause() {
        pause = true;
        gameProcess.pause();
    }

    @Override
    public void resume() {
        gameProcess.resume();
    }

    @Override
    public boolean isPaused() {
        return pause;
    }

    @Override
    public void setHunterNoMoveTime(int time) {
        gameProcess.setHunterNoMoveTime(time);
    }

    @Override
    public void clearCoin() {
        Map<String, Double> coinEarned = RunForMoney.getInstance().getCoinEarned();
        for (Map.Entry<String, Double> kv : coinEarned.entrySet()) {
            if (TeamHolder.getInstance().isRunner(kv.getKey())) {
                kv.setValue(0.00);
            }
        }
    }
}
