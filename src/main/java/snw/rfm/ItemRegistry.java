/*
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm;

import org.bukkit.inventory.ItemStack;
import snw.rfm.api.ItemEventListener;

import java.util.*;

/**
 * 物品事件注册表。
 *
 * @see snw.rfm.api.ItemEventListener
 */
public final class ItemRegistry {
    private static final Map<ItemStack, List<ItemEventListener>> map = new HashMap<>();

    /**
     * 此方法用于注册一个对特定物品的监听器。
     * @param item 将被监听的物品
     * @param listener 监听器实例
     */
    public static void register(ItemStack item, ItemEventListener listener) {
        if (map.containsKey(item)) {
            map.get(item).add(listener);
        } else {
            map.put(item, new ArrayList<>(Collections.singletonList(listener)));
        }
    }

    /**
     * 获取对特定物品的监听器。
     * @param item 用于查询的物品实例
     * @return 所有针对目标物品的监听器实例 (列表可以为空) 。
     */
    public static List<ItemEventListener> getByItem(ItemStack item) {
        List<ItemEventListener> result;
        return (result = map.get(item)) == null ? new ArrayList<>() : result;
    }

}
