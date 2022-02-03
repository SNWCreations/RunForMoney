package snw.rfm.item.processor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import snw.rfm.RunForMoney;
import snw.rfm.game.TeamHolder;
import snw.rfm.item.ItemConfiguration;
import snw.rfm.item.RFMItems;
import snw.rfm.tasks.items.HunterPauseTimer;

public final class HunterPauseCardItemProcessor implements Listener {
    private boolean isEnabled;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (RunForMoney.getInstance().getGameProcess() != null && TeamHolder.getInstance().isHunter(event.getPlayer())) {
            event.setCancelled(isEnabled);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST) // 不用 LOWEST 会导致获取不到事件，因为 NMS 内部会自动取消右键空气事件，一般的优先级 (中等) 不行
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (RunForMoney.getInstance().getGameProcess() == null) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (item.isSimilar(RFMItems.HUNTER_PAUSE_CARD)) {
                TeamHolder th = TeamHolder.getInstance();
                if (!th.isHunter(player)) { // 排除使用者是猎人从而导致猎人坑队友的情况，哈哈哈哈哈哈哈笑死我了
                    int hpctime = ItemConfiguration.getInstance().getItemTime("hpc");
                    Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " 使用了猎人暂停卡。猎人暂停了 " + hpctime + " 秒。");
                    isEnabled = true;
                    new HunterPauseTimer(hpctime, this).start(RunForMoney.getInstance());
                }
                item.setAmount(item.getAmount() - 1);
            }
        }

    }

    public void ok() {
        isEnabled = false;
    }
}
