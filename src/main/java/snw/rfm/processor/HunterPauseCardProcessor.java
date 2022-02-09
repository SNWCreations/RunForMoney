/*
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.processor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import snw.rfm.api.ItemEventListener;
import snw.rfm.config.ItemConfiguration;
import snw.rfm.game.TeamHolder;

public final class HunterPauseCardProcessor implements ItemEventListener {
    @Override
    public boolean onPlayerUseRequiredItem(Player player) {
        TeamHolder th = TeamHolder.getInstance();
        if (th.isRunner(player)) { // 排除使用者是猎人从而导致猎人坑队友的情况，哈哈哈哈哈哈哈笑死我了
            int hpctime = ItemConfiguration.getItemTime("hpc") * 20; // 2022/2/6 避免重复计算
            Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " 使用了猎人暂停卡。猎人暂停了 " + (hpctime / 20) + " 秒。");
            for (Player p : th.getHunters()) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, hpctime, 255, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, hpctime, 129, false));
            }
            return true;
        }
        return false;
    }
}
