package com.sachin.qa.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String pageUrl;
	private String browserName;
	private List<DifferenceType> types;

	public List<DifferenceType> getTypes() {
		return types;
	}

	public PageInfo(String pageUrl) {
		this.pageUrl = pageUrl;
		types = new ArrayList<>();
	}

	public void register(DifferenceType type) {
		types.add(type);
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}
}
