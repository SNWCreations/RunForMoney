package snw.rfm.group;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public final class GroupHolder extends ArrayList<Group> {
    private static final GroupHolder INSTANCE = new GroupHolder();

    @Nullable
    public Group findByName(String name) {
        for (Group i : this) {
            if (Objects.deepEquals(i.getName(), name)) {
                return i;
            }
        }
        return null;
    }

    @Nullable
    public Group findByPlayer(Player player) {
        for (Group i : this) {
            if (i.contains(player)) {
                return i;
            }
        }
        return null;
    }

    public static GroupHolder getInstance() {
        return INSTANCE;
    }
}
