package com.sapient.unilever.d2.qa.dgt.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.manager.ThreadManager;

public class PageInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String pageUrl;
	private String browserName;
	private String pageName;
	private String buildType;
	private List<Featurable> types;

	public PageInfo(String pageUrl, String browserName) {
		this.pageUrl = pageUrl;
		this.browserName = browserName;
		this.pageName = ThreadManager.pushURL(pageUrl) + browserName.hashCode() + ".info";
		types = new ArrayList<>();
		this.buildType = AppConstants.BUILD_TYPE.name();
	}

	public List<Featurable> getTypes() {
		return types;
	}

	public Featurable getType(String type) {
		for (Featurable f : types) {
			if (type.equals(f.getType()))
				return f;
		}
		return null;
	}

	public String getBuildType() {
		return buildType;
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

	@Override
	public String toString() {
		return String.format("Url: %s, on %s, for %s build.", pageUrl, browserName, buildType);
	}

}
