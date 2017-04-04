package com.sachin.qa.app.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.app.utils.StreamUtils;
import com.sachin.qa.page.PageInfo;
import com.sachin.qa.page.diff.Differentiator;
import com.sachin.qa.page.image.ImageDiffInfo;
import com.sachin.qa.page.js.JsDiffInfo;
import com.sachin.qa.page.js.JsType;

public class Reporter {
	public static void generateReportAsCSV() {
		File[] files = new File(AppConstants.FOLDER).listFiles((File dir, String name) -> {
			return name.endsWith(".info");
		});
		List<String> rows = new ArrayList<>();
		rows.add(new StringBuilder("\"URL\",").append("\"Browser\",").append("\"Image Path\",")
				.append("\"JS Logs Path\",").append("\"JS Error\",").append("\"Build Type\",").toString());
		for (File file : files) {
			PageInfo pageInfo = StreamUtils.readPageInfo(file);
			StringBuilder builder = new StringBuilder("\"" + pageInfo.getPageUrl() + "\",");
			builder.append("\"" + pageInfo.getBrowserName() + "\",");
			builder.append("\"" + pageInfo.getTypes().get(0).getResourcePath() + "\",");
			builder.append("\"" + pageInfo.getTypes().get(1).getResourcePath() + "\",");
			builder.append("\"" + ((JsType) pageInfo.getTypes().get(1)).hasError() + "\",");
			builder.append("\"" + pageInfo.getBuildType() + "\",");
			rows.add(builder.toString());
		}
		File file = new File(AppConstants.FOLDER, "Report.csv");
		try {
			FileUtils.writeLines(file, "utf-8", rows);
		} catch (IOException e) {
			LoggerFactory.getLogger(Reporter.class).debug("Error writing report csv", e);
		}
		System.out.println("Report at: " + file.getAbsolutePath());
	}

	public static void generateDIffReportAsCSV() {
		File[] files = new File(AppConstants.DIFF_FOLDER).listFiles((File dir, String name) -> {
			return name.endsWith(".diff");
		});
		List<String> rows = new ArrayList<>();
		rows.add(new StringBuilder("\"URL\",").append("\"Browser\",").append("\"Pre Image Path\",")
				.append("\"Post Image Path\",").append("\"Image Matched\",").append("\"PNG Diff\",")
				.append("\"Giff Diff\",").append("\"Image Diff size\",").append("\"Pre JS Logs Path\",")
				.append("\"Post JS Logs Path\",").append("\"JS matched\",").append("\"JS Error Diff\",").toString());
		for (File file : files) {
			Differentiator diff = StreamUtils.readDiffInfo(file);
			ImageDiffInfo image = (ImageDiffInfo) diff.getDiffInfos().get(0);
			StringBuilder builder = new StringBuilder("\"" + image.getPreInfo().getPageUrl() + "\",");
			builder.append("\"" + image.getPreInfo().getBrowserName() + "\",");
			builder.append("\"" + image.getPreInfo().getTypes().get(0).getResourcePath() + "\",");
			builder.append("\"" + image.getPostInfo().getTypes().get(0).getResourcePath() + "\",");
			builder.append("\"" + image.isMatched() + "\",");
			builder.append("\"" + image.getProperties().get("png") + "\",");
			builder.append("\"" + image.getProperties().get("gif") + "\",");
			builder.append("\"" + image.getDiffSize() + "\",");
			image = null;
			JsDiffInfo js = (JsDiffInfo) diff.getDiffInfos().get(1);
			builder.append("\"" + js.getPreInfo().getTypes().get(1).getResourcePath() + "\",");
			builder.append("\"" + js.getPostInfo().getTypes().get(1).getResourcePath() + "\",");
			builder.append("\"" + js.isMatched() + "\",");

			rows.add(builder.toString());
		}
		File file = new File(AppConstants.DIFF_FOLDER, "Diff-Report.csv");
		try {
			FileUtils.writeLines(file, "utf-8", rows);
		} catch (IOException e) {
			LoggerFactory.getLogger(Reporter.class).debug("Error writing report csv", e);
		}
		System.out.println("Report at: " + file.getAbsolutePath());
	}

}
