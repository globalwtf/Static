package com.qrakn.honcho.command;

public class CommandOption {
    private final String tag;

    public CommandOption(final String tag) {
        this.tag = tag.toLowerCase();
    }

    public String getTag() {
        return this.tag;
    }
}
