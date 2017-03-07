package com.sachin.qa.app.selenium;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverLogLevel;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;

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

	public WebDriverManager() {
		proxy = new BrowserMobProxyServer();
		proxy.setTrustAllServers(true);
		proxy.setConnectTimeout(30, TimeUnit.SECONDS);
		if (!AppConstants.USERNAME.isEmpty())
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
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		if (null != AppConstants.USERNAME && !AppConstants.USERNAME.isEmpty())
			capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		capabilities.setJavascriptEnabled(true);
		capabilities.setCapability("takesScreenshot", true);
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
		driver = new PhantomJSDriver(caps);
		if (null != AppConstants.USERNAME && !AppConstants.USERNAME.isEmpty()) {
			caps.setCapability("phantomjs.page.settings.userName", AppConstants.USERNAME);
			caps.setCapability("phantomjs.page.settings.password", AppConstants.PASSWORD);
		}
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
		driver = new EdgeDriver(cap);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return driver;
	}

	public WebDriver getiPhoneDriver() {
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", "iPhone 6");
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		if (null != AppConstants.USERNAME && !AppConstants.USERNAME.isEmpty())
			capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		capabilities.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return driver;
	}

	public WebDriver getiPadDriver() {
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", "iPad");
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		if (null != AppConstants.USERNAME && !AppConstants.USERNAME.isEmpty())
			capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		capabilities.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return driver;
	}

	public WebDriver getAndriodDriver() {
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", "Nexus 6P");
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		if (null != AppConstants.USERNAME && !AppConstants.USERNAME.isEmpty())
			capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		capabilities.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return driver;
	}

	public BrowserMobProxy getProxy() {
		return this.proxy;
	}

	/**
	 * This method is used to kill all the processes running of servers if any
	 * is running as system processes.
	 *
	 * @author Sachin
	 **/
	public void killService(String serviceName) {
		try {
			if (ProcessKiller.isProcessRunning(serviceName)) {
				ProcessKiller.killProcess(serviceName);
			}
		} catch (Exception ex) {
			logger.warn("Unable to close chrome server.", ex);
		}
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
		killService("chromedriver.exe");
		killService("IEDriverServer.exe");
		killService("geckodriver.exe");
		killService("MicrosoftWebDriver.exe");
		killService("phantomjs.exe");
	}
}
