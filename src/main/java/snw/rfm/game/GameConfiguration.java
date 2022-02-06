/**
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;
import snw.rfm.RunForMoney;

public final class GameConfiguration {
    private static GameConfiguration INSTANCE;
    private final FileConfiguration config;
    private Location endRoomLocation;

    public GameConfiguration() {
        config = RunForMoney.getInstance().getConfig();

        // region 终止间相关处理
        String el = config.getString("end_room_location");
        if (el == null) {
            Bukkit.getConsoleSender().sendMessage("[RunForMoney] " + ChatColor.YELLOW + "警告: 终止间位置未正常加载。因为其值为空。");
            Bukkit.getPluginManager().disablePlugin(RunForMoney.getInstance());
        } else {
            endRoomLocation = null;
            try {
                endRoomLocation = parseXYZStringIntoLocation(el);
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("[RunForMoney] " + ChatColor.YELLOW + "警告: 终止间位置未正常加载。因为其值无效。");
            }
        }
        // endregion
    }

    private Location parseXYZStringIntoLocation(String loc) {
        String[] loc_split = loc.split(" ");
        return new Location(Bukkit.getWorld("world"), Integer.parseInt(loc_split[0]), Integer.parseInt(loc_split[1]), Integer.parseInt(loc_split[2]));
    }

    @Nullable
    public Location getEndRoomLocation() {
        return endRoomLocation;
    }

    public void setEndRoomLocation(Location END_ROOM) {
        this.endRoomLocation = END_ROOM;
    }

    public int getReleaseTime() {
        return config.getInt("hunter_release_time", 60);
    }

    public int getGameTime() {
        return config.getInt("game_time", 30);
    }

    public int getCoinPerSecond() {
        return config.getInt("coin_per_second", 100);
    }

    public int getLeastHunter() {
        return Math.max(config.getInt("least_hunter"), 1);
    }

    public int getLeastRunner() {
        return Math.max(config.getInt("least_runner"), 1);
    }

    public static GameConfiguration getInstance() {
        return INSTANCE;
    }

    public static void init() {
        INSTANCE = new GameConfiguration();
    }
}
