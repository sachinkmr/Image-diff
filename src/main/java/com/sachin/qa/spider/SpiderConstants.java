package com.sachin.qa.spider;

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
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

import com.sachin.qa.spider.helpers.HelperUtils;

public class SpiderConstants {

	public static String SITE;
	public static final String USERNAME;
	public static final String PASSWORD;
	public static final Properties PROPERTIES;
	public static final List<String> SKIPPED_URLS;
	public static final String CRAWL_STORAGE_FOLDER;
	public static final String DATA_LOCATION;
	public static final Pattern PATTERN;
	public static final Pattern SHOULD_VISIT_PATTERN;
	public static final Pattern IMAGE_PATTERN;
	public static final Pattern ASSETS_PATTERN;
	public static final String USER_AGENT;
	public static long CRAWLING_TIME;
	public static String ERROR_TEXT;
	public static boolean ERROR;
	public static final String TIME_STAMP;
	public static final boolean URL_IS_CASE_SENSITIVE;
	static {
		SITE = System.getProperty("SiteAddress");
		USERNAME = System.getProperty("Username");
		PASSWORD = System.getProperty("Password");

		String host = "";
		try {
			host = new URL(SITE).getHost().replaceAll("www.", "");
		} catch (MalformedURLException e) {
			LoggerFactory.getLogger(SpiderConstants.class).debug("Error in loading config file", e);
		}
		TIME_STAMP = HelperUtils.generateUniqueString();
		File storage = new File(System.getProperty("user.dir") + File.separator + "temp" + File.separator + host
				+ File.separator + TIME_STAMP);
		storage.mkdirs();
		CRAWL_STORAGE_FOLDER = storage.getAbsolutePath();
		DATA_LOCATION = CRAWL_STORAGE_FOLDER + File.separator + "urls";
		new File(DATA_LOCATION).mkdirs();
		PROPERTIES = new Properties();
		SKIPPED_URLS = new ArrayList<>();
		String PROPERTIES_LOC = "";

		boolean deletePropFile = false;
		if (System.getProperty("CrawlerConfigFile") != null && !System.getProperty("CrawlerConfigFile").isEmpty()) {
			LoggerFactory.getLogger(SpiderConstants.class)
					.info("Loading user's config file provided from web interface");
			PROPERTIES_LOC = System.getProperty("CrawlerConfigFileLocation");
			deletePropFile = true;
		} else if (System.getProperty("CrawlerConfig") != null && !System.getProperty("CrawlerConfig").isEmpty()) {
			LoggerFactory.getLogger(SpiderConstants.class)
					.info("Loading user's config file provided from jenkins interface");
			PROPERTIES_LOC = HelperUtils.getResourceFile("Config.properties", "CrawlerConfig");
		} else {
			LoggerFactory.getLogger(SpiderConstants.class).info("Loading default config file");
			PROPERTIES_LOC = HelperUtils.getResourceFile("Config.properties");
		}

		// Loading crawler Config file
		LoggerFactory.getLogger(SpiderConstants.class).info("Property file loaded from: " + PROPERTIES_LOC);
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
			LoggerFactory.getLogger(SpiderConstants.class).error("Error in loading config file", e);
		}

		ASSETS_PATTERN = Pattern.compile("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp|js|css)))", Pattern.CASE_INSENSITIVE);
		IMAGE_PATTERN = Pattern.compile("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp)))", Pattern.CASE_INSENSITIVE);
		PATTERN = Pattern.compile(PROPERTIES.getProperty("crawler.domainRegex", ".").trim(), Pattern.CASE_INSENSITIVE);
		SHOULD_VISIT_PATTERN = Pattern.compile(PROPERTIES.getProperty("crawler.linksToVisit", ".").trim(),
				Pattern.CASE_INSENSITIVE);
		USER_AGENT = PROPERTIES.getProperty("crawler.userAgentString",
				"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");
		URL_IS_CASE_SENSITIVE = Boolean.parseBoolean(PROPERTIES.getProperty("crawler.caseSensitiveUrl", "false"));
		if (deletePropFile) {
			FileUtils.deleteQuietly(new File(PROPERTIES_LOC).getParentFile());
		}
	}

}
