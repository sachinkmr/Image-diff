package com.sachin.qa.image;

import org.openqa.selenium.WebDriver;

import com.sachin.qa.app.selenium.Browser;

public class ImageTaker {
	private ImageInfo info;
	private WebDriver driver;

	public ImageTaker(ImageInfo info, WebDriver driver, Browser browser) {
		this.info = info;
		this.driver = driver;
	}

	public void capture() {

	}
}
