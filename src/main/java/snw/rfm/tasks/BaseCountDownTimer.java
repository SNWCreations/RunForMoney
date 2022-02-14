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

import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.RunForMoney;

/**
 * 一个倒计时的实现。你可以继承这个类做倒计时。
 */
public abstract class BaseCountDownTimer extends BukkitRunnable {
    protected int secs;

    public BaseCountDownTimer(int secs) {
        if (secs <= 0) {
            throw new IllegalArgumentException("设定时间必须大于 0 秒。");
        }
        this.secs = secs;
    }

    /**
     * 启动此倒计时。
     */
    public void start() {
        super.runTaskTimer(RunForMoney.getInstance(), 20L, 20L);
    }

    @Override
    public final void run() {
        if (--secs > 0) { // 2022/2/3 v1.1.3 移动下减号，除掉个Bug。
            onNewSecond();
        } else {
            cancel();
            onZero();
        }
    }

    /**
     * 获取此计时器实例还有多少秒自动停止。
     * @return 剩余时间
     */
    protected int getTimeLeft() {
        return secs;
    }

    /**
     * 当计时器归零时调用此方法。
     */
    protected abstract void onZero();

    /**
     * 当计时器减少一秒时调用。(当计时器为 0 时或执行 start() 的一瞬间不调用)
     */
    protected abstract void onNewSecond();
}
