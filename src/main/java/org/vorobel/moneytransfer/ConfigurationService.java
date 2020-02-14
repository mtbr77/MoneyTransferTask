package org.vorobel.moneytransfer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//TODO: delete
public class ConfigurationService {
    private static final String PROPERTIES_FILE = "application.properties";

    public static Properties properties;

    static {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    //public final boolean isProduction = properties.getProperty("app.production");
    public static int getServicePort() {
        return Integer.parseInt(properties.getProperty("httpserver.port"));
    };

    public static boolean isSwaggerNeeded() {
        return Boolean.parseBoolean(properties.getProperty("swagger.ui"));
    }
}
