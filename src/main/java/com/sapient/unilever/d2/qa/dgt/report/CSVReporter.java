package com.sapient.unilever.d2.qa.dgt.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.page.PageInfo;
import com.sapient.unilever.d2.qa.dgt.page.diff.Differentiator;
import com.sapient.unilever.d2.qa.dgt.page.image.ImageDiffInfo;
import com.sapient.unilever.d2.qa.dgt.page.js.JsDiffInfo;
import com.sapient.unilever.d2.qa.dgt.page.js.JsType;
import com.sapient.unilever.d2.qa.dgt.utils.StreamUtils;

public class CSVReporter {
	public static void generateReportAsCSV() {
		File[] files = new File(AppConstants.FOLDER).listFiles((File dir, String name) -> {
			return name.endsWith(".info");
		});
		String path = "";
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
			String str = builder.toString();
			if (AppConstants.WEB) {
				path = new File(AppConstants.FOLDER).getAbsolutePath().substring(0, AppConstants.FOLDER.indexOf("DGT"));
				str = str.replaceAll(Pattern.quote(path), "http://10.207.16.9/").replaceAll(Pattern.quote("\\"), "/");
			}
			rows.add(str);
		}
		File file = new File(AppConstants.FOLDER, "Report.csv");
		try {
			FileUtils.writeLines(file, "utf-8", rows);
		} catch (IOException e) {
			LoggerFactory.getLogger(CSVReporter.class).debug("Error writing report csv", e);
		}
		if (AppConstants.WEB) {
			System.out.println("Report at: " + file.getAbsolutePath()
					.replaceAll(Pattern.quote(path), "http://10.207.16.9/").replaceAll(Pattern.quote("\\"), "/"));
		} else {
			System.out.println("Report at: " + file.getAbsolutePath());
		}
	}

	public static void generateDIffReportAsCSV() {
		File[] files = new File(AppConstants.DIFF_FOLDER).listFiles((File dir, String name) -> {
			return name.endsWith(".diff");
		});
		String path = "";
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
			String str = builder.toString();
			if (AppConstants.WEB) {
				path = new File(AppConstants.FOLDER).getAbsolutePath().substring(0, AppConstants.FOLDER.indexOf("DGT"));
				str = str.replaceAll(Pattern.quote(path), "http://10.207.16.9/").replaceAll(Pattern.quote("\\"), "/");
			}
			rows.add(str);
		}
		File file = new File(AppConstants.DIFF_FOLDER, "Diff-Report.csv");
		try {
			FileUtils.writeLines(file, "utf-8", rows);
		} catch (IOException e) {
			LoggerFactory.getLogger(CSVReporter.class).debug("Error writing report csv", e);
		}
		if (AppConstants.WEB) {
			System.out.println("Report at: " + file.getAbsolutePath()
					.replaceAll(Pattern.quote(path), "http://10.207.16.9/").replaceAll(Pattern.quote("\\"), "/"));
		} else {
			System.out.println("Report at: " + file.getAbsolutePath());
		}
	}

}
