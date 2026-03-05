package net.dimmid.config;

import net.dimmid.Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class Config {
    private static final String CONFIG_FILE_NAME = "config_dev.properties";
    private static final Properties properties = new Properties();
    private static Config instance = null;

    private Config() {
    }

    public static Config load() {
        if (instance != null) {
            return instance;
        }
        synchronized (Config.class) {
            if (instance == null) {
                instance = new Config();
                try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
                    properties.load(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return instance;
        }
    }

    public static Optional<String> getProperty(String key) {
        if (instance == null) {
            instance = Config.load();
        }
        String env = System.getenv(key);
        if (env != null && !env.isBlank()) {
            return Optional.of(env);
        }
        env = properties.getProperty(convertEnvTypeNamingToPropertyNaming(key));
        if (env == null || env.isBlank()) {
            env = null;
        }
        return Optional.ofNullable(env);
    }

    public static String getOrDefault(String key, String defaultValue) throws FileNotFoundException {
        return getProperty(key).orElse(defaultValue);
    }

    private static String convertEnvTypeNamingToPropertyNaming(String key) {
        return key.toLowerCase().replace("_", ".");
    }
}
