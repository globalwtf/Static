package com.minexd.zoot.profile.grant.menu;

import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.grant.Grant;
import com.minexd.zoot.profile.grant.procedure.GrantProcedure;
import com.minexd.zoot.profile.grant.procedure.GrantProcedureStage;
import com.minexd.zoot.profile.grant.procedure.GrantProcedureType;
import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.ItemBuilder;
import com.minexd.zoot.util.TimeUtil;
import lombok.AllArgsConstructor;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GrantsListMenu extends Menu {

    private Profile profile;

    @Override
    public String getTitle(Player player) {
        return ChatColor.GOLD.toString() + profile.getName() + "'s Grants (" + profile.getGrants().size() + ")";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Grant grant : profile.getGrants().stream().sorted(Comparator.comparingInt(g -> ((Grant)g).getRank().getWeight()).reversed()).sorted(Comparator.comparingInt(g -> g.isRemoved() ? 1 : 0)).collect(Collectors.toList())) {
            buttons.put(buttons.size(), new GrantInfoButton(profile, grant));
        }

        return buttons;
    }

    @AllArgsConstructor
    private class GrantInfoButton extends Button {

        private Profile profile;
        private Grant grant;

        @Override
        public String getName(Player player) {
            return "&3" + TimeUtil.dateToString(new Date(grant.getAddedAt()), "&7");
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.PAPER;
        }

        @Override
        public List<String> getDescription(Player player) {
            String addedBy = "Console";

            if (grant.getAddedBy() != null) {
                addedBy = "Could not fetch...";

                Profile addedByProfile = Profile.getByUuid(grant.getAddedBy());

                if (addedByProfile != null && addedByProfile.isLoaded()) {
                    addedBy = addedByProfile.getName();
                }
            }

            String removedBy = "Console";

            if (grant.getRemovedBy() != null) {
                removedBy = "Could not fetch...";

                Profile removedByProfile = Profile.getByUuid(grant.getRemovedBy());

                if (removedByProfile != null && removedByProfile.isLoaded()) {
                    removedBy = removedByProfile.getName();
                }
            }

            List<String> lore = new ArrayList<>();

            lore.add(CC.MENU_BAR);
            lore.add("&3Rank: &e" + grant.getRank().getDisplayName());
            lore.add("&3Duration: &e" + grant.getDurationText());
            lore.add("&3Issued by: &e" + addedBy);
            lore.add("&3Reason: &e" + grant.getAddedReason());

            if (grant.isRemoved()) {
                lore.add(CC.MENU_BAR);
                lore.add("&a&lGrant Removed");
                lore.add("&a" + TimeUtil.dateToString(new Date(grant.getRemovedAt()), "&7"));
                lore.add("&aRemoved by: &7" + removedBy);
                lore.add("&aReason: &7&o\"" + grant.getRemovedReason() + "\"");
            } else {
                if (!grant.hasExpired()) {
                    lore.add(CC.MENU_BAR);

                    if (player.hasPermission("static.grants.remove")) {
                        lore.add("&aClick to remove this grant");
                    } else {
                        lore.add("&cYou cannot remove grants");
                    }
                }
            }

            lore.add(CC.MENU_BAR);
            return lore;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            int durability;

            if (grant.isRemoved()) {
                durability = 14;
            } else if (grant.hasExpired()) {
                durability = 4;
            } else {
                durability = 5;
            }

            return new ItemBuilder(Material.STAINED_GLASS_PANE)
                    .name(getName(player))
                    .durability(durability)
                    .lore(getDescription(player))
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            if (!grant.isRemoved() && !grant.hasExpired()) {
                if (player.hasPermission("zoot.grants.remove")) {
                    GrantProcedure procedure = new GrantProcedure(player, profile, GrantProcedureType.REMOVE,
                            GrantProcedureStage.REQUIRE_TEXT
                    );
                    procedure.setGrant(grant);

                    player.sendMessage(CC.GREEN + "Type a reason for removing this grant in chat...");
                    player.closeInventory();
                } else {
                    player.sendMessage(CC.RED + "You cannot remove grants.");
                }
            }
        }
    }

}
