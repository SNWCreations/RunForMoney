/**
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.group;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import snw.rfm.RunForMoney;
import snw.rfm.game.GameProcess;
import snw.rfm.game.TeamHolder;

import java.util.HashSet;

public final class Group extends HashSet<Player> {
    private final String name;

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void activate() {
        for (Player p : this) {
            TeamHolder.getInstance().addEnabledHunter(p);
        }
    }

    public void deactivate() {
        for (Player p : this) {
            TeamHolder.getInstance().removeEnabledHunter(p);
        }
    }

    @Override
    public void clear() {
        GameProcess process = RunForMoney.getInstance().getGameProcess();
        for (Player p : this) {
            p.sendMessage(ChatColor.RED + "你离开了你所在的组。如果你在不知情的情况下看到此消息，则可能是管理员的操作。");
            if (process != null) { // 2022/2/2 修复了在游戏开始前移出有玩家在内的组会导致玩家变为旁观者模式的错误。
                process.out(p);
            }
        }
        super.clear();
    }
}
