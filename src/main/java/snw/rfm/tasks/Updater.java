/*
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.tasks;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.RunForMoney;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Updater extends BukkitRunnable {
    @Override
    public void run() {
        log("正在检查更新...");
        StringBuilder response;
        try {
            URL url = new URL("https://api.github.com/repos/SNWCreations/RunForMoney/releases/latest");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            logFail("连接 Github API 或 读取数据 时出现错误");
            e.printStackTrace();
            return;
        }

        String recievedVersion;
        try {
            recievedVersion = new JsonParser().parse(response.toString()).getAsJsonObject().get("name").getAsString();
        } catch (JsonSyntaxException e) {
            logFail("无法解析来自 Github API 的数据，因为返回的数据不是一个有效的 JSON");
            e.printStackTrace();
            return;
        }

        if (recievedVersion.startsWith("v")) {
            recievedVersion = recievedVersion.substring(1);
        }
        
        String thisVersionButItIsString = RunForMoney.getInstance().getDescription().getVersion();
        int[] latestVersion = parseVersionStringIntoArray(recievedVersion);
        int[] thisVersion = parseVersionStringIntoArray(thisVersionButItIsString);

        if (latestVersion[0] > thisVersion[0] || latestVersion[1] > thisVersion[1] || latestVersion[2] > thisVersion[2]) {
            log("你的插件是旧版本的！当前版本: " + thisVersionButItIsString + " ，最新版本: " + recievedVersion);
        } else if (Arrays.equals(latestVersion, thisVersion)) {
            log("此插件是最新版！");
        } else {
            log("你用的版本似乎是未来版本！这可能是新版本的一个测试构建？");
        }

    }

    private void log(Level level, String message) {
        Logger.getLogger("RunForMoney").log(level, "RunForMoney - 更新检查: " + message);
    }

    private void log(String message) {
        log(Level.INFO, message);
    }

    private void logFail(String message) {
        log(Level.WARNING, "检查更新时出现错误: " + message + " 。");
    }

    private int[] parseVersionStringIntoArray(String versionString) throws NumberFormatException {
        String[] splited_version = versionString.split("\\.");
        return new int[]{Integer.parseInt(splited_version[0]), Integer.parseInt(splited_version[1]), Integer.parseInt(splited_version[2])};
    }
}
