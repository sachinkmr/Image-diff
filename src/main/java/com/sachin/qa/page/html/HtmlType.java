package com.sachin.qa.page.html;

import java.io.File;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.app.selenium.WebDriverManager;
import com.sachin.qa.page.Featurable;

public class HtmlType extends Featurable {
	private static final long serialVersionUID = 1L;

	public HtmlType(String url, WebDriverManager webDriverManager) {
		super(url, ".html", webDriverManager);
		this.resourcePath = AppConstants.FOLDER + File.separator + "html" + File.separator + fileName;
		new File(this.resourcePath).getParentFile().mkdirs();
	}

	@Override
	public void apply() throws Exception {

	}

	@Override
	public void close() throws Exception {

	}

}
