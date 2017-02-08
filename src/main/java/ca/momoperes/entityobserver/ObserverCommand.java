package ca.momoperes.entityobserver;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.*;

public class ObserverCommand implements CommandExecutor {
    private final EntityObserver plugin;

    public ObserverCommand(EntityObserver plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("You must be an operator.");
            return false;
        }
        String worldName = null;
        if (args.length > 0) {
            worldName = args[0];
        }
        if (worldName == null) {
            // all worlds
            Bukkit.getWorlds().forEach(world -> observeForWorld(sender, world));
        } else {
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                sender.sendMessage("The world '" + worldName + "' does not exist.");
                return false;
            }
            observeForWorld(sender, world);
        }
        return false;
    }

    private void observeForWorld(CommandSender sender, World world) {
        List<Entity> entities = world.getEntities();
        Map<EntityType, Integer> entityCount = new HashMap<>();
        entities.forEach(entity -> {
            entityCount.put(entity.getType(), entityCount.containsKey(entity.getType()) ? entityCount.get(entity.getType()) + 1 : 1);
        });
        Map<EntityType, Integer> sorted = sortByValue(entityCount);
        sender.sendMessage("Entity observations for world " + world.getName() + ", " + entities.size() + " total:");
        if (sorted.size() == 0) {
            sender.sendMessage("<No entities>");
            return;
        }
        sorted.forEach((type, count) -> {
            sender.sendMessage("- '" + type.getName() + "': " + count + " (~" + (int) ((double) count / (double) entities.size() * 100) + "%)");
        });
    }

    private <K, V> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        list.sort((o1, o2) -> ((Comparable<V>) o1.getValue()).compareTo(o2.getValue()));
        Map<K, V> result = new LinkedHashMap<>();
        for (Iterator<Map.Entry<K, V>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<K, V> entry = it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
