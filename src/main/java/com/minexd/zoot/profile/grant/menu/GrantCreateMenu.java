package com.minexd.zoot.profile.grant.menu;

import com.minexd.zoot.Locale;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.grant.Grant;
import com.minexd.zoot.profile.grant.procedure.GrantProcedure;
import com.minexd.zoot.profile.grant.procedure.GrantProcedureStage;
import com.minexd.zoot.profile.grant.procedure.GrantProcedureType;
import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import lombok.AllArgsConstructor;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.pagination.PageButton;
import net.frozenorb.qlib.menu.pagination.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by vape on 6/1/2020 at 11:59 AM.
 */
@AllArgsConstructor
public class GrantCreateMenu extends PaginatedMenu {
    private Profile profile;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Select Rank";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new PageButton(-1, this));
        buttons.put(8, new PageButton(+1, this));

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int i = 0;
        for (Rank rank : Rank.getRanks().values().stream().sorted(Comparator.comparingInt(Rank::getWeight).reversed()).collect(Collectors.toList())) {
            if (rank.isDefaultRank()) continue;
            buttons.put(i++, new GrantRankButton(profile, rank));
        }

        return buttons;
    }

    @AllArgsConstructor
    private class GrantRankButton extends Button {
        private Profile profile;
        private Rank rank;

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            Profile issuer = Profile.getByUuid(player.getUniqueId());

            if (issuer == null || !issuer.isLoaded() || profile == null || !profile.isLoaded()) {
                player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
                player.closeInventory();
                return;
            }

            GrantProcedure procedure = new GrantProcedure(player, profile, GrantProcedureType.GRANT, GrantProcedureStage.REQUIRE_TEXT);
            procedure.setGrant(new Grant(UUID.randomUUID(), rank, player.getUniqueId(), System.currentTimeMillis(), "§", -1));

            player.sendMessage(CC.GREEN + "Type a reason for creating this grant in chat...");
            player.closeInventory();
        }

        @Override
        public String getName(Player player) {
            return rank.getColor() + rank.getDisplayName();
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.PAPER;
        }

        @Override
        public List<String> getDescription(Player player) {
            List<String> lore = new ArrayList<>();

            lore.add(CC.MENU_BAR);
            lore.add(CC.DARK_AQUA + "Weight: " + CC.YELLOW + rank.getWeight());
            lore.add(CC.DARK_AQUA + "Prefix: " + CC.YELLOW + rank.getPrefix());
            lore.add("");
            lore.add(CC.DARK_AQUA + "Left click to select this rank.");
            lore.add(CC.MENU_BAR);

            return lore;
        }
    }
}