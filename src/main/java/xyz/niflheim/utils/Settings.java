package xyz.niflheim.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Settings {
    public static Config Config = ConfigFactory.load();

    public static final int instances = Config.getInt("Client.instances");

    public static final String
            platform = Config.getString("Client.platform"),
            variant = Config.getString("Client.variant");
}
