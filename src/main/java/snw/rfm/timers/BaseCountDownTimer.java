package snw.rfm.timers;

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
    public void run() {
        onNewSecond();
        if (secs <= 0) { // 之所以还用 <= 是因为要防止意外。
            cancel();
            onZero();
            return;
        }
        secs--;
    }

    protected int getTimeLeft() {
        return secs;
    }

    public abstract void onZero();

    public abstract void onNewSecond();
}
