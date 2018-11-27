package utils;

import org.slf4j.Logger;
import org.slf4j.impl.Log4jLoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;

public class SeeTestProperties {

    public static final String PROPERTIES_FILENAME = "seetest.properties";
    public static URL SEETEST_IO_APPIUM_URL;
    public static String SEETEST_IO_NETWORK_URL;

    public static String EMBEDDED_SERVER = "local.embedded.server";
    public static String EMBEDDED_SERVER_HOST = "local.embedded.host";
    public static String EMBEDDED_SERVER_RESPONSE = "local.embedded.response";
    public static String EMBEDDED_SERVER_PORT = "local.embedded.port";
    public static String SEETEST_NETWORK_TUNNELPATH = "seetest.network.tunnelpath";

    public static String pathToProperties;

    static {
        try {
            SEETEST_IO_APPIUM_URL = new URL("https://cloud.seetest.io/wd/hub");
            SEETEST_IO_NETWORK_URL = "https://cloud.seetest.io/ws-istp";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    static Logger LOGGER = new Log4jLoggerFactory().getLogger("SeeTestProperties");

    public static class Names {
        public static final String ANDROID_APP_NAME = "android.app.name";
        public static final String IOS_APP_NAME = "ios.app.name";

    }

    /**
     * Loads properties.
     */
    public static Properties getSeeTestProperties() {
        Properties properties = new Properties();
        LOGGER.info("Enter loadInitProperties() ...");

        pathToProperties = Objects.requireNonNull(SeeTestProperties.class.getClassLoader().getResource(PROPERTIES_FILENAME)).getFile();

        try (FileReader fr = new FileReader(pathToProperties)) {
            properties.load(fr);
        } catch (IOException e) {
            LOGGER.warn("Could not load init properties", pathToProperties, e);
            throw new RuntimeException("Could not init properties from path!", e);
        }
        LOGGER.info("Loading properties from .. " + pathToProperties);
        LOGGER.info("--------- List Of Properties ---------");
        properties.forEach((key, value) -> LOGGER.info("Key = " + key + " ; Value = " + value));
        LOGGER.info("--------- END ---------");

        LOGGER.info("Exit loadInitProperties() ...");
        return properties;
    }
}
