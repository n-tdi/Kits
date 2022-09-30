package world.ntdi.kits.data;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
}
