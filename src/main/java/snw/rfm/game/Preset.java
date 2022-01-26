package snw.rfm.game;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import snw.rfm.RunForMoney;
import snw.rfm.group.Group;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Preset {
    private final List<String> runners;
    private final List<String> hunters;
    private final Map<String, Group> player_not_joined_groups;

    public Preset() {
        RunForMoney rfm = RunForMoney.getInstance();
        Logger l = rfm.getLogger();
        File preset_file = new File(rfm.getDataFolder(), "presets.yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(preset_file);
        runners = new ArrayList<>();
        hunters = new ArrayList<>();
        player_not_joined_groups = new HashMap<>();

        if (conf.getBoolean("IS_TEMPLATE")) {
            rfm.getLogger().info("注意: 检测到预设的 IS_TEMPLATE 值为 true ，预设不会加载。");
        } else {
            List<String> runners_ = conf.getStringList("runners");
            List<String> hunters_ = conf.getStringList("hunters");
            boolean ri = false;
            boolean no_continue = false;

            if (runners_.isEmpty()) {
                l.log(Level.WARNING, "runners 为空！");
                ri = true;
            }
            if (hunters_.isEmpty()) {
                l.log(Level.WARNING, "hunters 为空！");
                if (ri) {
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
                boolean no_group = false;
                if (groups == null) {
                    l.log(Level.INFO, "groups 项不存在，将不会预设组。");
                    no_group = true;
                }

                if (!no_group) {
                    Set<String> gk = groups.getKeys(false);
                    for (String k : gk) {
                        Group groupWillBeCreated = new Group(k);
                        RunForMoney.getInstance().getGroups().add(groupWillBeCreated);
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
}
