/*
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.api;

import snw.rfm.RunForMoney;
import snw.rfm.api.throwables.AlreadyPausedException;
import snw.rfm.api.throwables.AlreadyRunningException;

/**
 * 游戏控制器。可以对游戏进行一些修改。
 *
 * @since 1.1.10
 * @see RunForMoney#getGameController()
 */
public interface GameController {
    /**
     * 设置每秒增加的B币数量。
     * @param cps 每秒增加的B币数量
     * @since 1.1.10
     */
    void setCoinPerSecond(int cps);

    /**
     * 获取每秒增加的B币数量。
     * @return 每秒增加的B币数量
     * @since 1.1.10
     */
    int getCoinPerSecond();

    /**
     * 设置时间是否倒流。
     * @param isGameReversed 时间是否倒流
     * @since 1.1.10
     */
    void setGameReversed(boolean isGameReversed);

    /**
     * 获取时间是否正在倒流。
     * @return 时间是否正在倒流
     * @since 1.1.10
     */
    boolean isGameReversed();

    /**
     * 暂停游戏。
     *
     * @throws AlreadyPausedException 当游戏进程实例已经被暂停时又一次调用此方法时引发此异常。
     * @since 1.1.10
     */
    void pause() throws AlreadyPausedException;

    /**
     * 继续游戏。
     *
     * @since 1.1.10
     */
    void resume() throws AlreadyRunningException;

    /**
     * 获取游戏是否已经暂停。
     * @return 游戏是否已经暂停
     */
    boolean isPaused();
}
