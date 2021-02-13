package com.qrakn.honcho.command.adapter.impl;

import com.qrakn.honcho.command.adapter.CommandTypeAdapter;

import java.util.HashMap;
import java.util.Map;

public class BooleanTypeAdapter implements CommandTypeAdapter {
    private static final Map<String, Boolean> MAP;

    static {
        (MAP = new HashMap<>()).put("true", Boolean.TRUE);
        BooleanTypeAdapter.MAP.put("yes", Boolean.TRUE);
        BooleanTypeAdapter.MAP.put("false", Boolean.FALSE);
        BooleanTypeAdapter.MAP.put("no", Boolean.FALSE);
    }

    @Override
    public <T> T convert(final String string, final Class<T> type) {
        return type.cast(BooleanTypeAdapter.MAP.get(string.toLowerCase()));
    }
}
