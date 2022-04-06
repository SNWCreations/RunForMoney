/*
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import snw.rfm.RunForMoney;
import snw.rfm.group.Group;
import snw.rfm.group.GroupHolder;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public final class Preset {
    private static final Set<String> runners = new HashSet<>();
    private static final Set<String> hunters = new HashSet<>();
    private static final Map<String, Group> player_not_joined_groups = new HashMap<>();

    private Preset() {
        throw new UnsupportedOperationException("No snw.rfm.config.Preset instances for you!");
    }

    public static void init() {
        runners.clear();
        hunters.clear();
        player_not_joined_groups.clear();

        RunForMoney rfm = RunForMoney.getInstance();
        Logger l = rfm.getLogger();
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(new File(rfm.getDataFolder(), "presets.yml"));

        if (conf.getBoolean("IS_TEMPLATE")) {
            l.warning("注意: 检测到预设的 IS_TEMPLATE 值为 true ，预设不会加载。");
            return;
        }

        l.info("加载预设...");
        List<String> runners_ = conf.getStringList("runners");
        List<String> hunters_ = conf.getStringList("hunters");

        if (runners_.isEmpty()) {
            l.warning("runners 为空！");
        }
        if (hunters_.isEmpty()) {
            l.warning("hunters 为空！");
            if (runners_.isEmpty()) {
                l.warning("runners 项和 hunters 项均为空，预设无法加载。");
                return;
            }
        }


        for (String i : runners_) {
            runners.add(i.toLowerCase());
        }

        for (String i : hunters_) {
            hunters.add(i.toLowerCase());
        }

        Set<String> invalid = new HashSet<>();
        for (String i : runners) {
            if (hunters.contains(i)) {
                l.warning("检测到玩家名 " + i + " 在预设中重复，因此对该玩家的预设无效。");
                invalid.add(i);
            }
        }

        for (String s : invalid) { // 防止万恶的 ConcurrentModificationException
            hunters.remove(s);
            runners.remove(s);
        }

        ConfigurationSection groups = conf.getConfigurationSection("groups");
        if (groups == null) {
            l.info("groups 项不存在，将不会预设组。");
        } else {
            Set<String> gk = groups.getKeys(false);
            for (String k : gk) {
                Group groupWillBeCreated = new Group(k);
                GroupHolder.getInstance().add(groupWillBeCreated);
                List<String> willBeProcessed = groups.getStringList(k);
                for (String v : willBeProcessed) {
                    if (hunters.contains(v)) {
                        player_not_joined_groups.put(v, groupWillBeCreated);
                    }
                }
            }
            l.info("创建了 " + gk.toArray().length + " 个组。");
        }

        l.info("预设加载完成。");
    }

    public static boolean isPresetHunter(Player player) {
        return hunters.contains(player.getName().toLowerCase());
    }

    public static boolean isPresetRunner(Player player) {
        return runners.contains(player.getName());
    }

    @Nullable
    public static Group getPlayerNotJoinedGroup(Player player) {
        return player_not_joined_groups.get(player.getName());
    }
}
