package com.sachin.qa.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.app.selenium.Browser;

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

	public static String generateTimeString() {
		DateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
		DateFormat df1 = new SimpleDateFormat("hh-mmaa");
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
			LoggerFactory.getLogger(HelperUtils.class).debug("Error", e);
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

	/**
	 * Method to read CSV File.
	 *
	 * @return List<CSVRecord> List of CSV Records
	 *
	 ***/

	public static List<CSVRecord> readCSV(String csvPath) {
		try {
			CSVParser parser = CSVParser.parse(new File(csvPath), StandardCharsets.UTF_8, CSVFormat.EXCEL.withHeader());
			try {
				return parser.getRecords();
			} finally {
				parser.close();
			}
		} catch (FileNotFoundException ex) {
			LoggerFactory.getLogger(HelperUtils.class).debug("DEBUG", ex);
		} catch (IOException ex) {
			LoggerFactory.getLogger(HelperUtils.class).debug("DEBUG", ex);
		}
		return null;
	}

	public static void validate() throws Exception {
		boolean x = StringUtils.isBlank(AppConstants.SITE);
		boolean y = !StringUtils.isBlank(AppConstants.BRAND_NAME) && !StringUtils.isBlank(AppConstants.URL_TEXT);
		if (!(x & y) && !(!x & !y)) {
			throw new Exception("Please enter site or brand name and URL text file");
		}
		// if (StringUtils.isBlank(AppConstants.BUILD_VERSION)) {
		// throw new Exception("Please enter build version");
		// }
		// if (!(AppConstants.DELETE_DATA && AppConstants.IS_DIFF)) {
		// throw new Exception("Diff-run and DeleteData both should be set to
		// true");
		// }
	}

	public static void loadWebDriverServers() {
		Properties pro = new Properties();
		try {
			InputStream is = new FileInputStream(new File(HelperUtils.getResourceFile("webdriver.properties")));
			pro.load(is);
			is.close();
			System.setProperties(pro);
		} catch (IOException e) {
			LoggerFactory.getLogger(HelperUtils.class).error("Unable to load webdriver servers", e);
		}
	}
}
