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
import org.jetbrains.annotations.NotNull;
import snw.rfm.RunForMoney;
import snw.rfm.util.LanguageSupport;
import snw.rfm.util.PlaceHolderString;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ExportListCommand implements CommandExecutor {
    private final SimpleDateFormat SDF = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        RunForMoney rfm = RunForMoney.getInstance();
        Map<String, Double> coinEarned = rfm.getCoinEarned();
        if (coinEarned.size() == 0) {
            sender.sendMessage(ChatColor.RED + new PlaceHolderString("\\$commands.operation_failed\\$ \\$commands.coinlist.empty\\$").toString());
        } else {
            String date = SDF.format(new Date());
            String fileName = rfm.getDataFolder().getAbsolutePath() + File.separator + date + ".txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) { // 2022/2/7 改用 try-with-resource 结构
                // region 写入头
                writer.write(LanguageSupport.getTranslation("commands.coinlist.header"));
                writer.newLine();
                writer.write(LanguageSupport.getTranslation("commands.exportlist.created_time") + date);
                writer.newLine();
                writer.newLine();
                // endregion
                int a = 0;
                for (Map.Entry<String, Double> e : coinEarned.entrySet()) {
                    writer.write(++a + "." + e.getKey() + ": " + e.getValue()); // 写入排号，玩家名，B币数量
                    writer.newLine(); // 换行，否则数据会成一大坨。。
                }
            } catch (IOException e) {
                sender.sendMessage(ChatColor.RED + new PlaceHolderString("\\$commands.operation_failed\\$ \\$commands.exportlist.unable_to_export\\$").toString());
                e.printStackTrace();
                return true;
            }
            sender.sendMessage(ChatColor.GREEN + LanguageSupport.replacePlaceHolder("\\$commands.operation_success\\$ \\$commands.exportlist.unable_to_export\\$"));
        }
        return true;
    }
}
