package com.sapient.unilever.d2.qa.dgt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.page.D2Page;
import com.sapient.unilever.d2.qa.dgt.selenium.Browser;
import com.sapient.unilever.d2.qa.dgt.utils.HelperUtils;
import com.sapient.unilever.d2.qa.dgt.utils.NetUtils;

public class AppConstants {

    public static String BRAND_NAME, SITE;
    public static String USERNAME;
    public static String PASSWORD;
    public static Properties PROPERTIES;
    public static List<String> SKIPPED_URLS;
    public static String APP_DATA, URL_TEXT;
    public static Pattern PATTERN;
    public static Pattern SHOULD_VISIT_PATTERN;
    public static Pattern IMAGE_PATTERN;
    public static Pattern ASSETS_PATTERN;
    public static String USER_AGENT, PRE_DATA;
    public static long START_TIME, END_TIME;
    public static String ERROR_TEXT, DIFF_FOLDER, FOLDER, BUILD_VERSION, PRE_BUILD, PRE_TIME;
    public static boolean ERROR;
    public static final String TIME_STAMP;
    public static boolean URL_IS_CASE_SENSITIVE, HAS_DIFF, IMAGE_DIFF, JS_DIFF, HTML_DIFF, WEB;
    public static int IGNORED_PIXELS;
    public static int HEADER_PIXELS;
    public static int FOOTER_PIXELS;
    public static int PAGE_WAIT;
    public static int SCROLL_DELAY, PAGE_TIMEOUT;
    public static int BROWSER_INSTANCE = 1;
    public static Set<Browser> BROWSERS;
    public static Map<WebDriver, Boolean> DRIVERS;
    public static Set<D2Page> PAGES;
    public static BuildType BUILD_TYPE;

    static {
	TIME_STAMP = HelperUtils.generateTimeString();
	PAGES = new CopyOnWriteArraySet<>(new HashSet<>());
	WEB = System.getProperty("WebAppDirectory") != null && !System.getProperty("WebAppDirectory").isEmpty();
	if (WEB) {
	    System.setProperty("app.dir", System.getProperty("WebAppDirectory"));
	} else {
	    System.setProperty("app.dir", System.getProperty("user.dir"));
	}
	BUILD_TYPE = !StringUtils.isBlank(System.getProperty("BuildType"))
		&& System.getProperty("BuildType").equalsIgnoreCase("pre") ? BuildType.PRE : BuildType.POST;
	BRAND_NAME = System.getProperty("BrandName");

	IMAGE_DIFF = System.getProperty("imageDiff") != null && !System.getProperty("imageDiff").isEmpty()
		&& System.getProperty("imageDiff").equalsIgnoreCase("Yes");
	JS_DIFF = System.getProperty("jsDiff") != null && !System.getProperty("jsDiff").isEmpty()
		&& System.getProperty("jsDiff").equalsIgnoreCase("Yes");
	HTML_DIFF = System.getProperty("htmlDiff") != null && !System.getProperty("htmlDiff").isEmpty()
		&& System.getProperty("htmlDiff").equalsIgnoreCase("Yes");
	if (BUILD_TYPE == BuildType.PRE) {
	    SITE = System.getProperty("SiteAddress");
	    URL_TEXT = !StringUtils.isBlank(System.getProperty("UrlsTextFile")) ? System.getProperty("UrlsTextFile")
		    : "";
	    if (System.getProperty("UrlsTextFile") != null && !System.getProperty("UrlsTextFile").isEmpty()) {
		URL_TEXT = System.getProperty("UrlsTextFile");
	    } else if (System.getProperty("UrlsTextFileLocation") != null
		    && !System.getProperty("UrlsTextFileLocation").isEmpty()) {
		URL_TEXT = System.getProperty("UrlsTextFileLocation");
	    } else {
		URL_TEXT = "";
	    }

	    USERNAME = System.getProperty("Username");
	    PASSWORD = System.getProperty("Password");

	    try {
		BUILD_VERSION = getBuildVersion();
	    } catch (IOException e) {
		LoggerFactory.getLogger(AppConstants.class).error("Unable to get branch version", e);
	    }
	    File storage = new File(System.getProperty("app.dir") + File.separator + "data" + File.separator
		    + BRAND_NAME + File.separator + "Data" + File.separator + BUILD_VERSION);
	    FOLDER = storage.getAbsolutePath() + File.separator + BUILD_TYPE + File.separator + TIME_STAMP;
	    FOLDER = createFolder(FOLDER);
	} else {
	    PRE_BUILD = !StringUtils.isBlank(System.getProperty("PreBuildVersion"))
		    ? System.getProperty("PreBuildVersion") : "";
	    PRE_TIME = !StringUtils.isBlank(System.getProperty("PreBuildTime")) ? System.getProperty("PreBuildTime")
		    : "";
	    PRE_DATA = System.getProperty("app.dir") + File.separator + "data" + File.separator + BRAND_NAME
		    + File.separator + "Data" + File.separator + PRE_BUILD + File.separator + "PRE" + File.separator
		    + PRE_TIME;

	    readParam();
	    SITE = System.getProperty("SiteAddress");
	    URL_TEXT = !StringUtils.isBlank(System.getProperty("UrlsTextFile")) ? System.getProperty("UrlsTextFile")
		    : "";
	    if (System.getProperty("UrlsTextFile") != null && !System.getProperty("UrlsTextFile").isEmpty()) {
		URL_TEXT = System.getProperty("UrlsTextFile");
	    } else if (System.getProperty("UrlsTextFileLocation") != null
		    && !System.getProperty("UrlsTextFileLocation").isEmpty()) {
		URL_TEXT = System.getProperty("UrlsTextFileLocation");
	    } else {
		URL_TEXT = "";
	    }

	    IMAGE_DIFF = System.getProperty("imageDiff") != null && !System.getProperty("imageDiff").isEmpty()
		    && System.getProperty("imageDiff").equalsIgnoreCase("Yes");
	    JS_DIFF = System.getProperty("jsDiff") != null && !System.getProperty("jsDiff").isEmpty()
		    && System.getProperty("jsDiff").equalsIgnoreCase("Yes");
	    HTML_DIFF = System.getProperty("htmlDiff") != null && !System.getProperty("htmlDiff").isEmpty()
		    && System.getProperty("htmlDiff").equalsIgnoreCase("Yes");
	    HAS_DIFF = (IMAGE_DIFF || HTML_DIFF || JS_DIFF);
	    USERNAME = System.getProperty("Username");
	    PASSWORD = System.getProperty("Password");
	    try {
		BUILD_VERSION = getBuildVersion();
	    } catch (IOException e) {
		LoggerFactory.getLogger(AppConstants.class).error("Unable to get branch version", e);
	    }
	    File storage = new File(System.getProperty("app.dir") + File.separator + "data" + File.separator
		    + BRAND_NAME + File.separator + "Data" + File.separator + BUILD_VERSION);
	    FOLDER = storage.getAbsolutePath() + File.separator + BUILD_TYPE + File.separator + TIME_STAMP;
	    FOLDER = createFolder(FOLDER);

	    if (HAS_DIFF) {
		DIFF_FOLDER = System.getProperty("app.dir") + File.separator + "data" + File.separator + BRAND_NAME
			+ File.separator + "Difference" + File.separator + "Pre_" + PRE_BUILD + "_" + PRE_TIME
			+ File.separator + "Post_" + BUILD_VERSION + "_" + TIME_STAMP;
		DIFF_FOLDER = createFolder(DIFF_FOLDER);
	    }
	}
	boolean flag = false;
	try {
	    HelperUtils.validate();
	    flag = true;
	} catch (Exception e1) {
	    LoggerFactory.getLogger(AppConstants.class).error("Error in initialization", e1);
	}
	if (flag) {
	    DRIVERS = new HashMap<>();
	    PROPERTIES = new Properties();
	    SKIPPED_URLS = new ArrayList<>();
	    String PROPERTIES_LOC = "";

	    if (System.getProperty("CrawlerConfigFile") != null && !System.getProperty("CrawlerConfigFile").isEmpty()) {
		LoggerFactory.getLogger(AppConstants.class).info("Loading user's config file");
		PROPERTIES_LOC = System.getProperty("CrawlerConfigFile");
	    } else if (System.getProperty("CrawlerConfig") != null && !System.getProperty("CrawlerConfig").isEmpty()) {
		LoggerFactory.getLogger(AppConstants.class)
			.info("Loading user's config file provided from jenkins interface");
		PROPERTIES_LOC = HelperUtils.getResourceFile("Config.properties", "CrawlerConfig");
	    } else {
		LoggerFactory.getLogger(AppConstants.class).info("Loading default config file");
		PROPERTIES_LOC = HelperUtils.getResourceFile("Config.properties");
	    }

	    // Loading crawler Config file
	    LoggerFactory.getLogger(AppConstants.class).info("Property file loaded from: " + PROPERTIES_LOC);
	    try {
		Scanner in = new Scanner(new FileInputStream(new File(PROPERTIES_LOC)));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while (in.hasNext()) {
		    out.write(in.nextLine().replace("\\", "\\\\").getBytes());
		    out.write("\n".getBytes());
		}
		InputStream is = new ByteArrayInputStream(out.toByteArray());
		PROPERTIES.load(is);
		in.close();
		String[] sites = PROPERTIES.getProperty("crawler.skipDomains").trim().split(",");
		for (String site : sites) {
		    SKIPPED_URLS.add(new URL(site).getHost().replaceAll("www.", ""));
		}
	    } catch (IOException e) {
		LoggerFactory.getLogger(AppConstants.class).error("Error in loading config file", e);
	    }

	    ASSETS_PATTERN = Pattern.compile("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp|js|css)))",
		    Pattern.CASE_INSENSITIVE);
	    IMAGE_PATTERN = Pattern.compile("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp)))", Pattern.CASE_INSENSITIVE);
	    PATTERN = Pattern.compile(PROPERTIES.getProperty("crawler.domainRegex", ".").trim(),
		    Pattern.CASE_INSENSITIVE);
	    SHOULD_VISIT_PATTERN = Pattern.compile(PROPERTIES.getProperty("crawler.linksToVisit", ".").trim(),
		    Pattern.CASE_INSENSITIVE);
	    USER_AGENT = PROPERTIES.getProperty("crawler.userAgentString",
		    "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");
	    URL_IS_CASE_SENSITIVE = Boolean.parseBoolean(PROPERTIES.getProperty("crawler.caseSensitiveUrl", "false"));
	    HEADER_PIXELS = Integer.parseInt(PROPERTIES.getProperty("image.ignore.header.pixels", "0"));
	    FOOTER_PIXELS = Integer.parseInt(PROPERTIES.getProperty("image.ignore.footer.pixels", "0"));
	    IGNORED_PIXELS = Integer.parseInt(PROPERTIES.getProperty("image.ignore.pixel", "0"));
	    PAGE_WAIT = Integer.parseInt(PROPERTIES.getProperty("page.screen.wait", "1000"));
	    SCROLL_DELAY = Integer.parseInt(PROPERTIES.getProperty("page.scroll.delay", "1000"));
	    PAGE_TIMEOUT = Integer.parseInt(PROPERTIES.getProperty("page.time.out", "30000"));
	    // browsers info instantiation

	    String browserLoc = "";
	    if (System.getProperty("BrowserConfigFile") != null && !System.getProperty("BrowserConfigFile").isEmpty()) {
		LoggerFactory.getLogger(AppConstants.class).info("Loading user's browser file");
		browserLoc = System.getProperty("BrowserConfigFile");
	    } else if (System.getProperty("BrowserConfig") != null && !System.getProperty("BrowserConfig").isEmpty()) {
		LoggerFactory.getLogger(AppConstants.class)
			.info("Loading user's browser file provided from jenkins interface");
		browserLoc = HelperUtils.getResourceFile("browsers.json", "BrowserConfig");
	    } else {
		LoggerFactory.getLogger(AppConstants.class).info("Loading default browser file");
		browserLoc = HelperUtils.getResourceFile("browsers.json");
	    }
	    try {
		JSONObject BROWSER_PROPERTIES = new JSONObject(
			FileUtils.readFileToString(new File(browserLoc), "UTF-8"));
		BROWSER_INSTANCE = BROWSER_PROPERTIES.getInt("instances");
		BROWSERS = HelperUtils.readBrowsers(BROWSER_PROPERTIES);
		BROWSER_PROPERTIES = null;
	    } catch (JSONException | IOException e) {
		LoggerFactory.getLogger(AppConstants.class).error("Error in loading browser file", e);
	    }
	    // if (BUILD_TYPE == BuildType.PRE) {
	    // saveParam();
	    // }
	    try {
		HelperUtils.loadWebDriverServers();
	    } catch (Exception e) {
		LoggerFactory.getLogger(AppConstants.class).info("Error loading browser servers from property file.");
		LoggerFactory.getLogger(AppConstants.class).info("Falling back to in build servers.");
		System.setProperty("webdriver.chrome.driver", "servers/chromedriver.exe");
		System.setProperty("webdriver.ie.driver", "servers/IEDriverServer.exe");
		System.setProperty("webdriver.gecko.driver", "servers/geckodriver.exe");
		System.setProperty("webdriver.edge.driver", "servers/MicrosoftWebDriver.exe");
		System.setProperty("phantomjs.binary.path", "servers/phantomjs.exe");
	    }
	}
    }

    private static String createFolder(String bEFORE_FOLDER2) {
	File file = new File(bEFORE_FOLDER2);
	file.mkdirs();
	return file.getAbsolutePath();
    }

    public static void saveParam() {
	Map<String, String> map = new HashMap<>();
	Properties p = System.getProperties();
	for (String key : p.stringPropertyNames()) {
	    map.put(key, System.getProperty(key));
	}
	if (!StringUtils.isEmpty(SITE)) {
	    File file = new File(FOLDER, "urls.txt");
	    map.put("UrlsTextFileLocation", file.getAbsolutePath());
	    System.setProperty("SiteAddress", "");
	    writeUrlsToFile(file);
	}
	p = null;
	try {
	    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(FOLDER, "Suite.data")));
	    out.writeObject(map);
	    out.close();
	} catch (IOException e) {
	    LoggerFactory.getLogger(AppConstants.class).error("Unable to store suite properties", e);
	}

    }

    /**
     * @param file
     */
    private static void writeUrlsToFile(File file) {
	List<String> list = new ArrayList<>();
	for (D2Page page : PAGES) {
	    list.add(page.getSite());
	}
	Collections.sort(list);
	try {
	    FileUtils.writeLines(file, "UTF-8", list);
	} catch (IOException e) {
	    LoggerFactory.getLogger(AppConstants.class).error("Unable to store urls", e);
	}
    }

    @SuppressWarnings("unchecked")
    private static void readParam() {
	LoggerFactory.getLogger(AppConstants.class).info("Reading properties from previuos build");
	try {
	    ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File(PRE_DATA, "Suite.data")));
	    Map<String, String> map = (Map<String, String>) is.readObject();
	    is.close();
	    map.remove("BuildType");
	    map.remove("PreBuildVersion");
	    map.remove("PreBuildTime");
	    for (String key : map.keySet()) {
		try {
		    System.setProperty(key, map.get(key));
		} catch (Exception e) {
		}
	    }
	} catch (IOException | ClassNotFoundException e) {
	    LoggerFactory.getLogger(AppConstants.class).error("Unable to read suite properties", e);
	}
    }

    private static String getBuildVersion() throws IOException {
	try {
	    String url = !StringUtils.isBlank(SITE) ? SITE : FileUtils.readLines(new File(URL_TEXT), "utf-8").get(0);
	    String document = NetUtils.getURLHtml(url, USERNAME, PASSWORD);
	    document = document.substring(document.indexOf("<html"), document.indexOf("<head"));
	    int in = document.indexOf("<!--\"Release version - ");
	    document = document.substring(in).replaceAll("<!--\"Release version - ", "").replaceAll("\"-->", "");
	    return document.trim();
	} catch (Exception e) {
	    LoggerFactory.getLogger(AppConstants.class).warn("Unable to get release version ");
	}
	return "NA";
    }

}
