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

import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snw.rfm.api.ItemEventListener;
import snw.rfm.api.throwables.ItemAlreadyRegisteredException;

import java.util.*;

/**
 * 物品事件注册表。
 *
 * @see snw.rfm.api.ItemEventListener
 * @since 1.1.6
 */
public final class ItemRegistry {
    private static final Map<ItemStack, List<ItemEventListener>> map = new HashMap<>();
    private static final Map<String, ItemStack> registeredItems = new HashMap<>();

    /**
     * 此方法用于注册一个道具。
     * <p>
     * 注意: 针对一个道具物品实例，可能有多个内部名称与其对应。
     *
     * @param item 将被注册的道具实例
     * @throws ItemAlreadyRegisteredException 提供的内部名称或道具实例已被注册时引发
     * @since 1.1.8
     */
    public static void registerItem(@NotNull String name, @NotNull ItemStack item) {
        Validate.notNull(name, "内部名称不可为 null");
        Validate.notNull(item, "注册的道具实例不可为 null");
        if (registeredItems.get(name) != null) {
            throw new ItemAlreadyRegisteredException();
        }
        ItemStack processed = item.clone();
        processed.setAmount(1);
        registeredItems.put(name, processed);
    }

    /**
     * 此方法用于注册一个道具。
     * <p>
     * 这个重载版可以视为 这个方法的原始版 + registerItemEvent 方法 。
     *
     * @param item 将被注册的道具实例
     * @throws ItemAlreadyRegisteredException 提供的内部名称已被注册时引发
     * @since 1.1.8
     */
    public static void registerItem(@NotNull String name, @NotNull ItemStack item, @NotNull ItemEventListener listener) {
        registerItem(name, item);
        registerItemEvent(item, listener);
    }

    /**
     * 此方法用于注册一个对特定道具的监听器。
     * @param item 将被监听的物品
     * @param listener 监听器实例
     * @since 1.1.6
     */
    public static void registerItemEvent(@NotNull ItemStack item, @NotNull ItemEventListener listener) {
        Validate.notNull(item, "道具实例不可为 null");
        Validate.notNull(listener, "监听器实例不可为 null");
        ItemStack processed = item.clone();
        processed.setAmount(1);
        if (map.containsKey(processed)) {
            map.get(processed).add(listener);
        } else {
            map.put(processed, new ArrayList<>(Collections.singletonList(listener)));
        }
    }

    /**
     * 获取对特定道具的监听器。
     * @param item 用于查询的道具实例
     * @return 所有针对目标道具的监听器实例 (列表内可能无元素) 。
     * @since 1.1.6
     */
    @NotNull
    public static List<ItemEventListener> getProcessorByItem(@NotNull ItemStack item) {
        Validate.notNull(item, "道具实例不可为 null");
        ItemStack processed = item.clone();
        processed.setAmount(1);
        List<ItemEventListener> result;
        return (result = map.get(processed)) == null ? new ArrayList<>() : result;
    }

    /**
     * 获取特定内部名称指定的道具的监听器。
     * <p>
     * 本质是先获取道具实例再获取监听器实例列表。
     *
     * @param name 道具的内部名称
     */
    @SuppressWarnings("unused")
    @NotNull
    public static List<ItemEventListener> getProcessorByName(@NotNull String name) {
        ItemStack requestedItem = getRegisteredItemByName(name);
        Validate.notNull(requestedItem, "内部名称并未注册");
        return getProcessorByItem(requestedItem);
    }

    /**
     * 获得所有已注册道具的内部名称。
     * @return 所有已注册道具的内部名称
     */
    @NotNull
    public static Set<String> getRegisteredItemNames() {
        return registeredItems.keySet();
    }

    /**
     * 用内部名称获取某个道具物品的实例。
     * @param name 目标物品的内部名称
     * @return 目标物品 (可能因不存在返回 null)
     */
    @Nullable
    public static ItemStack getRegisteredItemByName(@NotNull String name) {
        Validate.notNull(name, "内部名称不可为 null");
        return registeredItems.get(name);
    }
}
