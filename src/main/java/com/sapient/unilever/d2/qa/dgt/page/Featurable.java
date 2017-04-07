package com.sapient.unilever.d2.qa.dgt.page;

import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.WebDriver;

import com.sapient.unilever.d2.qa.dgt.selenium.WebDriverManager;

public abstract class Featurable implements AutoCloseable, Serializable {

	private static final long serialVersionUID = 1L;
	protected String type;
	protected String url;
	protected String resourcePath;
	protected transient String fileName;
	protected transient WebDriverManager webDriverManager;

	protected Featurable(String url, String type, WebDriverManager webDriverManager) {
		this.url = url;
		this.type = type;
		this.webDriverManager = webDriverManager;
		this.fileName = Base64.encodeBase64URLSafeString((url + webDriverManager.getName()).getBytes()) + type;
	}

	public String getFileName() {
		return fileName;
	}

	public WebDriverManager getWebDriverManager() {
		return webDriverManager;
	}

	public String getUrl() {
		return url;
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

	public abstract void apply() throws Exception;
}
