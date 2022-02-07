/**
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static snw.rfm.Util.sortDescend;

public class ExportListCommand implements CommandExecutor {
    private final SimpleDateFormat SDF = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        RunForMoney rfm = RunForMoney.getInstance();
        Map<Player, Double> orginialCoinEarned = rfm.getCoinEarned();
        if (orginialCoinEarned.size() == 0) {
            sender.sendMessage(ChatColor.RED + "操作失败。B币榜为空！");
        } else {
            Map<Player, Double> sortedCoinEarned = sortDescend(orginialCoinEarned);
            String date = SDF.format(new Date());
            String fileName = rfm.getDataFolder().getAbsolutePath() + File.separator + date + ".txt";
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                writer.write("============ B币榜 ============");
                writer.newLine();
                writer.write("创建时间: " + date);
                writer.newLine();
                writer.newLine();
                int a = 0;
                for (Map.Entry<Player, Double> e : sortedCoinEarned.entrySet()) {
                    writer.write(++a + "." + e.getKey().getName() + ": " + e.getValue());
                    writer.newLine();
                }
                writer.close();
            } catch (IOException e) {
                sender.sendMessage(ChatColor.RED + "操作失败。尝试创建文件并写入数据时发生了异常，可能是没有权限或存储空间已满。");
                e.printStackTrace();
                return true;
            }
            sender.sendMessage(ChatColor.GREEN + "操作成功。文件已保存到 " + fileName);
        }
        return true;
    }
}
