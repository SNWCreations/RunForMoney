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

import snw.rfm.RunForMoney;

public final class ItemConfiguration {

    public static String getItemType(String itemName) {
        return RunForMoney.getInstance().getConfig().getString(itemName + "_type");
    }

    public static int getItemTime(String itemName) {
        return RunForMoney.getInstance().getConfig().getInt(itemName + "_time");
    }

    public static String getExitingPickaxeMinableBlock() {
        return RunForMoney.getInstance().getConfig().getString("exiting_pickaxe_minable_block", "minecraft:diamond_block");
    }
}
