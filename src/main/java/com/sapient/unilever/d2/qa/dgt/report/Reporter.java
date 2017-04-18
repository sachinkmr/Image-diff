package com.sapient.unilever.d2.qa.dgt.report;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.utils.DateTimeUtil;

public abstract class Reporter {
	protected int passedPage;
	protected int failedPage;
	protected Date startedTime;
	protected boolean diff;

	public Reporter() {
		AppConstants.END_TIME = System.currentTimeMillis();
		startedTime = new Date(AppConstants.START_TIME);
		diff = AppConstants.HAS_DIFF;
	}

	public boolean getDiff() {
		return diff;
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

	protected abstract void readData();

	public String getReportName() {
		return AppConstants.TIME_STAMP;
	}

	protected abstract void getDiffInfo();

	public int getPassedPage() {
		return passedPage;
	}

	public int getFailedPage() {
		return failedPage;
	}

	public int getTotalPage() {
		return passedPage + failedPage;
	}

	public Map<String, String> getSystemInfo() {
		Map<String, String> map = new LinkedHashMap<>();
		if (!AppConstants.URL_TEXT.isEmpty()) {
			try {
				map.put("Web Site", FileUtils.readLines(new File(AppConstants.URL_TEXT), "utf-8").get(0));
			} catch (IOException e) {
			}
		} else {
			map.put("Web Site", AppConstants.SITE);
		}
		map.put("Build Type", AppConstants.BUILD_TYPE.name());
		map.put("Branch Version", AppConstants.BUILD_VERSION);
		map.put("Brand Name", AppConstants.BRAND_NAME);
		try {
			if (System.getProperty("machine") != null && !System.getProperty("machine").isEmpty()) {
				map.put("Requester IP", System.getProperty("machine"));
			} else {
				map.put("Machine", InetAddress.getLocalHost().getHostName());
			}
		} catch (UnknownHostException e) {
		}
		if (AppConstants.HAS_DIFF) {
			map.put("Pre Branch Version", System.getProperty("PreBuildVersion"));
			map.put("Pre Build Time", System.getProperty("PreBuildTime"));
		}
		return map;

	}

}
