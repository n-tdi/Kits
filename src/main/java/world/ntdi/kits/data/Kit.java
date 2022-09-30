package world.ntdi.kits.data;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Kit {
    private String name;
    private Material icon;
    private int cooldown;
    private ItemStack[] items;

    public Kit(String name, Material icon, int cooldown, ItemStack[] items) {
        this.name = name;
        this.icon = icon;
        this.cooldown = cooldown;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public String getColorfulName() {
        return ChatColor.translateAlternateColorCodes('&', getName());
    }

    public Material getIcon() {
        return icon;
    }

    public int getCooldown() {
        return cooldown;
    }

    public ItemStack[] getItems() {
        return items;
    }

    private Cache<UUID, Long> cooldowns = CacheBuilder.newBuilder().expireAfterWrite(getCooldown(), TimeUnit.MINUTES).build();

    public void equip(Player p, String primaryColor, String secondaryColor) {
        if (!cooldowns.asMap().containsKey(p.getUniqueId())) {
            p.getInventory().addItem(getItems());
            p.sendMessage(primaryColor + "Successfully equipped " + getColorfulName());
            cooldowns.put(p.getUniqueId(), System.currentTimeMillis() + ((long) getCooldown() * 60 * 1000));
        } else {
            long distance = cooldowns.asMap().get(p.getUniqueId()) - System.currentTimeMillis();
            p.sendMessage(primaryColor + "You must wait " + secondaryColor + TimeUnit.MILLISECONDS.toMinutes(distance) + "m " + primaryColor + "until you can use this again.");
        }
    }
}
