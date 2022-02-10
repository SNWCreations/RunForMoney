/**
 * This file is part of RunForMoney.
 * <p>
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * <p>
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import snw.rfm.commands.CoinListCommand;
import snw.rfm.commands.admin.*;
import snw.rfm.commands.debug.ForceStartCommand;
import snw.rfm.commands.group.*;
import snw.rfm.commands.hunter.ActivateHunterCommand;
import snw.rfm.commands.hunter.DeactivateHunterCommand;
import snw.rfm.commands.team.HunterCommand;
import snw.rfm.commands.team.LeaveTeamCommand;
import snw.rfm.commands.team.RunnerCommand;
import snw.rfm.config.GameConfiguration;
import snw.rfm.config.ItemConfiguration;
import snw.rfm.config.Preset;
import snw.rfm.game.GameProcess;
import snw.rfm.processor.EventProcessor;
import snw.rfm.processor.ExitingPickaxeProcessor;
import snw.rfm.processor.HunterPauseCardProcessor;
import snw.rfm.tasks.Updater;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class RunForMoney extends JavaPlugin {
    private static RunForMoney INSTANCE;
    private GameProcess gameProcess;
    private final Map<Player, Double> coinEarned = new HashMap<>();

    @Override
    public void onLoad() {
        saveDefaultConfig();
        saveResource("presets.yml", false);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this; // 2022/1/29 把 INSTANCE 引用提前，便于 Util 操作实例。

        ConsoleCommandSender l = Bukkit.getConsoleSender();
        PluginManager pmgr = Bukkit.getPluginManager();
        if (Bukkit.getWorld("world") == null) {
            l.sendMessage("[RunForMoney] " + ChatColor.RED + "错误: 世界 'world' 不存在，插件无法加载。请把将用于游戏的地图改名为 'world' 再重试。");
            pmgr.disablePlugin(this);
            return; // 2022/2/9 因为看着 else 不爽，所以我把它删了。
        }

        l.sendMessage("[RunForMoney] " + ChatColor.GREEN + "============ Run FOR Money ============");
        l.sendMessage("[RunForMoney] " + ChatColor.GREEN + "本插件由 SNWCreations @ MCBBS.NET 制作");

        Logger ll = getLogger();
        ll.info("加载数据...");
        GameConfiguration.check(); // 2022/2/7 v1.1.5 GameConfiguration 不应该是需要实例化的。
        Preset.init();

        registerInternalItems();

        ll.info("注册命令...");
        // region 注册命令
        // v1.1.0 把本类的 registerCommand 移至 Util 类。
        Util.registerCommand("start", new StartCommand());
        Util.registerCommand("forcestop", new ForceStopCommand());
        Util.registerCommand("hunter", new HunterCommand());
        Util.registerCommand("runner", new RunnerCommand());
        Util.registerCommand("leaveteam", new LeaveTeamCommand());
        Util.registerCommand("endroom", new EndRoomCommand());
        Util.registerCommand("newgroup", new NewGroupCommand());
        Util.registerCommand("removegroup", new RemoveGroupCommand());
        Util.registerCommand("activategroup", new ActivateGroupCommand());
        Util.registerCommand("deactivategroup", new DeactivateGroupCommand());
        Util.registerCommand("joingroup", new JoinGroupCommand());
        Util.registerCommand("leavegroup", new LeaveGroupCommand());
        Util.registerCommand("activatehunter", new ActivateHunterCommand());
        Util.registerCommand("deactivatehunter", new DeactivateHunterCommand());
        Util.registerCommand("resume", new ResumeCommand());
        Util.registerCommand("grouplist", new GroupListCommand());
        Util.registerCommand("teamlist", new TeamListCommand());
        Util.registerCommand("coinlist", new CoinListCommand());
        Util.registerCommand("rfmitem", new RFMItemCommand());
        Util.registerCommand("exportcoinlist", new ExportListCommand());
        // endregion

        // region 注册调试命令
        // 警告: 以下注册的命令不应该被最终用户使用。
        Util.registerCommand("forcestart", new ForceStartCommand());
        // endregion

        ll.info("注册事件处理器...");
        pmgr.registerEvents(new EventProcessor(), this);
        pmgr.registerEvents(new ExitingPickaxeProcessor(), this);

        getLogger().info("加载完成。");

        if (getConfig().getBoolean("check_update", false)) { // 检查更新
            new Updater().runTaskAsynchronously(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Logger l = getLogger();
        if (getGameProcess() != null) {
            l.info("检测到有正在进行的游戏，正在强制终止现有游戏。");
            getGameProcess().stop();
        }
    }

    public static RunForMoney getInstance() {
        return INSTANCE;
    }

    public GameProcess getGameProcess() {
        return gameProcess;
    }

    public void setGameProcess(@Nullable GameProcess process) {
        gameProcess = process;
    }

    public Map<Player, Double> getCoinEarned() {
        return coinEarned;
    }

    private void registerInternalItems() {
        // region 弃权镐
        ItemStack ep = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta epmeta = ep.getItemMeta();
        //noinspection ConstantConditions
        epmeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "弃权镐");
        Damageable converted_meta = (Damageable) epmeta;
        converted_meta.setDamage(58);
        ep.setItemMeta((ItemMeta) converted_meta);
        NBTItem item = new NBTItem(ep);
        item.getStringList("CanDestroy").add(ItemConfiguration.getExitingPickaxeMinableBlock());
        ItemStack EXITING_PICKAXE = item.getItem();
        ItemRegistry.registerItem("ep", EXITING_PICKAXE);
        // endregion

        // region 猎人暂停卡 (2022/1/30)
        Material hpctype = Material.matchMaterial(ItemConfiguration.getItemType("hpc"));
        if (hpctype == null) {
            Bukkit.getConsoleSender().sendMessage("[RunForMoney] " + ChatColor.YELLOW + "警告: 创建物品 猎人暂停卡 时出现错误: 未提供一个可用的物品类型。请检查配置！");
        } else {
            ItemStack hpc = new ItemStack(hpctype);
            ItemMeta hpcmeta = hpc.getItemMeta();
            assert hpcmeta != null;
            hpcmeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "猎人暂停卡");
            hpc.setItemMeta(hpcmeta);
            ItemRegistry.registerItem("hpc", hpc, new HunterPauseCardProcessor());
        }
        // endregion

        // region 保命符 (2022/2/5)
        Material sltype = Material.matchMaterial(ItemConfiguration.getItemType("hpc"));
        if (sltype == null) {
            Bukkit.getConsoleSender().sendMessage("[RunForMoney] " + ChatColor.YELLOW + "警告: 创建物品 保命符 时出现错误: 未提供一个可用的物品类型。请检查配置！");
        } else {
            ItemStack sl = new ItemStack(sltype);
            ItemMeta slmeta = sl.getItemMeta();
            assert slmeta != null;
            slmeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "保命符");
            sl.setItemMeta(slmeta);
            ItemRegistry.registerItem("sl", sl);
        }
        // endregion
    }
}
