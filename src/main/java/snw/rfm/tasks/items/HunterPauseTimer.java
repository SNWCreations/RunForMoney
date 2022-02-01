package snw.rfm.tasks.items;

import snw.rfm.item.processor.HunterPauseCardItemProcessor;
import snw.rfm.tasks.BaseCountDownTimer;

public final class HunterPauseTimer extends BaseCountDownTimer {
    private final HunterPauseCardItemProcessor inst;

    public HunterPauseTimer(int secs, HunterPauseCardItemProcessor instance) {
        super(secs);
        inst = instance;
    }

    @Override
    public void onZero() {
        inst.ok();
    }

    @Override
    public void onNewSecond() {

    }
}
