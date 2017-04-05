package com.sachin.qa.dgt.page.html;

import java.io.File;

import com.sachin.qa.dgt.AppConstants;
import com.sachin.qa.dgt.page.Featurable;
import com.sachin.qa.dgt.selenium.WebDriverManager;

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
