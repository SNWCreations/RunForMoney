/**
 * This file is part of RunForMoney.
 *
 * RunForMoney is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RunForMoney is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RunForMoney. If not, see <https://www.gnu.org/licenses/>.
 */

package snw.rfm;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snw.rfm.api.GameController;
import snw.rfm.commands.CoinListCommand;
import snw.rfm.commands.admin.*;
import snw.rfm.commands.debug.ForceResumeCommand;
import snw.rfm.commands.debug.ForceStartCommand;
import snw.rfm.commands.group.*;
import snw.rfm.commands.hunter.ActivateHunterCommand;
import snw.rfm.commands.hunter.DeactivateHunterCommand;
import snw.rfm.commands.team.HunterCommand;
import snw.rfm.commands.team.LeaveTeamCommand;
import snw.rfm.commands.team.RunnerCommand;
import snw.rfm.config.GameConfiguration;
import snw.rfm.config.Preset;
import snw.rfm.game.GameProcess;
import snw.rfm.processor.EventProcessor;
import snw.rfm.processor.ExitingPickaxeProcessor;
import snw.rfm.processor.HunterPauseCardProcessor;
import snw.rfm.tasks.Updater;
import snw.rfm.util.LanguageSupport;
import snw.rfm.util.NickSupport;
import snw.rfm.util.PlaceHolderString;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class RunForMoney extends JavaPlugin {
    private static RunForMoney INSTANCE;
    private GameProcess gameProcess;
    private GameController gameController;
    private final Map<String, Double> coinEarned = new HashMap<>();

    @Override
    public void onLoad() {
        saveDefaultConfig();
        saveResource("presets.yml", false);
        saveResource("nickname.yml", false); // 2022/4/3 NickSupport!
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this; // 2022/1/29 ??? INSTANCE ????????????????????? Util ???????????????
        new Metrics(this, 14980); // 2022/4/19 Hello bStats!

        LanguageSupport.loadLanguage(getConfig().getString("language", "zh_CN"));

        ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
        PluginManager pluginManager = Bukkit.getPluginManager();

        consoleSender.sendMessage("[RunForMoney] " + ChatColor.GREEN + "============ Run FOR Money ============");
        consoleSender.sendMessage("[RunForMoney] " + ChatColor.GREEN + LanguageSupport.getTranslation("setup.author_info"));

        Logger ll = getLogger();
        ll.info(LanguageSupport.getTranslation("setup.load_data"));
        GameConfiguration.init(); // 2022/2/7 v1.1.5 GameConfiguration ?????????????????????????????????
        Preset.init();
        NickSupport.init(); // v1.8.0 NickSupport!
        EventProcessor.init();

        registerInternalItems();

        ll.info(LanguageSupport.getTranslation("setup.register_command"));
        // region ????????????
        registerCommand("start", new StartCommand());
        registerCommand("forcestop", new ForceStopCommand());
        registerCommand("hunter", new HunterCommand());
        registerCommand("runner", new RunnerCommand());
        registerCommand("leaveteam", new LeaveTeamCommand());
        registerCommand("endroom", new EndRoomCommand());
        registerCommand("newgroup", new NewGroupCommand());
        registerCommand("removegroup", new RemoveGroupCommand());
        registerCommand("activategroup", new ActivateGroupCommand());
        registerCommand("deactivategroup", new DeactivateGroupCommand());
        registerCommand("joingroup", new JoinGroupCommand());
        registerCommand("leavegroup", new LeaveGroupCommand());
        registerCommand("activatehunter", new ActivateHunterCommand());
        registerCommand("deactivatehunter", new DeactivateHunterCommand());
        registerCommand("resume", new ResumeCommand());
        registerCommand("grouplist", new GroupListCommand());
        registerCommand("teamlist", new TeamListCommand());
        registerCommand("coinlist", new CoinListCommand());
        registerCommand("rfmitem", new RFMItemCommand());
        registerCommand("exportcoinlist", new ExportListCommand());
        registerCommand("rfmrespawn", new RFMRespawnCommand());
        registerCommand("rfmsettingsquery", new RFMSettingsQueryCommand());
        registerCommand("rfmtimer", new RFMTimerCommand());
        registerCommand("pause", new PauseCommand());
        registerCommand("rfmreload", new RFMReloadCommand());
        registerCommand("playerremaining", new PlayerRemainingCommand());
        // endregion

        // region ??????????????????
        // ??????: ??????????????????????????????????????????????????????
        registerCommand("forcestart", new ForceStartCommand());
        registerCommand("forceresume", new ForceResumeCommand());
        // endregion

        ll.info(LanguageSupport.getTranslation("setup.register_event_processor"));
        pluginManager.registerEvents(new EventProcessor(), this);
        pluginManager.registerEvents(new ExitingPickaxeProcessor(), this);

        getLogger().info(LanguageSupport.getTranslation("setup.complete"));

        if (getConfig().getBoolean("check_update", false)) { // ????????????
            new Updater().start();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (getGameProcess() != null) {
            getLogger().info(LanguageSupport.getTranslation("unload.forcestop"));
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

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(@Nullable GameController gameController) {
        this.gameController = gameController;
    }

    public Map<String, Double> getCoinEarned() {
        return coinEarned;
    }

    public void registerInternalItems() {
        // region ?????????
        ItemStack ep = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta epmeta = ep.getItemMeta();
        //noinspection ConstantConditions
        epmeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + LanguageSupport.getTranslation("item.ep"));
        Damageable converted_meta = (Damageable) epmeta;
        converted_meta.setDamage(58);
        ep.setItemMeta((ItemMeta) converted_meta);
        NBTItem item = new NBTItem(ep);
        item.getStringList("CanDestroy").add(RunForMoney.getInstance().getConfig().getString("exiting_pickaxe_minable_block", "minecraft:diamond_block"));
        ItemStack EXITING_PICKAXE = item.getItem();
        ItemRegistry.registerItem("ep", EXITING_PICKAXE);
        // endregion

        // region ??????????????? (2022/1/30)
        Material hpctype = Material.matchMaterial(RunForMoney.getInstance().getConfig().getString("hpc_type", "minecraft:gold_ingot"));
        if (hpctype == null) {
            Bukkit.getConsoleSender().sendMessage("[RunForMoney] " + ChatColor.YELLOW +
                    new PlaceHolderString(
                            LanguageSupport.getTranslation("setup.config.item.invalid_type")).replaceArgument("itemName", LanguageSupport.getTranslation("item.hpc")));
        } else {
            ItemStack hpc = new ItemStack(hpctype);
            ItemMeta hpcmeta = hpc.getItemMeta();
            assert hpcmeta != null;
            hpcmeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + LanguageSupport.getTranslation("item.hpc"));
            hpc.setItemMeta(hpcmeta);
            HunterPauseCardProcessor hpcp = new HunterPauseCardProcessor();
            ItemRegistry.registerItem("hpc", hpc, hpcp);
            Bukkit.getPluginManager().registerEvents(hpcp, this);
        }
        // endregion
    }

    private void registerCommand(@NotNull String cmdName, @NotNull CommandExecutor executor) {
        PluginCommand cmd = getCommand(cmdName);
        if (cmd == null) {
            getLogger().severe(
                    new PlaceHolderString(LanguageSupport.getTranslation("setup.command_not_found")).replaceArgument("cmdName", cmdName).toString()
            );
            throw new NullPointerException();
        } else {
            cmd.setExecutor(executor);
        }
    }

}
