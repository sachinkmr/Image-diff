package com.sachin.qa.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.utils.HelperUtils;

public class AppConstants {

    public static String SITE;
    public static final String USERNAME;
    public static final String PASSWORD;
    public static final Properties PROPERTIES;
    public static final List<String> SKIPPED_URLS;
    public static final String CRAWLER_DATA;
    public static final String APP_DATA;
    public static final Pattern PATTERN;
    public static final Pattern SHOULD_VISIT_PATTERN;
    public static final Pattern IMAGE_PATTERN;
    public static final Pattern ASSETS_PATTERN;
    public static final String USER_AGENT;
    public static long CRAWLING_TIME;
    public static String ERROR_TEXT;
    public static boolean ERROR;
    public static final String TIME_STAMP;
    public static final boolean URL_IS_CASE_SENSITIVE,IS_DIFF;
    public static final String BROWSER_TYPE;
    public static final int BROWSER_INSTANCE;
    public static final Set<D2Page> d2Pages;

    static {
	SITE = System.getProperty("SiteAddress");
	USERNAME = System.getProperty("Username");
	PASSWORD = System.getProperty("Password");
	d2Pages = new CopyOnWriteArraySet<>();
	String host = "";
	try {
	    host = new URL(SITE).getHost().replaceAll("www.", "");
	} catch (MalformedURLException e) {
	    LoggerFactory.getLogger(AppConstants.class).debug("Error in loading config file", e);
	}
	TIME_STAMP = HelperUtils.generateUniqueString();
	File storage = new File(System.getProperty("user.dir") + File.separator + "data" + File.separator + host
		+ File.separator + TIME_STAMP);
	File file = new File(storage, "crawler-data");
	file.mkdirs();
	CRAWLER_DATA = file.getAbsolutePath();
	file = new File(storage, "app-data");
	file.mkdirs();
	APP_DATA = file.getAbsolutePath();
	file = null;
	storage = null;

	PROPERTIES = new Properties();
	SKIPPED_URLS = new ArrayList<>();
	String PROPERTIES_LOC = "";

	boolean deletePropFile = false;
	if (System.getProperty("CrawlerConfigFile") != null && !System.getProperty("CrawlerConfigFile").isEmpty()) {
	    LoggerFactory.getLogger(AppConstants.class).info("Loading user's config file");
	    PROPERTIES_LOC = System.getProperty("CrawlerConfigFileLocation");
	    deletePropFile = true;
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

	ASSETS_PATTERN = Pattern.compile("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp|js|css)))", Pattern.CASE_INSENSITIVE);
	IMAGE_PATTERN = Pattern.compile("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp)))", Pattern.CASE_INSENSITIVE);
	PATTERN = Pattern.compile(PROPERTIES.getProperty("crawler.domainRegex", ".").trim(), Pattern.CASE_INSENSITIVE);
	SHOULD_VISIT_PATTERN = Pattern.compile(PROPERTIES.getProperty("crawler.linksToVisit", ".").trim(),
		Pattern.CASE_INSENSITIVE);
	USER_AGENT = PROPERTIES.getProperty("crawler.userAgentString",
		"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");
	URL_IS_CASE_SENSITIVE = Boolean.parseBoolean(PROPERTIES.getProperty("crawler.caseSensitiveUrl", "false"));
	IS_DIFF = Boolean.parseBoolean(PROPERTIES.getProperty("app.isdiff", "false"));
	
	// browsers info instantiation
	BROWSER_INSTANCE = Integer.parseInt(PROPERTIES.getProperty("instance", "1"));
	
	BROWSER_TYPE = PROPERTIES.getProperty("browser.type", "chrome").toUpperCase();
	switch (BROWSER_TYPE) {
	case "CHROME":
	    System.setProperty("webdriver.chrome.driver", "servers/chromedriver.exe");
	    break;
	case "IE":
	    System.setProperty("webdriver.ie.driver", "servers/IEDriverServer.exe");
	    break;
	case "FIREFOX":
	    System.setProperty("webdriver.gecko.driver", "servers/geckodriver.exe");
	    break;
	case "EDGE":
	    System.setProperty("webdriver.edge.driver", "servers/MicrosoftWebDriver.exe");
	    break;
	case "PHANTOM":
	    System.setProperty("phantomjs.binary.path", "servers/phantomjs.exe");
	    break;
	}
	if (deletePropFile) {
	    FileUtils.deleteQuietly(new File(PROPERTIES_LOC).getParentFile());
	}
    }

}
