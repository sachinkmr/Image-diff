package com.sachin.qa.app.site;

import org.openqa.selenium.WebDriver;

import com.sachin.qa.app.utils.HelperUtils;
import com.sachin.qa.image.ImageInfo;

public class UrlHandler implements Runnable {
	private WebPage page;
	private ImageInfo imageInfo;

	public UrlHandler(WebPage page, ImageInfo imageInfo) {
		this.page = page;
		this.imageInfo = imageInfo;
	}

	@Override
	public void run() {
		WebDriver driver = HelperUtils.getIdealDriver();
	}

}
