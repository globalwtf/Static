package com.minexd.zoot.profile.option;

import lombok.Getter;
import lombok.Setter;

public class ProfileOptions {

    @Getter @Setter private boolean publicChatEnabled = true;
    @Getter @Setter private boolean receivingNewConversations = true;
    @Getter @Setter private boolean playingMessageSounds = true;
    @Getter @Setter private boolean frozen = false;
    @Getter @Setter private boolean twoFA = false;
}
