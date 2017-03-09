package com.sachin.qa.app.site;

import java.io.File;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.app.selenium.WebDriverManager;
import com.sachin.qa.app.utils.StreamUtils;
import com.sachin.qa.image.ImageInfo;
import com.sachin.qa.image.ImageTaker;
import com.sachin.qa.image.diff.DiffFinder;

public class UrlHandler implements Runnable {
	private WebDriverManager webDriverManager;
	private ImageInfo imageInfo;
	protected static final Logger logger = LoggerFactory.getLogger(UrlHandler.class);

	public UrlHandler(WebDriverManager webDriverManager, String url) {
		this.webDriverManager = webDriverManager;
		if (AppConstants.IS_DIFF) {
			imageInfo = StreamUtils.readImageInfo(url, webDriverManager.getName());
		} else {
			String name = Base64.encodeBase64URLSafeString((url + webDriverManager.getName()).getBytes());
			imageInfo = new ImageInfo(url);
			imageInfo.setDiffGiff(AppConstants.DIFF_FOLDER_GIF + File.separator + name + ".gif");
			imageInfo.setDiffImage(AppConstants.DIFF_FOLDER_PNG + File.separator + name + ".png");
			imageInfo.setImagePathNew(AppConstants.AFTER_FOLDER + File.separator + name + ".png");
			imageInfo.setImagePathOld(AppConstants.BEFORE_FOLDER + File.separator + name + ".png");
			imageInfo.setProcessed(false);
			imageInfo.setBrowserName(webDriverManager.getName());
		}
	}

	@Override
	public void run() {
		try {
			ImageTaker imageTaker = new ImageTaker(imageInfo, webDriverManager.getWebDriver());
			imageTaker.capture();
			if (AppConstants.IS_DIFF) {
				new Thread(new DiffFinder(imageInfo)).start();
			}
			StreamUtils.storeImageInfo(imageInfo);
		} catch (Exception ex) {
			logger.error("Unable to cature URL: " + imageInfo.getPageUrl());
		}

	}

}
