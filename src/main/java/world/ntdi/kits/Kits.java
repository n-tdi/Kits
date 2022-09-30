package world.ntdi.kits;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import world.ntdi.kits.commands.KitsCommand;
import world.ntdi.kits.data.JsonUtil;

public final class Kits extends JavaPlugin {
    public JsonUtil jsonUtil;

    public String primaryColor;
    public String secondaryColor;

    @Override
    public void onEnable() {
        // Plugin startup logic
        jsonUtil = new JsonUtil(this);
        jsonUtil.readKits();

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        primaryColor = translate(getConfig().getString("colors.primary-color"));
        secondaryColor = translate(getConfig().getString("colors.secondary-color"));


        getCommand("kits").setExecutor(new KitsCommand(this));
        getCommand("kits").setTabCompleter(new KitsCommand(this));
    }

    private String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        jsonUtil.storeKits();
    }
}
