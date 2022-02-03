package snw.rfm.tasks;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.RunForMoney;
import snw.rfm.game.GameConfiguration;
import snw.rfm.game.TeamHolder;
import snw.rfm.group.GroupHolder;

import java.util.List;

public final class HunterReleaseTimer extends BaseCountDownTimer {
    public HunterReleaseTimer() {
        super(GameConfiguration.getInstance().getReleaseTime());
    }

    @Override
    protected void onZero() {
        RunForMoney rfm = RunForMoney.getInstance();
        GameConfiguration conf = GameConfiguration.getInstance();
        List<BaseCountDownTimer> timers = rfm.getGameProcess().getTimers();
        TeamHolder holder = TeamHolder.getInstance();
        GroupHolder gh = GroupHolder.getInstance();

        int gameTimeSecs = conf.getGameTime() * 60;
        for (Player i : Bukkit.getOnlinePlayers()) {
            if (holder.isHunter(i)) {
                if (gh.findByPlayer(i) == null) {
                    holder.addEnabledHunter(i);
                    i.removePotionEffect(PotionEffectType.SLOW);
                    i.removePotionEffect(PotionEffectType.JUMP);
                    i.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, gameTimeSecs * 20, 1, false)); // 2022/2/3 免去重复取值。
                }
            }
        }
        new SendingActionBarMessage(new TextComponent(ChatColor.DARK_RED + "" + ChatColor.BOLD + "猎人已经放出")).start();
        CoinTimer ct = new CoinTimer(gameTimeSecs, conf.getCoinPerSecond(), rfm.getCoinEarned());
        ct.start(rfm);
        timers.add(ct); // 应该先启动后增加。
    }

    @Override
    protected void onNewSecond() {
        String text = "";
        int left = getTimeLeft();
        if (left == 30) {
            text = ChatColor.RED + "猎人还有 " + ChatColor.GREEN + ChatColor.BOLD + getTimeLeft() + ChatColor.RESET + ChatColor.RED + " 秒放出";
        } else if (left == 15) {
            text = ChatColor.RED + "猎人还有 " + ChatColor.YELLOW + ChatColor.BOLD + getTimeLeft() + ChatColor.RESET + ChatColor.RED + " 秒放出";
        } else if (left <= 10) {
            text = ChatColor.RED + "猎人还有 " + ChatColor.DARK_RED + ChatColor.BOLD + getTimeLeft() + ChatColor.RESET + ChatColor.RED + " 秒放出";
        }
        if (!text.equals("")) {
            new SendingActionBarMessage(new TextComponent(text)).start();
        }
    }

    private static final class SendingActionBarMessage extends BukkitRunnable {
        private final TextComponent text;
        private int ticked = 0;

        public SendingActionBarMessage(TextComponent textToSend) {
            this.text = textToSend;
        }

        @Override
        public void run() {
            for (Player i : Bukkit.getOnlinePlayers()) {
                i.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
            }
            if (ticked++ >= 20) {
                cancel();
            }
        }

        public void start() {
            super.runTaskTimer(RunForMoney.getInstance(), 0L, 1L);
        }
    }
}
