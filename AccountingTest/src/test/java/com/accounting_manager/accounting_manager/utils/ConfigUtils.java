package com.accounting_manager.accounting_manager.utils;

import lombok.extern.log4j.Log4j2;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

@Log4j2
/**
 * This class provides utility methods for reading configuration properties.
 */
public class ConfigUtils {

    /**
     * The single instance of this class.
     */
    private static ConfigUtils configUtils;

    /**
     * The property object that holds the configuration properties.
     */
    private Properties prop;

    /**
     * The property object that holds the routes (urls) properties.
     */
    private Properties routesProp;

    /**
     * Private constructor to prevent instantiation of this class.
     * It initializes the properties object by reading the properties file.
     */
    private ConfigUtils() {
        prop = readProp();
        routesProp= readRoutesProp();
    }

    /**
     * This method returns the single instance of this class.
     * If the instance does not exist, it is created.
     *
     * @return The single instance of this class.
     */
    public static ConfigUtils getInstance() {
        if(configUtils == null) {
            configUtils = new ConfigUtils();
        }
        return  configUtils;
    }

    /**
     * This method reads the properties file and returns a Properties object.
     * The properties file to read is determined by the "env" system property.
     *
     * @return A Properties object that holds the configuration properties.
     * @throws RuntimeException if the specified environment is not supported.
     */
    private Properties readProp() {
        InputStream is;
        try {
            String env = System.getProperty("env", "LOCAL");
            switch (env) {
                case "PRODUCTION" -> {
                    is = new FileInputStream("src/test/resources/env/prod.properties");
                }
                case "LOCAL" -> {
                    is = new FileInputStream("src/test/resources/env/local.properties");
                }
                default -> {
                    throw new RuntimeException("Environment is not supported!");
                }
            }
            prop = new Properties();
            prop.load(is);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return prop;
    }

    /**
     * This method reads the properties file and returns a Properties object.
     * @return A Properties object that holds route properties.
     */
    private Properties readRoutesProp() {
        routesProp = new Properties();
        try {
            routesProp.load(new FileInputStream("src/test/resources/env/routes.properties"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return routesProp;
    }

    /**
     * This method returns the URL for a given property.
     * It concatenates the base URL with the value of the specified property.
     *
     * @param property The property for which to return the URL.
     * @return The URL for the specified property.
     */
    public String getUrl(String property) {
        return prop.getProperty("baseUrl")+routesProp.getProperty(property);
    }

    public String getSubUrl(String property) {
        return routesProp.getProperty(property);
    }
}