package com.sachin.qa.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public class PageInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String pageUrl;
	private String browserName;
	private String pageName;
	private List<Featurable> types;

	public List<Featurable> getTypes() {
		return types;
	}

	public PageInfo(String pageUrl, String browserName) {
		this.pageUrl = pageUrl;
		this.browserName = browserName;
		this.pageName = Base64.encodeBase64URLSafeString((pageUrl + browserName).getBytes()) + ".info";
		types = new ArrayList<>();
	}

	public String getPageName() {
		return pageName;
	}

	public void register(Featurable type) {
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

}
