package org.vorobel.moneytransfer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationService {
    private static final String PROPERTIES_FILE = "application.properties";

    public static final Properties properties = new Properties();

    //public final boolean isProduction = properties.getProperty("app.production");

    public static void load() throws IOException {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            properties.load(is);
        }
    }
}
