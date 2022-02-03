package snw.rfm.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.RunForMoney;

public abstract class BaseCountDownTimer extends BukkitRunnable {
    protected int secs;

    public BaseCountDownTimer(int secs) {
        if (secs <= 0) {
            throw new IllegalArgumentException("设定时间必须大于 0 秒。");
        }
        this.secs = secs;
    }

    public void start(RunForMoney plugin) {
        super.runTaskTimer(plugin, 0, 20L);
    }

    @Override
    public final void run() {
        if (secs-- > 0) {
            onNewSecond();
        } else {
            cancel();
            onZero();
        }
    }

    protected int getTimeLeft() {
        return secs;
    }

    protected abstract void onZero();

    protected abstract void onNewSecond();
}
