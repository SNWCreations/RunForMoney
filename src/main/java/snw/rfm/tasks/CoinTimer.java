package snw.rfm.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import snw.rfm.RunForMoney;
import snw.rfm.events.GameStopEvent;
import snw.rfm.game.TeamHolder;

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
    protected void onZero() {
        Bukkit.getPluginManager().callEvent(new GameStopEvent());
        RunForMoney.getInstance().getGameProcess().stop();
    }

    @Override
    protected void onNewSecond() {
        for (Player i : TeamHolder.getInstance().getRunners()) {
            coinEarned.put(i, coinEarned.get(i) + coinPerSecond);
        }
    }
}
