package com.sapient.unilever.d2.qa.dgt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.report.HTMLReporter;
import com.sapient.unilever.d2.qa.dgt.report.ImageReporter;

public class Demo {
	protected static final Logger logger = LoggerFactory.getLogger(Demo.class);

	public static void main(String[] args) {
		System.setProperty("BuildType", "POST");
		System.setProperty("BrandName", "Dove");
		System.setProperty("imageDiff", "yes");
		System.setProperty("jsDiff", "yes");
		System.setProperty("PreBuildVersion", "2.18.1");
		System.setProperty("PreBuildTime", "17-April-2017_04-51PM");
		new HTMLReporter().generateImageReport(new ImageReporter());
	}
}
