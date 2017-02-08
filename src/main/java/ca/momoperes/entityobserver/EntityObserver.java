package ca.momoperes.entityobserver;

import org.bukkit.plugin.java.JavaPlugin;

public class EntityObserver extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("observer").setExecutor(new ObserverCommand(this));
    }
}
