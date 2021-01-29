package com.minexd.zoot.profile.option.event;

import com.minexd.zoot.profile.option.menu.ProfileOptionButton;
import com.minexd.zoot.util.BaseEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class OptionsOpenedEvent extends BaseEvent {

    private final Player player;
    private List<ProfileOptionButton> buttons = new ArrayList<>();

}
