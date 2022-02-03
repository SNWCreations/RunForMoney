package snw.rfm.game;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import snw.rfm.RunForMoney;
import snw.rfm.group.Group;
import snw.rfm.group.GroupHolder;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Preset {
    private final List<String> runners = new ArrayList<>();
    private final List<String> hunters = new ArrayList<>();
    private final Map<String, Group> player_not_joined_groups = new HashMap<>();
    private static Preset INSTANCE;

    public Preset() {
        RunForMoney rfm = RunForMoney.getInstance();
        Logger l = rfm.getLogger();
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(new File(rfm.getDataFolder(), "presets.yml"));

        if (conf.getBoolean("IS_TEMPLATE")) {
            rfm.getLogger().log(Level.WARNING, "注意: 检测到预设的 IS_TEMPLATE 值为 true ，预设不会加载。");
        } else {
            l.info("加载预设...");
            List<String> runners_ = conf.getStringList("runners");
            List<String> hunters_ = conf.getStringList("hunters");
            boolean no_continue = false;

            if (runners_.isEmpty()) {
                l.log(Level.WARNING, "runners 为空！");
            }
            if (hunters_.isEmpty()) {
                l.log(Level.WARNING, "hunters 为空！");
                if (runners_.isEmpty()) {
                    l.log(Level.WARNING, "runners 项和 hunters 项均为空，预设无法加载。");
                    no_continue = true;
                }
            }


            if (!no_continue) {
                for (String i : runners_) {
                    runners.add(i.toLowerCase());
                }

                for (String i : hunters_) {
                    hunters.add(i.toLowerCase());
                }

                Set<String> invalid = new HashSet<>();
                for (String i : runners) {
                    if (hunters.contains(i)) {
                        l.log(Level.WARNING, "检测到玩家名 " + i + " 在预设中重复，因此对该玩家的预设无效。");
                        invalid.add(i);
                        hunters.remove(i);
                        runners.remove(i);
                    }
                }

                ConfigurationSection groups = conf.getConfigurationSection("groups");
                if (groups == null) {
                    l.log(Level.INFO, "groups 项不存在，将不会预设组。");
                } else {
                    Set<String> gk = groups.getKeys(false);
                    for (String k : gk) {
                        Group groupWillBeCreated = new Group(k);
                        GroupHolder.getInstance().add(groupWillBeCreated);
                        List<String> willBeProcessed = groups.getStringList(k);
                        for (String v : willBeProcessed) {
                            if (!(invalid.contains(v) || runners.contains(v))) {
                                player_not_joined_groups.put(v, groupWillBeCreated);
                            }
                        }
                    }
                    l.info("创建了 " + gk.toArray().length + " 个组。");
                }
            }
            l.info("预设加载完成。");
        }
    }

    public boolean isPresetHunter(Player player) {
        return hunters.contains(player.getName().toLowerCase());
    }

    public boolean isPresetRunner(Player player) {
        return runners.contains(player.getName());
    }

    @Nullable
    public Group getPlayerNotJoinedGroup(Player player) {
        return player_not_joined_groups.get(player.getName());
    }

    public static Preset getInstance() {
        return INSTANCE;
    }

    public static void init() {
        INSTANCE = new Preset();
    }
}
