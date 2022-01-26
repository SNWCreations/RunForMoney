package snw.rfm.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import snw.rfm.RunForMoney;

public final class GameConfiguration {
    private Location endRoomLocation;
    private final int releaseTime;
    private final int gameTime;
    private final int coinPerSecond;
    final FileConfiguration config;
    private final String EPMB;

    public GameConfiguration() {
        config = RunForMoney.getInstance().getConfig();
        String el = config.getString("end_room_location");
        releaseTime = config.getInt("hunter_release_time", 60);
        gameTime = config.getInt("game_time", 30);
        coinPerSecond = config.getInt("coin_per_second", 100);
        EPMB = config.getString("exiting_pickaxe_minable_block", "diamond_block");

        if (el == null) {
            disableThisPluginBecause("错误: end_room_location 为空，插件无法加载。");
            return;
        }
        endRoomLocation = parseXYZStringIntoLocation(el);
        if (endRoomLocation == null) {
            disableThisPluginBecause("错误: 加载位置时出现问题，插件无法加载。");
        }
    }

    private Location parseXYZStringIntoLocation(String loc) {
        String[] loc_split = loc.split(" ");
        if (loc_split.length != 3) {
            return null;
        }
        return new Location(Bukkit.getWorld("world"), Integer.parseInt(loc_split[0]), Integer.parseInt(loc_split[1]), Integer.parseInt(loc_split[2]));
    }

    private void disableThisPluginBecause(String reason) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + reason);
        Bukkit.getPluginManager().disablePlugin(RunForMoney.getInstance());
    }

    public Location getEndRoomLocation() {
        return endRoomLocation;
    }

    public void setEndRoomLocation(Location END_ROOM) {
        this.endRoomLocation = END_ROOM;
        config.set("end_room_location", ((int) END_ROOM.getX()) + " " + ((int) END_ROOM.getY()) + " " + ((int) END_ROOM.getZ()));
    }

    public int getReleaseTime() {
        return releaseTime;
    }

    public int getAFKTime() {
        return config.getInt("afk_timer", 3);
    }

    public boolean isAFKLocationToHunter() {
        return config.getBoolean("afk_loc_to_hunter", false);
    }

    public String getExitingPickaxeMinableBlock() {
        return EPMB;
    }

    public int getGameTime() {
        return gameTime;
    }

    public int getCoinPerSecond() {
        return coinPerSecond;
    }
}
