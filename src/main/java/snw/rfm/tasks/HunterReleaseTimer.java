package snw.rfm.tasks;

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
        super(GameConfiguration.getInstance().getReleaseTime());
    }

    @Override
    public void onZero() {
        RunForMoney rfm = RunForMoney.getInstance();
        GameConfiguration conf = GameConfiguration.getInstance();
        List<BaseCountDownTimer> timers = rfm.getGameProcess().getTimers();
        TeamHolder holder = TeamHolder.getInstance();
        GroupHolder gh = GroupHolder.getInstance();

        for (Player i : Bukkit.getOnlinePlayers()) {
            for (int r = 0; r < 11; r++) {
                i.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "" + ChatColor.BOLD + "猎人已经放出"));
            }
            if (holder.isHunter(i)) {
                if (gh.findByPlayer(i) == null) {
                    holder.addEnabledHunter(i);
                }
            }
        }
        CoinTimer ct = new CoinTimer(conf.getGameTime() * 60, conf.getCoinPerSecond(), rfm.getCoinEarned());
        timers.add(ct);
        ct.start(rfm);
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
            for (int r = 0; r < 11; r++) {
                i.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
            }
        }
    }
}
