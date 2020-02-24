package org.vorobel.moneytransfer.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

    public static int getRestServicePort() {
        return Integer.parseInt(properties.getProperty("restserver.port"));
    };

    public static boolean isSwaggerNeeded() {
        return Boolean.parseBoolean(properties.getProperty("swagger.ui"));
    }


    public static String getInitialBalance() {
        return properties.getProperty("tests.load.initialBalance");
    }

    public static String getTransferAmount() {
        return properties.getProperty("tests.load.transferAmount");
    }

    public static long getAccountsNumber() {
        return Long.parseLong(properties.getProperty("tests.load.accountsNumber"));
    }

    public static long getTransfersNumber() {
        return Long.parseLong(properties.getProperty("tests.load.transfersNumber"));
    }
}
