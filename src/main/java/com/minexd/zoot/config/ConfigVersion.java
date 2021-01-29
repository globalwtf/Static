package com.minexd.zoot.config;

import com.minexd.zoot.config.impl.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConfigVersion {

    VERSION_1(1, new ConfigConversion1()),
    VERSION_2(2, new ConfigConversion2()),
    VERSION_3(3, new ConfigConversion3()),
    VERSION_4(4, new ConfigConversion4()),
    VERSION_5(5, new ConfigConversion5()),
    VERSION_6(6, new ConfigConversion6());

    private int number;
    private ConfigConversion conversion;

}
