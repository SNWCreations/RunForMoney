/*
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm.game;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import snw.rfm.RunForMoney;
import snw.rfm.config.GameConfiguration;
import snw.rfm.tasks.MainTimer;
import snw.rfm.tasks.ScheduledRFMTask;
import snw.rfm.tasks.ScheduledRFMTaskImpl;

import java.util.Map;

public final class GameController implements snw.rfm.api.GameController {
    private boolean isReversed = false;
    private boolean pause = false;
    private final GameProcess gameProcess;
    private int coinPerSecond;

    public GameController(GameProcess process, int coinPerSecond) {
        Validate.notNull(process);
        this.gameProcess = process;
        Validate.isTrue(coinPerSecond > 0); // 为什么不是 != 0 ? 总不能开局就倒扣 B币 吧，没 B币 可扣的!
        this.coinPerSecond = coinPerSecond;
    }

    @Override
    public void setCoinPerSecond(int cps) throws IllegalArgumentException {
        if (cps == 0) {
            throw new IllegalArgumentException();
        }
        coinPerSecond = cps;
    }

    @Override
    public int getCoinPerSecond() {
        return coinPerSecond;
    }

    @Deprecated
    @Override
    public void setGameReversed(boolean isGameReversed) {
        isReversed = isGameReversed;
        setCoinPerSecond(-getCoinPerSecond());
    }

    @Deprecated
    @Override
    public boolean isGameReversed() {
        return isReversed;
    }

    @Override
    public void pause() {
        pause = true;
        gameProcess.pause();
    }

    @Override
    public void resume() {
        gameProcess.resume();
    }

    @Override
    public boolean isPaused() {
        return pause;
    }

    @Override
    public void setHunterNoMoveTime(int time) {
        gameProcess.setHunterNoMoveTime(time);
    }

    @Override
    public void clearCoin() {
        Map<String, Double> coinEarned = RunForMoney.getInstance().getCoinEarned();
        for (Map.Entry<String, Double> kv : coinEarned.entrySet()) {
            if (TeamHolder.getInstance().isRunner(kv.getKey())) {
                kv.setValue(0.00);
            }
        }
    }

    @Override
    @Nullable
    public ScheduledRFMTask registerRemainingTimeEvent(int remaining, Runnable runnable) {
        if (gameProcess.getMainTimer().getTimeLeft() - (remaining * 60) < 0) {
            return null;
        }
        ScheduledRFMTaskImpl result = new ScheduledRFMTaskImpl(remaining, runnable, gameProcess.getMainTimer()); // 如果游戏剩余时间小于希望的时间，那么这个任务将永远没有机会被执行 (除非使用 executeItNow() 方法)
        gameProcess.getMainTimer().getTasks().add(result);
        return result;
    }

    @Override
    public boolean respawn(Player player) {
        Validate.notNull(player);
        if (!player.isOnline() || TeamHolder.getInstance().isRunner(player)) {
            return false;
        }
        player.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "你已被复活", "", 20, 40, 10);
        player.setGameMode(GameMode.ADVENTURE);
        TeamHolder.getInstance().getRunners().add(player.getName());
        Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + player.getName() + " 已被复活。");
        return true;
    }

    @Override
    public void removeRemainingTime(int secsToRemove) throws IllegalArgumentException {
        removeRemainingTime(secsToRemove, true);
    }

    @Override
    public void removeRemainingTime(int secsToRemove, boolean addCoin) throws IllegalArgumentException {
        Validate.isTrue(secsToRemove > 0);

        MainTimer mt = gameProcess.getMainTimer();
        mt.setRemainingTime(mt.getTimeLeft() - secsToRemove);
        if (addCoin) {
            Map<String, Double> ce = RunForMoney.getInstance().getCoinEarned();
            TeamHolder.getInstance().getRunners().forEach(IT -> ce.put(IT, ce.get(IT) + (secsToRemove * getCoinPerSecond())));
        }
    }

    @Override
    public void addMoney(Player player, double coin) {
        Validate.notNull(player);
        addMoney(player.getName(), coin);
    }

    @Override
    public void addMoney(String player, double coin) {
        Validate.notNull(player);
        Map<String, Double> ce = RunForMoney.getInstance().getCoinEarned();
        ce.put(player, ce.get(player) + coin);
    }

    @Override
    public void forceOut(Player player) throws IllegalStateException {
        Validate.notNull(player);
        if (!TeamHolder.getInstance().isRunner(player)) {
            throw new IllegalStateException();
        }

        TeamHolder.getInstance().removeRunner(player);
        gameProcess.out(player);
        gameProcess.checkStop();

        Map<String, Double> earned = RunForMoney.getInstance().getCoinEarned(); // 2022/2/2 有现成的 get 我不用。。。
        earned.put(player.getName(), earned.get(player.getName()) * GameConfiguration.getCoinMultiplierOnBeCatched());

        Location el = GameConfiguration.getEndRoomLocation();
        if (el != null) { // 如果管理员在设置里放置了错误或者不可读的位置 xyz ，就会导致获取到的位置为 null
            player.teleport(el); // 传送
        }
    }

    @Override
    public int getGameRemainingTime() {
        return gameProcess.getMainTimer().getTimeLeft();
    }
}
