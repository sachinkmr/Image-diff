package com.sapient.unilever.d2.qa.dgt.report.js;

import java.util.ArrayList;
import java.util.List;

public class JsPage {
	private final String url;
	private final String browser;
	private List<JsError> jsError;
	private int errorCount;
	private int warningCount;

	public JsPage(String url, String browser) {
		this.url = url;
		this.browser = browser;
		jsError = new ArrayList<>();
	}

	public void addJsError(JsError error) {
		jsError.add(error);
	}

	public JsErrorType addJsError(String errorString, String pageUrl, String browserName) {
		JsError error = new JsError();
		String type = errorString.split(":")[0].trim();
		error.setType(type);
		error.setMessage(errorString.replace(type + " : ", "").trim());
		error.setPageUrl(pageUrl);
		error.setBrowserName(browserName);
		error.setCategory(errorString);
		if (type.equals(JsErrorType.ERROR.toString()))
			errorCount++;
		if (type.equals(JsErrorType.WARNING.toString()))
			warningCount++;
		jsError.add(error);
		return error.getType();
	}

	public String getUrl() {
		return url;
	}

	public String getBrowser() {
		return browser;
	}

	public List<JsError> getJsError() {
		return jsError;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public int getWarningCount() {
		return warningCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((browser == null) ? 0 : browser.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JsPage other = (JsPage) obj;
		if (browser == null) {
			if (other.browser != null)
				return false;
		} else if (!browser.equals(other.browser))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

}
