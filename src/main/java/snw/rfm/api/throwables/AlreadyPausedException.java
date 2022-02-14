/*
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.api.throwables;

import snw.rfm.api.GameController;

/**
 * 当游戏进程实例已经被暂停但又一次调用 pause() 方法时引发此异常。
 *
 * @since 1.1.10
 * @see GameController#pause()
 */
public class AlreadyPausedException extends AlreadyException {
}
