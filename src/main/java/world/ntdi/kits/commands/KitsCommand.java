package world.ntdi.kits.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import world.ntdi.kits.Kits;
import world.ntdi.kits.data.Kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KitsCommand implements CommandExecutor, TabCompleter {
    private final String primaryColor;
    private final String secondaryColor;
    private final Kits kits;

    public KitsCommand(Kits kits) {
        this.kits = kits;
        this.primaryColor = kits.primaryColor;
        this.secondaryColor = kits.secondaryColor;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (hasPerms(sender, getPerm("permissions.kits-help")))
                sender.sendMessage(
                    secondaryColor + "/kits create",
                    secondaryColor + "/kits delete",
                    secondaryColor + "/kits view",
                    secondaryColor + "/kits viewgui",
                    secondaryColor + "/kits equip"
                );
        } else {
            if (sender instanceof Player p) {
                String subCMD = args[0];
                if (subCMD.equals("create")) {
                    if (hasPerms(p, getPerm("permissions.create-kit"))) {
                        if (args.length >= 2) {
                            int cooldown = Integer.parseInt(args[1]);
                            String name = argBuilder(args, 2);
                            for (Kit kit : kits.jsonUtil.getKits()) {
                                if (kit.getName().equalsIgnoreCase(name)) {
                                    p.sendMessage(secondaryColor + "Kit name already in use!");
                                    return true;
                                }
                            }

                            List<ItemStack> rawItems = Arrays.stream(p.getInventory().getContents()).toList();
                            if (rawItems.size() == 0) {
                                p.sendMessage(primaryColor + "No items in inventory!");
                            }
                            List<ItemStack> filteredItems = new ArrayList<>();
                            for (ItemStack ri : rawItems) {
                                if (ri != null) {
                                    if (!ri.getType().equals(Material.AIR)) {
                                        filteredItems.add(ri);
                                    }
                                }
                            }

                            ItemStack[] finalItems = filteredItems.toArray(ItemStack[]::new);
                            Material icon = (p.getInventory().getItemInMainHand().getType() != Material.AIR ? p.getInventory().getItemInMainHand().getType() : finalItems[0].getType());
                            Kit kit = new Kit(name, icon, cooldown, finalItems);
                            kits.jsonUtil.addKit(kit);
                            p.sendMessage(primaryColor + "Successfully created kit " + kit.getColorfulName());
                        } else {
                            p.sendMessage("/kits create <int cooldown> [String... name]");
                        }
                    }
                } else if (subCMD.equals("delete")) {
                    if (hasPerms(p, getPerm("permissions.remove-kit"))) {
                        if (args.length >= 2) {
                            String name = argBuilder(args, 1);
                            List<Kit> kitList = kits.jsonUtil.getKits();
                            for (int i = 0; i < kitList.size(); i++) {
                                Kit kit = kitList.get(i);
                                if (kit.getName().equals(name) || ChatColor.stripColor(kit.getColorfulName()).equals(name)) {
                                    kitList.remove(kit);
                                    kits.jsonUtil.setKits(kitList);
                                    p.sendMessage(primaryColor + "Succesfully removed kit " + kit.getColorfulName() + primaryColor + " from the list!");
                                    return true;
                                }
                            }
                            p.sendMessage(primaryColor + "unable to find kit " + name);
                        } else {
                            p.sendMessage("/kits delete [String... name]" + secondaryColor + " - note: name does not need to contain colorcodes");
                        }
                    }
                } else if (subCMD.equals("view")) {
                    if (hasPerms(p, getPerm("permissions.view-kit"))) {
                        p.sendMessage(secondaryColor + "Kits: ");
                        for (Kit kit : kits.jsonUtil.getKits()) {
                            TextComponent kitName = new TextComponent(primaryColor + kit.getColorfulName());
                            kitName.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, kit.getName()));
                            p.spigot().sendMessage(kitName);
                        }
                    }
                } else if (subCMD.equals("viewgui")) {
                    if (hasPerms(p, getPerm("permissions.view-kit-gui"))) {
                        p.sendMessage(primaryColor + "Coming Soon!");
                    }
                } else if (subCMD.equals("equip")) {
                    if (hasPerms(p, getPerm("permissions.kit-equip"))) {
                        if (args.length >= 2) {
                            String name = argBuilder(args, 1);
                            for (Kit kit : kits.jsonUtil.getKits()) {
                                if (kit.getName().equals(name) || ChatColor.stripColor(kit.getColorfulName()).equals(name)) {
                                    p.getInventory().addItem(kit.getItems());
                                    p.sendMessage(primaryColor + "Successfully equipped " + kit.getColorfulName());
                                    return true;
                                }
                            }
                            p.sendMessage(primaryColor + "Could not find kit!");
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean hasPerms(CommandSender sender, String permission) {
        return sender.hasPermission("kits.*") || sender.hasPermission("kits."+permission);
    }

    private String getPerm(String perm) {
        return kits.getConfig().getString(perm);
    }

    private String argBuilder(String[] args, int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < args.length; i++) {
           sb.append(args[i]).append(" ");
        }
        return sb.toString();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length >= 2) {
            if (args[0].equals("equip")) {
                final List<String> completions = new ArrayList<>();
                for (Kit kit : kits.jsonUtil.getKits()) {
                    completions.add(ChatColor.stripColor(kit.getColorfulName()));
                }
                return completions;
            } else if (args[0].equals("delete")) {
                final List<String> completions = new ArrayList<>();
                for (Kit kit : kits.jsonUtil.getKits()) {
                    completions.add(ChatColor.stripColor(kit.getColorfulName()));
                }
                return completions;
            } else {
                return List.of("");
            }
        } else if (args.length > 0) {
            //create new array
            final List<String> completions = List.of("create", "view", "delete", "viewgui", "equip");

            return completions;
        }
        return List.of("");
    }
}
