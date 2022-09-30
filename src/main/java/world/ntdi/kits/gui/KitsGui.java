package world.ntdi.kits.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemBuilder;
import world.ntdi.kits.Kits;
import world.ntdi.kits.data.Kit;

public class KitsGui {
    private InventoryGUI gui;
    private String primaryColor;
    private String secondaryColor;

    public KitsGui(Player p, Kits kits) {
        primaryColor = kits.primaryColor;
        secondaryColor = kits.secondaryColor;
        int size = 2 + (kits.jsonUtil.getKits().size() % 7);
        gui = new InventoryGUI(size * 9, primaryColor + "KITS");
        makeItems(p, kits, size * 9);
        gui.open(p);
    }

    public void makeItems(Player p, Kits kits, int size) {
        gui.fill(0, size, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(""));
        int start = 10;
        for (Kit kit : kits.jsonUtil.getKits()) {
            ItemButton kitBtn = ItemButton.create(
                    new ItemBuilder(kit.getIcon()).setName(kit.getColorfulName()),
                    (e) -> {
                        p.closeInventory();
                        new InspectKitGui(p, kit, kits);
                    }
                );
            gui.addButton(kitBtn, start);
            if((start+1) % 7 == 0) {
                start += 2;
            }
            start++;
        }
    }
}
