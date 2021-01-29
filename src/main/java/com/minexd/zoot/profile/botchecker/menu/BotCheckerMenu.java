package com.minexd.zoot.profile.botchecker.menu;

import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.botchecker.BOT_ITEM_LIST;
import com.minexd.zoot.tokens.menus.RankMenu;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BotCheckerMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return CC.RED + "Please Authenticate";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Profile profile = Profile.getProfiles().get(player.getUniqueId());

        
        Random random = new Random();

        buttons.put(random.nextInt(54), new Button() {

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                if (clickType != ClickType.LEFT) return;

                Profile issuer = Profile.getByUuid(player.getUniqueId());

                profile.setBotChecked(true);
                player.closeInventory();
            }

            @Override
            public String getName(Player player) {
                return CC.translate("&aClick Me!");
            }

            @Override
            public List<String> getDescription(Player player) {
                List<String> lore = new ArrayList<>();
                lore.add("");
                return lore;
            }

            @Override
            public Material getMaterial(Player player) {
                ArrayList<Material> allMaterials = new BOT_ITEM_LIST().getMaterials();
                Material material = allMaterials.get(random.nextInt(allMaterials.size()));
                profile.setBotCheckMaterial(material);
                return material;
            }
        });

        return buttons;
    }
}
