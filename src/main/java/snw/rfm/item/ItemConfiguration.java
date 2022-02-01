package snw.rfm.item;

import org.bukkit.configuration.file.FileConfiguration;
import snw.rfm.RunForMoney;

public final class ItemConfiguration {
    private static ItemConfiguration INSTANCE;
    private final FileConfiguration config;

    public ItemConfiguration() {
        config = RunForMoney.getInstance().getConfig();
    }

    public String getItemType(String itemName) {
        return config.getString(itemName + "_type");
    }

    public int getItemTime(String itemName) {
        return config.getInt(itemName + "_time");
    }

    public String getExitingPickaxeMinableBlock() {
        return config.getString("exiting_pickaxe_minable_block", "minecraft:diamond_block");
    }

    public static ItemConfiguration getInstance() {
        return INSTANCE;
    }

    public static void init() {
        INSTANCE = new ItemConfiguration();
    }
}
