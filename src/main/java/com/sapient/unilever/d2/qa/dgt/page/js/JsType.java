package com.sapient.unilever.d2.qa.dgt.page.js;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.page.Featurable;
import com.sapient.unilever.d2.qa.dgt.selenium.WebDriverManager;

public class JsType extends Featurable {
	private static final long serialVersionUID = 1L;
	protected static final Logger logger = LoggerFactory.getLogger(JsType.class);
	private Set<String> list;

	public JsType(String url, WebDriverManager webDriverManager) {
		super(url, ".jsLog", webDriverManager);
		list = new LinkedHashSet<>();
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
			} else if (this.getWebDriver() instanceof PhantomJSDriver) {
				LogEntries logEntries = ((PhantomJSDriver) this.getWebDriver()).manage().logs().get("browser");
				list.add("Logging console logs for: " + this.url);
				list.add("Browser Type: " + this.getWebDriverName());
				list.add("----------------------------------------------------------------------");
				for (LogEntry entry : logEntries) {
					list.add(entry.getLevel() + " : " + entry.getMessage());
				}
			} else {
				logger.warn("skipping console log for: " + url + " on " + webDriverManager.getName());
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

			FileUtils.writeLines(new File(this.resourcePath), "UTF-8", list);
		} catch (Exception e) {
			logger.warn("unable to read console log for: " + url, e);
		}
	}

	@Override
	public void close() throws Exception {
		try {
			FileUtils.writeLines(new File(this.resourcePath), "UTF-8", list);
		} catch (Exception e) {
			logger.warn("unable to read console log for: " + url, e);
		}
	}
}
