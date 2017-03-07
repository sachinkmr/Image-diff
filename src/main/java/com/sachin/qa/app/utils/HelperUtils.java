package com.sachin.qa.app.utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.app.selenium.Browser;
import com.sachin.qa.app.site.UrlHandler;

import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import edu.uci.ics.crawler4j.url.WebURL;

public class HelperUtils {

	protected static final Logger logger = LoggerFactory.getLogger(HelperUtils.class);

	/**
	 * Method returns the unique string based on time stamp
	 *
	 *
	 * @return unique string
	 */
	public static String generateUniqueString() {
		DateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
		DateFormat df1 = new SimpleDateFormat("hh-mm-ss-SSaa");
		Calendar calobj = Calendar.getInstance();
		String time = df1.format(calobj.getTime());
		String date = df.format(calobj.getTime());
		return date + "_" + time;
	}

	public static String getResourceFile(String fileName) {
		File file = null;
		try {
			String str = IOUtils.toString(HelperUtils.class.getClassLoader().getResourceAsStream(fileName));
			file = new File(new File(AppConstants.CRAWLER_DATA).getParentFile(), fileName);
			FileUtils.write(file, str, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	public static String getSiteAddress(String address) {
		String add = URLCanonicalizer.getCanonicalURL(address);
		WebURL url = new WebURL();
		url.setURL(add);
		String domain = url.getDomain();
		String site = add.substring(0, add.indexOf(domain) + domain.length() + 1);
		return site;
	}

	public static String getResourceFile(String fileName, String pROPERTIES_LOC) {
		File file = null;
		try {
			String str = FileUtils.readFileToString(new File(pROPERTIES_LOC), "UTF-8");
			file = new File(AppConstants.CRAWLER_DATA, fileName);
			FileUtils.write(file, str, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	public static Date getTestCaseTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	public static String getUUID() {
		String str = new String("");
		for (char a : UUID.randomUUID().toString().replaceAll("-", "").toCharArray()) {
			if (Character.isDigit(a)) {
				str += Character.toString(Character.toChars(97 + Character.getNumericValue(a))[0]);
			} else {
				str += Character.toString(a);
			}
		}
		return str;
	}

	public static Set<Browser> readBrowsers(JSONObject BROWSER_PROPERTIES) {
		Set<Browser> list = new LinkedHashSet<>();
		JSONArray arr = BROWSER_PROPERTIES.getJSONObject("broswers").names();
		for (int i = 0; i < arr.length(); i++) {
			String name = arr.getString(i);
			Browser browser = new Browser();
			browser.setName(name);
			browser.setCustom(false);
			browser.setHeight(0);
			browser.setParentBrowser(name);
			browser.setUserAgent("");
			browser.setWidth(0);
			if (BROWSER_PROPERTIES.getJSONObject("broswers").getJSONObject(name).getBoolean("enable")) {
				list.add(browser);
			}
		}
		arr = BROWSER_PROPERTIES.getJSONArray("custom");
		for (int i = 0; i < arr.length(); i++) {
			JSONObject brow = arr.getJSONObject(i);
			Browser browser = new Browser();
			browser.setName(brow.getString("name"));
			browser.setCustom(true);
			browser.setHeight(brow.getInt("height"));
			browser.setParentBrowser(brow.getString("emulatedBy"));
			browser.setUserAgent(brow.getString("userAgent"));
			browser.setWidth(brow.getInt("width"));
			if (brow.getBoolean("enable")) {
				list.add(browser);
			}
		}
		return list;
	}

	public static WebDriver getIdealDriver() {
		synchronized (UrlHandler.class) {
			for (WebDriver driver : AppConstants.DRIVERS.keySet()) {
				if (!AppConstants.DRIVERS.get(driver)) {
					return driver;
				}
			}
		}
		return null;
	}

}
