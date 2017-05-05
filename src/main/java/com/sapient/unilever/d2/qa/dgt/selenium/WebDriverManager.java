package com.sapient.unilever.d2.qa.dgt.selenium;

import java.io.File;
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

import com.sapient.unilever.d2.qa.dgt.AppConstants;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

public class WebDriverManager {
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

		if (null != AppConstants.USERNAME && !AppConstants.USERNAME.isEmpty()) {
			proxy = new BrowserMobProxyServer();
			proxy.setTrustAllServers(true);
			proxy.setConnectTimeout(30, TimeUnit.SECONDS);
			proxy.addRequestFilter(new RequestFilter() {
				@Override
				public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents,
						HttpMessageInfo messageInfo) {
					final String login = AppConstants.USERNAME + ":" + AppConstants.PASSWORD;
					final String base64login = new String(Base64.encodeBase64(login.getBytes()));
					request.headers().add("Authorization", "Basic " + base64login);
					return null;
				}
			});
			proxy.start(0);
		}

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
	public WebDriver getChromeDriver() throws Exception {
		ChromeOptions options = new ChromeOptions();
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setJavascriptEnabled(true);
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		if (null != proxy && proxy.isStarted())
			capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		capabilities.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		capabilities.setCapability(CapabilityType.LOGGING_PREFS, getLoggingLevel());
		driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(AppConstants.PAGE_TIMEOUT, TimeUnit.MILLISECONDS);
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
	public WebDriver getFireFoxDriver() throws Exception {
		FirefoxProfile profile = new FirefoxProfile(new File(System.getProperty("firefox.automation.profile.path")));
		if (null != proxy && proxy.isStarted()) {
			profile.setPreference("network.proxy.http", "localhost");
			profile.setPreference("network.proxy.http_port", proxy.getPort());
			profile.setPreference("network.proxy.ssl", "localhost");
			profile.setPreference("network.proxy.ssl_port", proxy.getPort());
			profile.setPreference("network.proxy.type", 1);
		}
		profile.setPreference("setAcceptUntrustedCertificates", "true");
		profile.setPreference("setAssumeUntrustedCertificateIssuer", "false");
		profile.setPreference("acceptSslCerts", "true");
		profile.setAcceptUntrustedCertificates(true);
		profile.setAssumeUntrustedCertificateIssuer(false);
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		capabilities.setCapability(FirefoxDriver.PROFILE, profile);
		if (null != proxy && proxy.isStarted())
			capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		capabilities.setJavascriptEnabled(true);
		capabilities.setCapability("takesScreenshot", true);
		capabilities.setCapability(CapabilityType.LOGGING_PREFS, getLoggingLevel());
		driver = new FirefoxDriver(capabilities);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(AppConstants.PAGE_TIMEOUT, TimeUnit.MILLISECONDS);
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

	public WebDriver getIEDriver() throws Exception {
		DesiredCapabilities capabilitiesIE = DesiredCapabilities.internetExplorer();
		capabilitiesIE.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capabilitiesIE.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		capabilitiesIE.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
		capabilitiesIE.setCapability(CapabilityType.LOGGING_PREFS, getLoggingLevel());
		if (null != proxy && proxy.isStarted())
			capabilitiesIE.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		InternetExplorerDriverService.Builder serviceBuilder = new InternetExplorerDriverService.Builder();
		serviceBuilder.usingAnyFreePort();
		serviceBuilder.usingDriverExecutable(new File(System.getProperty("webdriver.ie.driver")));
		serviceBuilder.withLogLevel(InternetExplorerDriverLogLevel.TRACE);
		serviceBuilder.withLogFile(new File("Logs/IELogs.txt"));
		InternetExplorerDriverService service = serviceBuilder.build();
		driver = new InternetExplorerDriver(service, capabilitiesIE);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(AppConstants.PAGE_TIMEOUT, TimeUnit.MILLISECONDS);
		return driver;
	}

	public WebDriver getPhantomDriver() throws Exception {
		DesiredCapabilities caps = DesiredCapabilities.phantomjs();
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", true);
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX, "Y");
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, addCommandLineArguments());
		if (null != proxy && proxy.isStarted())
			caps.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		caps.setCapability(CapabilityType.LOGGING_PREFS, getLoggingLevel());
		driver = new PhantomJSDriver(caps);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(AppConstants.PAGE_TIMEOUT, TimeUnit.MILLISECONDS);
		return driver;
	}

	public WebDriver getEdgeDriver() throws Exception {
		DesiredCapabilities cap = DesiredCapabilities.edge();
		cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		cap.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		cap.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
		if (null != proxy && proxy.isStarted())
			cap.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		cap.setCapability(CapabilityType.LOGGING_PREFS, getLoggingLevel());
		driver = new EdgeDriver(cap);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(AppConstants.PAGE_TIMEOUT, TimeUnit.MILLISECONDS);
		return driver;
	}

	public WebDriver getiPhoneDriver() throws Exception {
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", "Apple iPhone 6");
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		if (null != proxy && proxy.isStarted())
			capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		capabilities.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		capabilities.setCapability(CapabilityType.LOGGING_PREFS, getLoggingLevel());
		driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(AppConstants.PAGE_TIMEOUT, TimeUnit.MILLISECONDS);
		return driver;
	}

	public WebDriver getiPadDriver() throws Exception {
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", "Apple iPad");
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		if (null != proxy && proxy.isStarted())
			capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		capabilities.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		capabilities.setCapability(CapabilityType.LOGGING_PREFS, getLoggingLevel());
		driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(AppConstants.PAGE_TIMEOUT, TimeUnit.MILLISECONDS);
		return driver;
	}

	public WebDriver getAndriodDriver() throws Exception {
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", "Google Nexus 6");
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		if (null != proxy && proxy.isStarted())
			capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		capabilities.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		capabilities.setCapability(CapabilityType.LOGGING_PREFS, getLoggingLevel());
		driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(AppConstants.PAGE_TIMEOUT, TimeUnit.MILLISECONDS);
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
		if (null != proxy && proxy.isStarted()) {
			proxy.stop();
		}
		if (null != driver) {
			driver.quit();
		}
	}

	public WebDriver getWebDriver() {
		return driver;
	}

	private LoggingPreferences getLoggingLevel() {
		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.BROWSER, Level.ALL);
		logPrefs.enable(LogType.CLIENT, Level.OFF);
		logPrefs.enable(LogType.DRIVER, Level.OFF);
		logPrefs.enable(LogType.PERFORMANCE, Level.OFF);
		logPrefs.enable(LogType.PROFILER, Level.OFF);
		logPrefs.enable(LogType.SERVER, Level.OFF);
		return logPrefs;
	}

}
