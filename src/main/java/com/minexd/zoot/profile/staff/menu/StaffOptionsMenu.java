package com.minexd.zoot.profile.staff.menu;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.tags.PlayerTag;
import com.minexd.zoot.util.CC;
import lombok.AllArgsConstructor;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffOptionsMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "Staff Options";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(1, new StaffNotificationButton());
        buttons.put(3, new StaffModeButton());
        return buttons;
    }

    @AllArgsConstructor
    private class StaffNotificationButton extends Button {

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;
            Profile issuer = Profile.getByUuid(player.getUniqueId());
            issuer.getStaffOptions().staffJoinLeaveAsNotis(!issuer.getStaffOptions().staffJoinLeaveAsNotis());
        }

        @Override
        public String getName(Player player) {
            return CC.translate("&3Join/Leave Messages");
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.PAPER;
        }

        @Override
        public List<String> getDescription(Player player) {
            Profile issuer = Profile.getByUuid(player.getUniqueId());
            List<String> lore = new ArrayList<>();
            lore.add(CC.CHAT_BAR);
            lore.add(CC.translate("&3This toggles how you see a player joining or leaving the network"));
            lore.add("");
            if (!issuer.getStaffOptions().staffJoinLeaveAsNotis()) {
                lore.add(CC.translate("&bCurrently Set To: &3Chat"));
            }
            if(issuer.getStaffOptions().staffJoinLeaveAsNotis()) {
                lore.add(CC.translate("&bCurrently Set To: &3Notifications"));
            }
            lore.add(CC.CHAT_BAR);
            return lore;
        }
    }

    private class StaffModeButton extends Button {

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;
            Profile issuer = Profile.getByUuid(player.getUniqueId());
            issuer.getStaffOptions().staffModeEnabled(!issuer.getStaffOptions().staffModeEnabled());
        }

        @Override
        public String getName(Player player) {
            return CC.translate("&3Staff Mode");
        }

        @Override
        public List<String> getDescription(Player player) {
            Profile issuer = Profile.getByUuid(player.getUniqueId());
            List<String> lore = new ArrayList<>();
            lore.add(CC.CHAT_BAR);
            lore.add(CC.translate("&3This toggles staff chat and admin chat, as well as reports."));
            lore.add("");
            if (!issuer.getStaffOptions().staffModeEnabled()) {
                lore.add(CC.translate("&bStaff Mode: &cDisabled"));
            }
            if(issuer.getStaffOptions().staffModeEnabled()) {
                lore.add(CC.translate("&bStaff Mode: &aEnabled"));
            }
            lore.add(CC.CHAT_BAR);
            return lore;
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.FEATHER;
        }
    }
}
