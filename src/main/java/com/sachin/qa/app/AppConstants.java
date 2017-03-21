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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.selenium.Browser;
import com.sachin.qa.app.utils.HelperUtils;

public class AppConstants {

	public static String BRAND_NAME, SITE;
	public static String USERNAME;
	public static String PASSWORD;
	public static Properties PROPERTIES;
	public static List<String> SKIPPED_URLS;
	public static String CRAWLER_DATA;
	public static String APP_DATA, URL_TEXT;
	public static Pattern PATTERN;
	public static Pattern SHOULD_VISIT_PATTERN;
	public static Pattern IMAGE_PATTERN;
	public static Pattern ASSETS_PATTERN;
	public static String USER_AGENT;
	public static long CRAWLING_TIME;
	public static String ERROR_TEXT, DIFF_FOLDER_PNG, DIFF_FOLDER_GIF, AFTER_FOLDER, BEFORE_FOLDER, BUILD_VERSION;
	public static boolean ERROR;
	// public static final String TIME_STAMP;
	public static boolean URL_IS_CASE_SENSITIVE, IS_DIFF;
	public static int IGNORED_PIXELS;
	public static int HEADER_PIXELS;
	public static int FOOTER_PIXELS;
	public static int BROWSER_INSTANCE = 1;
	public static Set<Browser> BROWSERS;
	public static Map<WebDriver, Boolean> DRIVERS;

	static {
		SITE = System.getProperty("SiteAddress");
		USERNAME = System.getProperty("Username");
		PASSWORD = System.getProperty("Password");
		BUILD_VERSION = System.getProperty("BuildVersion");
		BRAND_NAME = System.getProperty("BrandName");
		IS_DIFF = System.getProperty("Diff-run") != null && !System.getProperty("Diff-run").isEmpty()
				&& System.getProperty("Diff-run").equalsIgnoreCase("true");
		URL_TEXT = !StringUtils.isBlank(System.getProperty("UrlsTextFile")) ? System.getProperty("UrlsTextFile") : "";
		// TIME_STAMP = HelperUtils.generateUniqueString();
		boolean flag = false;
		try {
			HelperUtils.validate();
			flag = true;
		} catch (Exception e1) {
			LoggerFactory.getLogger(EntryPoint.class).error("Error in initialization", e1);
		}
		if (flag) {
			DRIVERS = new HashMap<>();
			File storage = null;
			if (!StringUtils.isBlank(SITE)) {
				String host = "";
				try {
					host = new URL(SITE).getHost().replaceAll("www.", "");
				} catch (MalformedURLException e) {
					LoggerFactory.getLogger(AppConstants.class).debug("Error in loading config file", e);
				}
				storage = new File(System.getProperty("user.dir") + File.separator + "data" + File.separator + host
						+ File.separator + BUILD_VERSION);
			} else {
				storage = new File(System.getProperty("user.dir") + File.separator + "data" + File.separator
						+ BRAND_NAME + File.separator + BUILD_VERSION);
			}

			File file = new File(storage, "crawler-data");
			file.mkdirs();
			CRAWLER_DATA = file.getAbsolutePath();
			file = new File(storage, "app-data");
			file.mkdirs();
			APP_DATA = file.getAbsolutePath();
			BEFORE_FOLDER = APP_DATA + File.separator + "before";
			AFTER_FOLDER = APP_DATA + File.separator + "after";
			DIFF_FOLDER_GIF = APP_DATA + File.separator + "diff" + File.separator + "gif";
			DIFF_FOLDER_PNG = APP_DATA + File.separator + "diff" + File.separator + "png";
			new File(BEFORE_FOLDER).mkdirs();
			if (IS_DIFF) {
				createFolder(AFTER_FOLDER);
				createFolder(DIFF_FOLDER_GIF);
				createFolder(DIFF_FOLDER_PNG);
			}

			file = null;
			storage = null;

			PROPERTIES = new Properties();
			SKIPPED_URLS = new ArrayList<>();
			String PROPERTIES_LOC = "";

			boolean deletePropFile = false, delBrowserFile = false;
			if (System.getProperty("CrawlerConfigFile") != null && !System.getProperty("CrawlerConfigFile").isEmpty()) {
				LoggerFactory.getLogger(AppConstants.class).info("Loading user's config file");
				PROPERTIES_LOC = System.getProperty("CrawlerConfigFile");
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
			// browsers info instantiation

			String browserLoc = "";
			if (System.getProperty("BrowserConfigFile") != null && !System.getProperty("BrowserConfigFile").isEmpty()) {
				LoggerFactory.getLogger(AppConstants.class).info("Loading user's browser file");
				browserLoc = System.getProperty("BrowserConfigFile");
				delBrowserFile = true;
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
			if (deletePropFile) {
				FileUtils.deleteQuietly(new File(PROPERTIES_LOC).getParentFile());
			}
			if (delBrowserFile) {
				FileUtils.deleteQuietly(new File(browserLoc).getParentFile());
			}
		}

	}

	private static void createFolder(String bEFORE_FOLDER2) {
		File file = new File(bEFORE_FOLDER2);
		if (file.exists())
			FileUtils.deleteQuietly(file);
		file.mkdirs();
	}

}
