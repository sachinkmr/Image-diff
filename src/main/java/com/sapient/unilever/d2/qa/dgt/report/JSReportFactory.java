package com.sapient.unilever.d2.qa.dgt.report;

import java.text.SimpleDateFormat;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.NetworkMode;
import com.relevantcodes.extentreports.model.Log;
import com.relevantcodes.extentreports.view.Icon;
import com.sapient.unilever.d2.qa.dgt.utils.HelperUtils;

public class JSReportFactory {
	private static JSReportFactory factory;
	private ExtentReports reporter;
	private SimpleDateFormat df;
	private final MongoClient mongo;
	private final MongoDatabase mongoDB;

	// private Set<String> tests;

	private JSReportFactory() {
		mongo = new MongoClient("localhost", 27017);
		reporter = new ExtentReports("", true, DisplayOrder.OLDEST_FIRST, NetworkMode.ONLINE);
		mongoDB = mongo.getDatabase("JSON_validator");
		df = new SimpleDateFormat("h:mm:ss a");

		// tests = new HashSet<>();
	}

	public synchronized static JSReportFactory getInstance() {
		if (factory != null)
			return factory;
		factory = new JSReportFactory();
		return factory;
	}

	public ExtentReports getExtentReport() {
		return reporter;
	}

	public ExtentTest getTest(String testName) {
		return getTest(testName, "");
	}

	public ExtentTest getTest(String testName, String testDescription) {
		return getExtentReport().startTest(testName, testDescription);

	}

	public synchronized void closeTest(ExtentTest test) {
		if (test != null) {
			test.setEndedTime(HelperUtils.getTestCaseTime(System.currentTimeMillis()));
			reporter.endTest(test);
			Icon ic = new Icon();
			for (Log log : test.getTest().getLogList()) {

			}
			test.getTest().getLogList().clear();
			test = null;
		}
	}

	public void closeTest(String testName) {
		if (!testName.isEmpty()) {
			ExtentTest test = getTest(testName);
			getExtentReport().endTest(test);
		}
	}

	public void closeReport() {
		mongo.close();
		if (reporter != null) {
			reporter.close();
			reporter = null;
		}
		factory = null;
	}

	public MongoDatabase getMongoDB() {
		return mongoDB;
	}

}