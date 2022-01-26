package snw.rfm.timers;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import snw.rfm.RunForMoney;
import snw.rfm.game.GameConfiguration;
import snw.rfm.game.TeamHolder;
import snw.rfm.group.GroupHolder;

import java.util.List;

public final class HunterReleaseTimer extends BaseCountDownTimer {
    public HunterReleaseTimer() {
        super(RunForMoney.getInstance().getGameConfiguration().getReleaseTime());
    }

    @Override
    public void onZero() {
        RunForMoney rfm = RunForMoney.getInstance();
        GameConfiguration conf = rfm.getGameConfiguration();
        List<BaseCountDownTimer> timers = rfm.getGameProcess().getTimers();
        TeamHolder holder = rfm.getTeamHolder();
        GroupHolder gh = rfm.getGroups();

        for (Player i : Bukkit.getOnlinePlayers()) {
            for (int r = 0; r < 6; r++){
                i.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "猎人已经放出"));
            }
            if (holder.isHunter(i)) {
                if (gh.findByPlayer(i) == null) {
                    holder.addEnabledHunter(i);
                }
            }
        }
        CoinTimer ct = new CoinTimer(conf.getGameTime() * 60, conf.getCoinPerSecond(), rfm.getCoinEarned());
        AFKNotifyTimer at = new AFKNotifyTimer();
        timers.add(ct);
        timers.add(at);
        ct.start(rfm);
        at.start(rfm);
    }

    @Override
    public void onNewSecond() {
        String text = "";
        int left = getTimeLeft();
        if (left == 30) {
            text = ChatColor.RED + "猎人还有 " + ChatColor.GREEN + ChatColor.BOLD + getTimeLeft() + ChatColor.RESET + ChatColor.RED + " 秒放出";
        } else if (left == 15) {
            text = ChatColor.RED + "猎人还有 " + ChatColor.YELLOW + ChatColor.BOLD + getTimeLeft() + ChatColor.RESET + ChatColor.RED + " 秒放出";
        } else if (left <= 10) {
            text = ChatColor.RED + "猎人还有 " + ChatColor.DARK_RED + ChatColor.BOLD + getTimeLeft() + ChatColor.RESET + ChatColor.RED + " 秒放出";
        }
        for (Player i : Bukkit.getOnlinePlayers()) {
            for (int r = 0; r < 6; r++){
                i.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
            }
        }
    }
}
