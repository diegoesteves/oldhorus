package org.aksw.horus;

import org.ini4j.Ini;

/**
 * Created by dnes on 12/04/16.
 */
public class HorusConfig {

    private Ini horusConfig;

    public static String HORUS_DATA_DIR;

    public HorusConfig(Ini config) {

        this.horusConfig =  config;
        HORUS_DATA_DIR = this.horusConfig.get("eval", "data-directory");
    }

    public boolean getBooleanSetting(String section, String key) {

        return Boolean.valueOf(horusConfig.get(section, key));
    }

    public String getStringSetting(String section, String key) {

        return horusConfig.get(section, key);
    }

    public void setStringSetting(String section, String key, String value) {

        this.horusConfig.put(section, key, value);
    }

    public Integer getIntegerSetting(String section, String key) {

        return Integer.valueOf(this.horusConfig.get(section, key));
    }

    public Double getDoubleSetting(String section, String key) {

        return Double.valueOf(this.horusConfig.get(section, key));
    }

}
