package com.sachin.qa.page;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.WebDriver;

import com.sachin.qa.app.selenium.WebDriverManager;

public abstract class DifferenceType implements Differentiable, AutoCloseable {
	private static final long serialVersionUID = 1L;
	protected String type;
	protected String url;
	protected String resourcePath;
	protected transient String fileName;
	protected transient WebDriverManager webDriverManager;

	protected DifferenceType(String url, String type) {
		this.url = url;
		this.type = type;
		this.fileName = Base64.encodeBase64URLSafeString((url + webDriverManager.getName()).getBytes()) + type;
	}

	public void setWebDriverManager(WebDriverManager webDriverManager) {
		this.webDriverManager = webDriverManager;
	}

	public String getFileName() {
		return fileName;
	}

	public WebDriverManager getWebDriverManager() {
		return webDriverManager;
	}

	public WebDriver getWebDriver() {
		return webDriverManager.getWebDriver();
	}

	public String getWebDriverName() {
		return webDriverManager.getName();
	}

	public String getType() {
		return type;
	}

	public String getResourcePath() {
		return resourcePath;
	}

}
