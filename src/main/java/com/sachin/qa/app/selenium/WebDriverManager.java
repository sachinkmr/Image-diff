package com.sachin.qa.app.selenium;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverLogLevel;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;

import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;

public class WebDriverManager {
	static {
		System.setProperty("webdriver.chrome.driver", "servers/chromedriver.exe");
		System.setProperty("webdriver.ie.driver", "servers/IEDriverServer.exe");
		System.setProperty("webdriver.gecko.driver", "servers/geckodriver.exe");
		System.setProperty("webdriver.edge.driver", "servers/MicrosoftWebDriver.exe");
		System.setProperty("phantomjs.binary.path", "servers/phantomjs.exe");
	}
	protected static final Logger logger = LoggerFactory.getLogger(WebDriverManager.class);
	private BrowserMobProxy proxy;
	private WebDriver driver;
	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public WebDriverManager() {
		proxy = new BrowserMobProxyServer();
		proxy.setTrustAllServers(true);
		proxy.setConnectTimeout(30, TimeUnit.SECONDS);
		if (null != AppConstants.USERNAME && !AppConstants.USERNAME.isEmpty())
			proxy.addRequestFilter((request, contents, messageInfo) -> {
				final String login = AppConstants.USERNAME + ":" + AppConstants.PASSWORD;
				final String base64login = new String(Base64.encodeBase64(login.getBytes()));
				request.headers().add("Authorization", "Basic " + base64login);
				return null;
			});
		proxy.start(0);
	}

	/***
	 * @author Sachin
	 *         <p>
	 *         This method returns instance of chrome driver
	 *         </p>
	 * 
	 * @return WebDriver
	 * 
	 ***/
	public WebDriver getChromeDriver() {
		ChromeOptions options = new ChromeOptions();
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setJavascriptEnabled(true);
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		if (null != AppConstants.USERNAME && !AppConstants.USERNAME.isEmpty())
			capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		capabilities.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		capabilities.setCapability(CapabilityType.LOGGING_PREFS, getLoggingLevel());
		driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		return driver;
	}

	/**
	 * @author Sachin
	 *         <p>
	 *         This method returns instance of Firefox driver
	 *         </p>
	 * 
	 * @return WebDriver
	 * 
	 **/
	public WebDriver getFireFoxDriver() {
		FirefoxProfile profile = new FirefoxProfile();
		try {
			JavaScriptError.addExtension(profile);
		} catch (IOException e) {
			LoggerFactory.getLogger(WebDriverManager.class).debug("ubable to add extention", e);
		}
		profile.setPreference("network.proxy.http", "localhost");
		profile.setPreference("network.proxy.http_port", proxy.getPort());
		profile.setPreference("network.proxy.ssl", "localhost");
		profile.setPreference("network.proxy.ssl_port", proxy.getPort());
		profile.setPreference("network.proxy.type", 1);
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		capabilities.setCapability(FirefoxDriver.PROFILE, profile);
		if (null != AppConstants.USERNAME && !AppConstants.USERNAME.isEmpty())
			capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		capabilities.setJavascriptEnabled(true);
		capabilities.setCapability("takesScreenshot", true);
		capabilities.setCapability(CapabilityType.LOGGING_PREFS, getLoggingLevel());
		driver = new FirefoxDriver(capabilities);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return driver;
	}

	/**
	 * @author Sachin
	 *         <p>
	 *         Method returns instance of proxy driver
	 *         </p>
	 * 
	 * @return BrowserMobProxy
	 * 
	 **/

	public WebDriver getIEDriver() {
		DesiredCapabilities capabilitiesIE = DesiredCapabilities.internetExplorer();
		capabilitiesIE.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capabilitiesIE.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		capabilitiesIE.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
		capabilitiesIE.setCapability(CapabilityType.LOGGING_PREFS, getLoggingLevel());
		if (null != AppConstants.USERNAME && !AppConstants.USERNAME.isEmpty())
			capabilitiesIE.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		InternetExplorerDriverService.Builder serviceBuilder = new InternetExplorerDriverService.Builder();
		serviceBuilder.usingAnyFreePort();
		serviceBuilder.usingDriverExecutable(new File(System.getProperty("webdriver.ie.driver")));
		serviceBuilder.withLogLevel(InternetExplorerDriverLogLevel.TRACE);
		serviceBuilder.withLogFile(new File("Logs/IELogs.txt"));
		InternetExplorerDriverService service = serviceBuilder.build();
		driver = new InternetExplorerDriver(service, capabilitiesIE);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return driver;
	}

	public WebDriver getPhantumDriver() {
		DesiredCapabilities caps = DesiredCapabilities.phantomjs();
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", true);
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX, "Y");
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, addCommandLineArguments());
		if (null != AppConstants.USERNAME && !AppConstants.USERNAME.isEmpty())
			caps.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		caps.setCapability(CapabilityType.LOGGING_PREFS, getLoggingLevel());
		driver = new PhantomJSDriver(caps);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return driver;
	}

	public WebDriver getEdgeDriver() {
		DesiredCapabilities cap = DesiredCapabilities.edge();
		cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		cap.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		cap.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
		if (null != AppConstants.USERNAME && !AppConstants.USERNAME.isEmpty())
			cap.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		cap.setCapability(CapabilityType.LOGGING_PREFS, getLoggingLevel());
		driver = new EdgeDriver(cap);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return driver;
	}

	public WebDriver getiPhoneDriver() {
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", "Apple iPhone 6");
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		if (null != AppConstants.USERNAME && !AppConstants.USERNAME.isEmpty())
			capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		capabilities.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		capabilities.setCapability(CapabilityType.LOGGING_PREFS, getLoggingLevel());
		driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return driver;
	}

	public WebDriver getiPadDriver() {
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", "Apple iPad");
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		if (null != AppConstants.USERNAME && !AppConstants.USERNAME.isEmpty())
			capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		capabilities.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		capabilities.setCapability(CapabilityType.LOGGING_PREFS, getLoggingLevel());
		driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return driver;
	}

	public WebDriver getAndriodDriver() {
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", "Google Nexus 6");
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		if (null != AppConstants.USERNAME && !AppConstants.USERNAME.isEmpty())
			capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		capabilities.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		capabilities.setCapability(CapabilityType.LOGGING_PREFS, getLoggingLevel());
		driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return driver;
	}

	public BrowserMobProxy getProxy() {
		return this.proxy;
	}

	private List<String> addCommandLineArguments() {
		List<String> cliArgsCap = new ArrayList<>();
		cliArgsCap.add("--ignore-ssl-errors=yes");
		return cliArgsCap;
	}

	public void close() {
		if (null != driver) {
			driver.quit();
		}
		if (null != proxy) {
			proxy.stop();
		}
	}

	public WebDriver getWebDriver() {
		return driver;
	}

	private LoggingPreferences getLoggingLevel() {
		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.BROWSER, Level.ALL);
		return logPrefs;
	}
}
