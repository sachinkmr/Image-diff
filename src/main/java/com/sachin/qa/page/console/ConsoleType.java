package com.sachin.qa.page.console;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.page.DifferenceType;

import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

public class ConsoleType extends DifferenceType {
	private List<String> list;

	public ConsoleType(String url) {
		super(url, ".jsLog");
		list = new ArrayList<>();
		this.resourcePath = AppConstants.FOLDER + File.separator + "jsLogs" + File.separator + fileName;
	}

	private static final long serialVersionUID = 1L;
	protected static final Logger logger = LoggerFactory.getLogger(ConsoleType.class);

	@Override
	public void apply() throws Exception {
		try {
			if (this.getWebDriver() instanceof ChromeDriver || this.getWebDriver() instanceof PhantomJSDriver) {
				LogEntries logEntries = this.getWebDriver().manage().logs().get(LogType.BROWSER);
				list.add("Logging console logs for: " + this.url);
				list.add("Browser Type: " + this.getWebDriverName());
				list.add("----------------------------------------------------------------------");
				for (LogEntry entry : logEntries) {
					if (entry.getLevel().equals(Level.SEVERE))
						list.add(entry.getLevel() + " : " + entry.getMessage());
				}
			} else if (this.getWebDriver() instanceof FirefoxDriver) {
				list.add("Logging console logs for: " + this.url);
				list.add("Browser Type: " + this.getWebDriverName());
				list.add("----------------------------------------------------------------------");
				for (JavaScriptError jsError : JavaScriptError.readErrors(this.getWebDriver())) {
					list.add(jsError.toString());
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
			list = null;
		} catch (Exception e) {
			logger.warn("unable to read console log for: " + url, e);
		}
	}

	@Override
	public void differ(DifferenceType type) throws Exception {
		// TODO Auto-generated method stub

	}

}
