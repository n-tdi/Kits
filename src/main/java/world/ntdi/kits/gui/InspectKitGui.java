package world.ntdi.kits.gui;

import org.bukkit.entity.Player;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import world.ntdi.kits.Kits;
import world.ntdi.kits.data.Kit;

public class InspectKitGui {
    private InventoryGUI gui;

    public InspectKitGui(Player p, Kit kit, Kits kits) {
        gui = new InventoryGUI(4 * 9, kits.primaryColor + "KITS");
        makeItems(kit);
        gui.open(p);
    }

    public void makeItems(Kit kit) {
        for (int i = 0; i < kit.getItems().length; i++) {
            ItemButton item = ItemButton.create(kit.getItems()[i], (e) -> e.setCancelled(true));
            gui.addButton(item, i);
        }
    }


}
