package networktunnel;

import com.experitest.appium.SeeTestCapabilityType;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.impl.Log4jLoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utils.SeeTestProperties;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class TestBase {
    public static final String ENV_VAR_ACCESS_KEY = "SEETEST_IO_ACCESS_KEY";
    private static final boolean FULL_RESET = true;
    public static final boolean INSTRUMENT_APP = true;

    String accessKey = null;
    DesiredCapabilities dc = new DesiredCapabilities();
    RemoteWebDriver driver = null;
    String os;
    Properties properties;
    private String deviceQuery;
    Logger LOGGER = new Log4jLoggerFactory().getLogger(this.getClass().getName());

    /**
     * Core setup function, which sets up the selenium/appium drivers.
     *
     */

    public void setUp() {
        LOGGER.info("Enter TestBase setUp");
        this.initDefaultDesiredCapabilities();
        driver = os.equals("android") ?
                new AndroidDriver(SeeTestProperties.SEETEST_IO_APPIUM_URL, dc) :
                new IOSDriver(SeeTestProperties.SEETEST_IO_APPIUM_URL, dc);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        LOGGER.info("Driver loaded ..");
    }

    /**
     * Initialize default properties.
     *
     */
    protected void initDefaultDesiredCapabilities() {
        LOGGER.info("Setting up Desired Capabilities");
        dc.setCapability(SeeTestCapabilityType.ACCESS_KEY, accessKey);
        dc.setCapability(MobileCapabilityType.FULL_RESET, FULL_RESET);

        if (os.equals("android"))
            dc.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);
        else
            dc.setCapability(CapabilityType.BROWSER_NAME, BrowserType.SAFARI);

        dc.setCapability(CapabilityType.VERSION, "Any");
        dc.setCapability(CapabilityType.PLATFORM, Platform.ANY);
        String query = String.format("@os='%s'", os);
        dc.setCapability(SeeTestCapabilityType.DEVICE_QUERY, query);
        LOGGER.info("Device Query = {}", query);
        LOGGER.info("Desired Capabilities setup complete");
    }


    @AfterClass
    protected void tearDown() {
        driver.quit();
    }
}
