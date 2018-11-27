package networktunnel;


import org.eclipse.jetty.server.Server;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.impl.Log4jLoggerFactory;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import utils.SeeTestProperties;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.UnknownHostException;

import static utils.SeeTestProperties.SEETEST_NETWORK_TUNNELPATH;
import static utils.SeeTestProperties.pathToProperties;


/**
 * Network Tunnel Test for Seetest.
 *
 *
 * USAGE
 *
 * set ENVIORNMENT variables
 *
 * SEETEST_IO_ACCESS_KEY -> access key of Valid user of seetest cloud.
 *
 * OS -> "android" or "ios"
 *
 * LOCAL_APP -> Local Web URL
 *
 * EXPECTED_VALUE -> Expected value for the test
 *
 * XPATH_QUERY -> Element to test (with text value against which EXPECTED_VALUE will be asserted)
 *
 *
 *
 */
public class NetworkTunnelTest extends TestBase {

    private static final String ENV_VAR_ACCESS_KEY = "SEETEST_IO_ACCESS_KEY";
    private static final String LOCAL_APP_URL = "local.app.url";
    private static final String XPATH_QUERY = "xpath.query";
    private static final String EXPECTED_VALUE = "expected.value";

    public static final boolean FULL_RESET = true;
    private Logger LOGGER = new Log4jLoggerFactory().getLogger(this.getClass().getName());
    private Process process = null;
    private Server server = null;
    private String localApplicationUrl;


    /**
     * Core setup function, which sets up the selenium/appium drivers.
     * @param testContext Test Context for the Test.
     */
    @Parameters("os")
    @BeforeClass
    public void setUp(@Optional("android") String os, ITestContext testContext) {
        LOGGER.info("Enter TestBase setUp");
        properties = SeeTestProperties.getSeeTestProperties();
        accessKey = System.getenv(ENV_VAR_ACCESS_KEY);
        if (accessKey == null || accessKey.length() < 10) {
            LOGGER.error("Access key must be set in Environment variable SEETEST_IO_ACCESS_KEY");
            LOGGER.info("To get access get to to https://cloud.seetest.io or learn at https://docs.seetest.io/display/SEET/Execute+Tests+on+SeeTest+-+Obtaining+Access+Key", accessKey);
            throw new RuntimeException("Access key invalid : accessKey - " + accessKey);
        }
        // Start Embedded Server.
        boolean embeddedServerEnabled = Boolean.parseBoolean(
                properties.getProperty(SeeTestProperties.EMBEDDED_SERVER, "true"));
        if (embeddedServerEnabled) {
            startJettyWebServer();
        } else {
            localApplicationUrl = properties.getProperty (LOCAL_APP_URL);
        }
        startNetworkTunnel();
        this.os = os;
        dc.setCapability("testName",
                testContext.getCurrentXmlTest().getName() + "." + this.getClass().getSimpleName());
        super.setUp();
        LOGGER.info("Exit TestBase setUp");
    }

    /**
     * Starts the Web Server.
     */
    private void startJettyWebServer() {
        LOGGER.info("--------- Start Embedded HTTP Server ---------");
        String response = properties.getProperty(SeeTestProperties.EMBEDDED_SERVER_RESPONSE);
        int port = Integer.parseInt(properties.getProperty(SeeTestProperties.EMBEDDED_SERVER_PORT));
        String ip = properties.getProperty(SeeTestProperties.EMBEDDED_SERVER_HOST);

        if (ip == null || ip.isEmpty()) {
            StringBuilder error = new StringBuilder();
            error.append("Please configure ")
                    .append(SeeTestProperties.EMBEDDED_SERVER_HOST)
                    .append(" in ")
                    .append(pathToProperties);

            LOGGER.error(error.toString());
            throw new RuntimeException(error.toString());
        }

        try {
            InetSocketAddress addr = InetSocketAddress.createUnresolved(ip,port);
            localApplicationUrl = String.format("http://%s:%s", InetAddress.getByName(ip),port) ;
            server = new Server(addr);
        } catch (UnknownHostException e) {
            LOGGER.error("Unable to start Embedded Java Web Server");
        }
        server.setHandler(new JettyHelloWorldServer(response));
        try {
            server.start();
        } catch (Exception e) {
            LOGGER.error("Unable to start Embedded Java Web Server");
        }
        LOGGER.error("--------- Started Embedded Java Web Server ---------");
    }

    /**
     * Starts the Network Tunnel.
     */
    private void startNetworkTunnel() {

        LOGGER.info("--------- Start Tunneling ---------");
        String networkTunnelFilePath = properties.getProperty(SEETEST_NETWORK_TUNNELPATH);

        if (networkTunnelFilePath == null || networkTunnelFilePath.isEmpty()) {
            StringBuilder error = new StringBuilder();
            error.append("Please configure ")
                    .append(SeeTestProperties.SEETEST_NETWORK_TUNNELPATH)
                    .append(" in ")
                    .append(pathToProperties);
            LOGGER.error(error.toString());
            throw new RuntimeException(error.toString());
        }
        StringBuilder networkTunnelConnectString = new StringBuilder(networkTunnelFilePath);
        networkTunnelConnectString.append(" --url ").append(SeeTestProperties.SEETEST_IO_NETWORK_URL);
        networkTunnelConnectString.append(" --access-key ").append(accessKey);

        // Create and start Process with ProcessBuilder.
        ProcessBuilder pb =
                new ProcessBuilder(networkTunnelFilePath,
                        "--url", SeeTestProperties.SEETEST_IO_NETWORK_URL,
                        "--access-key", accessKey);
        pb.command();

        try {
            process = pb.start();
        } catch (IOException e) {
            LOGGER.error("Unable to start Process - " + networkTunnelConnectString);
            LOGGER.error("Exception .." + e);

        }
        LOGGER.info("Process started - " + networkTunnelConnectString);
        LOGGER.info("--------- Started Tunnel ---------");
    }

    //@Test (threadPoolSize = 3, invocationCount = 9)
    @Test
    public void testSampleNetworkTunnelTest() {
        LOGGER.info("Enter testSampleNetworkTunnelTest()");
        if (localApplicationUrl == null) {
            Assert.fail("Should no fail - local.app.url is not defined or ");
        }
        String expectedValue = properties.getProperty(EXPECTED_VALUE, "Hello");
        String xpathQuery = properties.getProperty(XPATH_QUERY);
        LOGGER.info("Loading Local App = " + localApplicationUrl);
        driver.get(localApplicationUrl);
        LOGGER.info("XPATH Query = " + xpathQuery);

        By searchElementBy = By.xpath(xpathQuery);
        String actualVal = driver.findElement(searchElementBy).getText();
        LOGGER.info("Expected = " + expectedValue + "; Actual = " + actualVal);
        Assert.assertEquals(expectedValue,actualVal);
        LOGGER.info("Exit testSampleNetworkTunnelTest()");
    }

   @AfterClass
    protected void tearDown() {
        LOGGER.info("Enter tearDown()");
        if (process != null) {
            LOGGER.info("destroying network tunnel process");
            process.destroy();
        }
       try {
           server.stop();
       } catch (Exception e) {
           LOGGER.error("Unable to stop Embedded Java Server.");
           LOGGER.error("Manually Embedded Java Server.");
       }

       driver.quit();
    }
}
