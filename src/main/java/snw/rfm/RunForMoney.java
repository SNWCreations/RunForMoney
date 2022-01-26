package snw.rfm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import snw.rfm.commands.CoinListCommand;
import snw.rfm.commands.admin.*;
import snw.rfm.commands.group.ActivateGroupCommand;
import snw.rfm.commands.group.DeactivateGroupCommand;
import snw.rfm.commands.group.NewGroupCommand;
import snw.rfm.commands.group.RemoveGroupCommand;
import snw.rfm.commands.hunter.ActivateHunterCommand;
import snw.rfm.commands.hunter.DeactivateHunterCommand;
import snw.rfm.commands.hunter.JoinGroupCommand;
import snw.rfm.commands.hunter.LeaveGroupCommand;
import snw.rfm.commands.team.HunterCommand;
import snw.rfm.commands.team.LeaveTeamCommand;
import snw.rfm.commands.team.RunnerCommand;
import snw.rfm.game.*;
import snw.rfm.group.GroupHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class RunForMoney extends JavaPlugin {
    private static RunForMoney INSTANCE;
    private GameConfiguration CONFIG;
    private GameProcess gameProcess;
    private TeamHolder team;
    private GroupHolder groups;
    private Preset preset;
    private Map<Player, Double> coinEarned;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        saveResource("presets.yml", false);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        ConsoleCommandSender l = Bukkit.getConsoleSender();
        if (Bukkit.getWorld("world") == null) {
            l.sendMessage(ChatColor.RED + "错误: 世界 'world' 不存在，插件无法加载。请把将用于游戏的地图改名为 'world' 再重试。");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        INSTANCE = this;
        CONFIG = new GameConfiguration();
        team = new TeamHolder();
        groups = new GroupHolder();
        coinEarned = new HashMap<>();

        l.sendMessage("[RunForMoney] " + ChatColor.GREEN + "============ Run FOR Money ============");
        l.sendMessage("[RunForMoney] " + ChatColor.GOLD + "本插件由 SNWCreations @ MCBBS.NET 制作");

        Logger ll = getLogger();
        ll.info("加载预设...");
        preset = new Preset();

        ll.info("注册命令...");
        registerCommand("start", new StartCommand());
        registerCommand("forcestop", new ForceStopCommand());
        registerCommand("hunter", new HunterCommand());
        registerCommand("runner", new RunnerCommand());
        registerCommand("leaveteam", new LeaveTeamCommand());
        registerCommand("erl", new EndRoomCommand());
        registerCommand("ng", new NewGroupCommand());
        registerCommand("rg", new RemoveGroupCommand());
        registerCommand("ag", new ActivateGroupCommand());
        registerCommand("dg", new DeactivateGroupCommand());
        registerCommand("jg", new JoinGroupCommand());
        registerCommand("lg", new LeaveGroupCommand());
        registerCommand("ah", new ActivateHunterCommand());
        registerCommand("dh", new DeactivateHunterCommand());
        registerCommand("ep", new ExitingPickaxeCommand());
        registerCommand("resume", new ResumeCommand());
        registerCommand("grouplist", new GroupListCommand());
        registerCommand("teamlist", new TeamListCommand());
        registerCommand("coinlist", new CoinListCommand());

        ll.info("注册事件处理器...");
        Bukkit.getPluginManager().registerEvents(new EventProcessor(), this);

        getLogger().info("加载完成。");
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

    public GameConfiguration getGameConfiguration() {
        return CONFIG;
    }

    public GroupHolder getGroups() {
        return groups;
    }

    public TeamHolder getTeamHolder() {
        return team;
    }

    public Preset getPreset() {
        return preset;
    }

    public Map<Player, Double> getCoinEarned() {
        return coinEarned;
    }

    private void registerCommand(String cmdName, CommandExecutor executor) {
        PluginCommand cmd = Bukkit.getPluginCommand(cmdName);
        if (cmd == null) {
            getLogger().log(Level.SEVERE, "命令 " + cmdName + "注册失败。插件无法加载。");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            cmd.setExecutor(executor);
        }
    }
}
