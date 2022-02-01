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
