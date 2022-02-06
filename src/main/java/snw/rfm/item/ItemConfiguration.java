/**
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

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
