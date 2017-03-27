package com.sachin.qa.app.selenium;

public class Browser {
	private String name;
	private int height;
	private int width;
	private String userAgent;
	private boolean custom;
	private String parentBrowser;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public boolean isCustom() {
		return custom;
	}

	public void setCustom(boolean custom) {
		this.custom = custom;
	}

	public String getParentBrowser() {
		return parentBrowser;
	}

	public void setParentBrowser(String parentBrowser) {
		this.parentBrowser = parentBrowser;
	}

	@Override
	public String toString() {
		return name;
	}

}
