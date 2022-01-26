package snw.rfm.timers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import snw.rfm.RunForMoney;
import snw.rfm.events.GameStopEvent;
import snw.rfm.events.GameStopType;

import java.util.Map;

public final class CoinTimer extends BaseCountDownTimer {
    private final int coinPerSecond;
    private final Map<Player, Double> coinEarned;

    public CoinTimer(int secs, int coinPerSecond, Map<Player, Double> coinEarned) {
        super(secs);
        this.coinPerSecond = coinPerSecond;
        this.coinEarned = coinEarned;
    }

    @Override
    public void onZero() {
        Bukkit.getPluginManager().callEvent(new GameStopEvent(GameStopType.RUNNER_WON));
        RunForMoney.getInstance().getGameProcess().stop();
    }

    @Override
    public void onNewSecond() {
        for (Player i : RunForMoney.getInstance().getTeamHolder().getRunners()) {
            coinEarned.put(i, coinEarned.get(i) + coinPerSecond);
        }
    }

    @Override
    public void start(RunForMoney plugin) {
        for (Player i : plugin.getTeamHolder().getRunners()) {
            coinEarned.put(i, 0.00);
        }
        super.start(plugin);
    }

    public Map<Player, Double> getCoinEarned() {
        return coinEarned;
    }
}
