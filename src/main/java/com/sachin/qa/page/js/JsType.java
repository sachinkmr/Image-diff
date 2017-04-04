package com.sachin.qa.page.js;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.app.selenium.WebDriverManager;
import com.sachin.qa.page.Featurable;

public class JsType extends Featurable {
	private static final long serialVersionUID = 1L;
	protected static final Logger logger = LoggerFactory.getLogger(JsType.class);

	private List<String> list;

	public JsType(String url, WebDriverManager webDriverManager) {
		super(url, ".jsLog", webDriverManager);
		list = new ArrayList<>();
		this.resourcePath = AppConstants.FOLDER + File.separator + "jsLogs" + File.separator + fileName;
		new File(this.resourcePath).getParentFile().mkdirs();
	}

	public boolean hasError() {
		return list.size() > 3;
	}

	@Override
	public void apply() throws Exception {
		try {
			if (this.getWebDriver() instanceof ChromeDriver) {
				LogEntries logEntries = this.getWebDriver().manage().logs().get(LogType.BROWSER);
				list.add("Logging console logs for: " + this.url);
				list.add("Browser Type: " + this.getWebDriverName());
				list.add("----------------------------------------------------------------------");
				for (LogEntry entry : logEntries) {
					list.add(entry.getLevel() + " : " + entry.getMessage());
				}
			}
			// if (this.getWebDriver() instanceof FirefoxDriver) {
			// LogEntries logEntries =
			// this.getWebDriver().manage().logs().get(LogType.BROWSER);
			// list.add("Logging console logs for: " + this.url);
			// list.add("Browser Type: " + this.getWebDriverName());
			// list.add("----------------------------------------------------------------------");
			// for (LogEntry entry : logEntries) {
			// list.add(entry.getLevel() + " : " + entry.getMessage());
			// }
			// }
			if (this.getWebDriver() instanceof PhantomJSDriver) {
				LogEntries logEntries = ((PhantomJSDriver) this.getWebDriver()).manage().logs().get("browser");
				list.add("Logging console logs for: " + this.url);
				list.add("Browser Type: " + this.getWebDriverName());
				list.add("----------------------------------------------------------------------");
				for (LogEntry entry : logEntries) {
					list.add(entry.getLevel() + " : " + entry.getMessage());
				}
			}
			FileUtils.writeLines(new File(this.resourcePath), "UTF-8", list);
		} catch (Exception e) {
			logger.warn("unable to read console log for: " + url, e);
		}
	}

	@Override
	public void close() throws Exception {
		try {
			FileUtils.writeLines(new File(this.resourcePath), "UTF-8", list);
			if (list.size() > 3) {
				list.remove(0);
				list.remove(1);
				list.remove(2);
			} else {
				list.clear();
			}
		} catch (Exception e) {
			logger.warn("unable to read console log for: " + url, e);
		}
	}

}
