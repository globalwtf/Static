package com.minexd.zoot.network.event;

import com.minexd.zoot.util.BaseEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class ReceiveStaffChatEvent extends BaseEvent implements Cancellable {

    @Getter
    private Player player;
    @Getter
    @Setter
    private boolean cancelled;

    public ReceiveStaffChatEvent(Player player) {
        this.player = player;
    }

}
