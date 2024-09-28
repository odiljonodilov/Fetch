package Fetch.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties;
    public static final String BASE_URL;
    static {
        try (FileInputStream fileInputStream = new FileInputStream("config.properties")) {
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load configuration file.");
        }
        BASE_URL = properties.getProperty("geo_api_base_url");
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

}
