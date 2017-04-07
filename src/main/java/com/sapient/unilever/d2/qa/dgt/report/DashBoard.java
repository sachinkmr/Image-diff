package com.sapient.unilever.d2.qa.dgt.report;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.model.TestAttribute;
import com.relevantcodes.extentreports.utils.DateTimeUtil;

public class DashBoard {
	private static DashBoard dashBoard;
	public int totalTests;
	public int totalSteps;
	public int passedTests;
	public int failedTests;
	public int fatalTests;
	public int warningTests;
	public int errorTests;
	public int skippedTests;
	public int unknownTests;
	public int passedSteps;
	public int failedSteps;
	public int fatalSteps;
	public int warningSteps;
	public int errorSteps;
	public int infoSteps;
	public int skippedSteps;
	public int unknownSteps;
	private List<DashBoardCategory> dashBoardCategories;
	private List<LogStatus> logStatusList;
	private Date startedTime;
	// private Map<String, Set<String>> pageTypes;

	public synchronized static DashBoard getInstance() {
		if (null == dashBoard) {
			dashBoard = new DashBoard();
		}
		return dashBoard;
	}

	public String getIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {

		}
		return "10.207.16.9";
	}

	public synchronized void addTest(ExtentTest test) {
		TestCase tc = new TestCase(test.getTest().getName());
		tc.setId(test.getTest().getId().toString());
		tc.setDesc(test.getDescription());
		Date startTime = null;
		LogStatus status = test.getRunStatus();
		if (status == LogStatus.FATAL || status == LogStatus.ERROR || status == LogStatus.WARNING
				|| status == LogStatus.UNKNOWN) {
			if (!logStatusList.contains(status)) {
				logStatusList.add(status);
			}
		}
		TestAttribute attr = test.getTest().getCategoryList().get(0);
		DashBoardCategory cat = new DashBoardCategory(attr.getName());
		if (dashBoardCategories.contains(cat)) {
			cat = dashBoardCategories.remove(dashBoardCategories.indexOf(cat));
		}
		tc.setCats(attr.getName());
		if (cat.getTestCases().contains(tc)) {
			tc = cat.getTestCases().remove(cat.getTestCases().indexOf(new TestCase(test.getTest().getName())));
			startTime = tc.getTime();
		} else {

		}
		if (null == startTime) {
			startTime = test.getStartedTime();
		}
		tc.setTime(startTime);
		cat.getTestCases().add(tc);
		dashBoardCategories.add(cat);
		if (test.getRunStatus().name().equalsIgnoreCase("pass")) {
			cat.setPassed(cat.getPassed() + 1);
		} else if (test.getRunStatus().name().equalsIgnoreCase("fail")
				|| test.getRunStatus().name().equalsIgnoreCase("fatal")
				|| test.getRunStatus().name().equalsIgnoreCase("error")) {
			cat.setFailed(cat.getFailed() + 1);
		}
		cat.setTotal(cat.getTotal() + 1);
		tc.setStatus(status);
		tc.getLogStatus().add(status);
		tc.setEndedTime(test.getEndedTime());
		tc.setRunDuration(DateTimeUtil.getDiff(test.getEndedTime(), tc.getTime()));
	}

	private DashBoard() {
		dashBoardCategories = new CopyOnWriteArrayList<>();
		logStatusList = new CopyOnWriteArrayList<>();
		ExtentReports report = JSReportFactory.getInstance().getExtentReport();
		startedTime = report.getStartedTime();
	}

	public Date getStartedTime() {
		return this.startedTime;
	}

	public String getRunDuration() {
		return DateTimeUtil.getDiff(Calendar.getInstance().getTime(), this.startedTime);
	}

	public String getRunDurationOverall() {
		return DateTimeUtil.getDiff(Calendar.getInstance().getTime(), this.startedTime);
	}

	public List<LogStatus> getLogStatusList() {
		return this.logStatusList;
	}

	public Map<String, String> getSystemInfo() {
		Map<String, String> map = new LinkedHashMap<>();

		try {
			if (System.getProperty("machine") != null && !System.getProperty("machine").isEmpty()) {
				map.put("Requester IP", System.getProperty("machine"));
			} else {
				map.put("Machine", InetAddress.getLocalHost().getHostName());
			}
		} catch (UnknownHostException e) {
		}
		return map;
	}

	public List<DashBoardCategory> getDashBoardCategories() {
		return dashBoardCategories;
	}

	public int getTotalTests() {
		return totalTests;
	}

	public int getTotalSteps() {
		return totalSteps;
	}

	public List<TestCase> getTestCases() {
		List<TestCase> list = new ArrayList<>();
		for (DashBoardCategory cat : dashBoardCategories) {
			for (TestCase tc : cat.getTestCases()) {
				if (list.contains(tc)) {
					LogStatus status = tc.getStatus();
					tc = list.remove(list.indexOf(tc));
					tc.setCats(cat.getName());
					tc.getLogStatus().add(status);
				}
				list.add(tc);
			}
		}
		return list;
	}

	public int getFatalTests() {
		return fatalTests;
	}

	public int getWarningTests() {
		return warningTests;
	}

	public int getErrorTests() {
		return errorTests;
	}

	public int getSkippedTests() {
		return skippedTests;
	}

	public int getUnknownTests() {
		return unknownTests;
	}

	public int getFatalSteps() {
		return fatalSteps;
	}

	public int getWarningSteps() {
		return warningSteps;
	}

	public int getErrorSteps() {
		return errorSteps;
	}

	public int getInfoSteps() {
		return infoSteps;
	}

	public int getSkippedSteps() {
		return skippedSteps;
	}

	public int getUnknownSteps() {
		return unknownSteps;
	}

	public int getPassedTests() {
		return passedTests;
	}

	public int getFailedTests() {
		return failedTests;
	}

	public int getOtherTests() {
		return skippedTests + warningTests + unknownTests;
	}

	public int getPassedSteps() {
		return passedSteps;
	}

	public int getFailedSteps() {
		return failedSteps;
	}

	public int getOtherSteps() {
		return skippedSteps + warningSteps + unknownSteps + infoSteps;
	}

	public void updateTestStatus() {
		List<TestCase> list = getTestCases();
		for (TestCase test : list) {
			updateStatus(test);
			totalTests++;
			if (test.getStatus().name().equalsIgnoreCase("fail")) {
				failedTests++;
			} else if (test.getStatus().name().equalsIgnoreCase("pass")) {
				passedTests++;
			} else if (test.getStatus().name().equalsIgnoreCase("fatal")) {
				fatalTests++;
			} else if (test.getStatus().name().equalsIgnoreCase("error")) {
				errorTests++;
			} else if (test.getStatus().name().equalsIgnoreCase("skip")) {
				skippedTests++;
			} else if (test.getStatus().name().equalsIgnoreCase("warning")) {
				warningTests++;
			} else if (test.getStatus().name().equalsIgnoreCase("unknown")) {
				unknownTests++;
			}
		}
	}

	private void updateStatus(TestCase test) {
		if (test.getLogStatus().contains(LogStatus.FATAL)) {
			test.setStatus(LogStatus.FATAL);
			return;
		}
		if (test.getLogStatus().contains(LogStatus.ERROR)) {
			test.setStatus(LogStatus.ERROR);
			return;
		}
		if (test.getLogStatus().contains(LogStatus.WARNING)) {
			test.setStatus(LogStatus.WARNING);
			return;
		}
		if (test.getLogStatus().contains(LogStatus.FAIL)) {
			test.setStatus(LogStatus.FAIL);
			return;
		}
		if (test.getLogStatus().contains(LogStatus.SKIP)) {
			test.setStatus(LogStatus.SKIP);
			return;
		}
		if (test.getLogStatus().contains(LogStatus.UNKNOWN)) {
			test.setStatus(LogStatus.UNKNOWN);
			return;
		}
		test.setStatus(LogStatus.PASS);
	}
}
