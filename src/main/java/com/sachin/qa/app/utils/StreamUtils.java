package com.sachin.qa.app.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.page.PageInfo;

import edu.uci.ics.crawler4j.crawler.Page;

public class StreamUtils {
	protected static final Logger logger = LoggerFactory.getLogger(StreamUtils.class);

	public void savePage(File file, Page page) {
		try {
			FileOutputStream fout = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(fout));
			out.writeObject(page);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.debug("Error in Writing File: " + file.getAbsolutePath(), e);
		}
	}

	public static PageInfo fetchInfo(File file) {
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
			PageInfo link = (PageInfo) in.readObject();
			in.close();
			return link;
		} catch (IOException | ClassNotFoundException e) {
			logger.debug("Error in Reading File: " + file.getAbsolutePath(), e);
		}
		return null;
	}

	public static void storeImageInfo(PageInfo info) {
		String fileName = Base64.encodeBase64URLSafeString((info.getPageUrl() + info.getBrowserName()).getBytes());
		File file = new File(AppConstants.CRAWLER_DATA, fileName + ".info");
		try {
			FileOutputStream fout = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(fout));
			out.writeObject(info);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.debug("Error in Writing File: " + file.getAbsolutePath(), e);
		}
	}

	public static PageInfo readImageInfo(String url, String name) {
		String fileName = Base64.encodeBase64URLSafeString((url + name).getBytes());
		File file = new File(AppConstants.CRAWLER_DATA, fileName + ".info");
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
			PageInfo info = (PageInfo) in.readObject();
			in.close();
			return info;
		} catch (IOException | ClassNotFoundException e) {
			logger.debug("Error in Reading File: " + file.getAbsolutePath(), e);
		}
		return null;
	}

	public static void generateReportCSV() throws IOException {
		File file = new File(AppConstants.APP_DATA, "Report.csv");
		File[] files = new File(AppConstants.CRAWLER_DATA).listFiles();
		String header = "url,beforeImage,afterImage,matched,diff,diffImage,gifImage,browser";
		List<String> list = new ArrayList<>();
		list.add(header);
		logger.info("Generating Report: " + file.getAbsolutePath());
		for (File f : files) {
			StringBuffer s = new StringBuffer();
			// PageInfo info = fetchInfo(f);
			// s.append(info.getPageUrl());
			// s.append(",");
			// s.append(info.getImagePathOld());
			// s.append(",");
			// s.append(AppConstants.IS_DIFF ? info.getImagePathNew() : "");
			// s.append(",");
			// s.append(AppConstants.IS_DIFF ? info.isMatched() : "");
			// s.append(",");
			// s.append(AppConstants.IS_DIFF ? info.getDiffSize() : "");
			// s.append(",");
			// s.append(AppConstants.IS_DIFF ? info.getDiffImage() : "");
			// s.append(",");
			// s.append(AppConstants.IS_DIFF ? info.getDiffGiff() : "");
			// s.append(",");
			// s.append(info.getBrowserName());
			// list.add(s.toString());

		}
		FileUtils.writeLines(file, "UTF-8", list);
	}

	public static PageInfo readPageInfo(String url, String name) {
		String fileName = Base64.encodeBase64URLSafeString((url + name).getBytes());
		File file = new File(AppConstants.PRE_DATA, fileName + ".info");
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
			PageInfo info = (PageInfo) in.readObject();
			in.close();
			return info;
		} catch (IOException | ClassNotFoundException e) {
			logger.debug("Error in Reading File: " + file.getAbsolutePath(), e);
		}
		return null;
	}

	public static void writeImageInfo(PageInfo info) {
		String fileName = Base64.encodeBase64URLSafeString((info.getPageUrl() + info.getBrowserName()).getBytes());
		File file = new File(AppConstants.FOLDER, fileName + ".info");
		try {
			FileOutputStream fout = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(fout));
			out.writeObject(info);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.debug("Error in Writing File: " + file.getAbsolutePath(), e);
		}
	}
}
