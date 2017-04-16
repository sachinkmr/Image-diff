package com.sapient.unilever.d2.qa.dgt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.report.HTMLReporter;
import com.sapient.unilever.d2.qa.dgt.report.JsReporter;

public class Demo {
	protected static final Logger logger = LoggerFactory.getLogger(Demo.class);

	public static void main(String[] args) {
		System.setProperty("BuildType", "pre");
		System.setProperty("BrandName", "Dove");
		// System.setProperty("Username", "unileverwebpr");
		// System.setProperty("Password", "d2prA890");
		System.setProperty("UrlsTextFile", "D:\\DoveUrls.txt");
		new HTMLReporter().generateJSReport(new JsReporter());
	}
}
