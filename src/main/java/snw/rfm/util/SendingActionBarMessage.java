/*
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.RunForMoney;

import java.util.Collection;

public final class SendingActionBarMessage extends BukkitRunnable {
    private final TextComponent text;
    private final Collection<? extends Player> playersToSend;
    private int ticked = 0;

    public SendingActionBarMessage(TextComponent textToSend, Collection<? extends Player> playersToSend) {
        Validate.notNull(textToSend);
        Validate.notNull(playersToSend);
        this.text = textToSend;
        this.playersToSend = playersToSend;
    }

    @Override
    public void run() {
        playersToSend.forEach((IT) -> IT.spigot().sendMessage(ChatMessageType.ACTION_BAR, text)); // 2022/2/6 用 Stream 优化。
        if (ticked++ >= 20) {
            cancel();
        }
    }

    public void start() {
        super.runTaskTimer(RunForMoney.getInstance(), 0L, 1L);
    }
}