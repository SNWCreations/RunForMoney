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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import snw.rfm.RunForMoney;
import snw.rfm.Util;

public final class GameConfiguration {
    private static Location endRoomLocation;

    // 因为此方法中有些地方用到了 RunForMoney.getInstance() 方法，所以不能作为 static 块。否则会因为 INSTANCE 未被设置从而导致 NullPointerException 。
    public static void check() {

        // region 终止间相关处理
        String el = RunForMoney.getInstance().getConfig().getString("end_room_location");
        if (el == null) {
            Bukkit.getConsoleSender().sendMessage("[RunForMoney] " + ChatColor.YELLOW + "警告: 终止间位置未正常加载。因为其值为空。");
            Bukkit.getPluginManager().disablePlugin(RunForMoney.getInstance());
        } else {
            try {
                endRoomLocation = Util.parseXYZStringIntoLocation(el);
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("[RunForMoney] " + ChatColor.YELLOW + "警告: 终止间位置未正常加载。因为其值无效。");
            }
        }
        // endregion
    }

    @Nullable
    public static Location getEndRoomLocation() {
        return endRoomLocation;
    }

    public static void setEndRoomLocation(Location END_ROOM) {
        endRoomLocation = END_ROOM;
    }

    public static int getReleaseTime() {
        return RunForMoney.getInstance().getConfig().getInt("hunter_release_time", 60);
    }

    public static int getGameTime() {
        return RunForMoney.getInstance().getConfig().getInt("game_time", 30);
    }

    public static int getCoinPerSecond() {
        int result = RunForMoney.getInstance().getConfig().getInt("coin_per_second", 100);
        return (result > 0) ? result : 100;
    }

    public static int getLeastHunter() {
        return Math.max(RunForMoney.getInstance().getConfig().getInt("least_hunter"), 1);
    }

    public static int getLeastRunner() {
        return Math.max(RunForMoney.getInstance().getConfig().getInt("least_runner"), 1);
    }

    public static double getCoinMultiplierOnBeCatched() {
        return Math.max(RunForMoney.getInstance().getConfig().getInt("coin_multiplier_on_be_catched"), 0.1);
    }
}
