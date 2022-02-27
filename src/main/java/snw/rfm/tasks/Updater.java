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

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import snw.rfm.RunForMoney;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Updater extends Thread {
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
        } catch (JsonParseException e) {
            logFail("无法解析来自 Github API 的数据，因为返回的数据不是一个有效的 JSON");
            e.printStackTrace();
            return;
        }

        if (recievedVersion.startsWith("v")) {
            recievedVersion = recievedVersion.substring(1);
        }

        int versionDifference = getVersionDifference(recievedVersion);
        if (versionDifference == -1) {
            log("你的插件是旧版本的！当前版本: " + RunForMoney.getInstance().getDescription().getVersion() + " ，最新版本: " + recievedVersion);
        } else if (versionDifference == 0) {
            log("此插件是最新版！");
        } else if (versionDifference == 1) {
            log("你用的版本尚未发布！这可能是新版本的一个测试构建？");
        } else {
            logFail("内部代码返回了不可能的值，判断失败。判断方法返回了: " + versionDifference + " 。Github 上最后的版本是: " + recievedVersion + " ，现有版本是: " + RunForMoney.getInstance().getDescription().getVersion());
        }

    }

    private void log(Level level, String message) {
        Logger.getLogger("RunForMoney").log(level, "[RunForMoney] " + message);
    }

    private void log(String message) {
        log(Level.INFO, message);
    }

    private void logFail(String message) {
        log(Level.WARNING, "检查更新时出现错误: " + message + " 。");
    }

    // -1 = 过期
    // 0 = 最新版
    // 1 = 未来版
    private static int getVersionDifference(String versionToCompare) {
        String current = RunForMoney.getInstance().getDescription().getVersion();
        if (current.equals(versionToCompare))
            return 0;
        if (current.split("\\.").length != 3 || versionToCompare.split("\\.").length != 3)
            return -1;

        int curMaj = Integer.parseInt(current.split("\\.")[0]);
        int curMin = Integer.parseInt(current.split("\\.")[1]);
        String curPatch = current.split("\\.")[2];

        int relMaj = Integer.parseInt(versionToCompare.split("\\.")[0]);
        int relMin = Integer.parseInt(versionToCompare.split("\\.")[1]);
        String relPatch = versionToCompare.split("\\.")[2];

        if (curMaj < relMaj)
            return -1;
        if (curMaj > relMaj)
            return 1;
        if (curMin < relMin)
            return -1;
        if (curMin > relMin)
            return 1;

        // 以下比较是否是 SNAPSHOT 版，虽然我平常不发 SNAPSHOT 。。。但总要考虑
        int curPatchN = Integer.parseInt(curPatch.split("-")[0]);
        int relPatchN = Integer.parseInt(relPatch.split("-")[0]);
        if (curPatchN < relPatchN)
            return -1;
        if (curPatchN > relPatchN)
            return 1;
        if (!relPatch.contains("-") && curPatch.contains("-"))
            return -1;
        if (relPatch.contains("-") && curPatch.contains("-"))
            return 0;

        return 1;
    }
}
