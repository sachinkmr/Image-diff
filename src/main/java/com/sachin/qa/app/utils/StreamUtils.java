package com.sachin.qa.app.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.page.PageInfo;
import com.sachin.qa.page.diff.Differentiator;

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
			logger.error("Error in Writing File: " + file.getAbsolutePath(), e);
		}
	}

	public static PageInfo readPageInfo(File file) {
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
			PageInfo link = (PageInfo) in.readObject();
			in.close();
			return link;
		} catch (Exception e) {
			logger.error("Error in Reading File: " + file.getAbsolutePath(), e);
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
			logger.error("Error in Writing File: " + file.getAbsolutePath(), e);
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
			logger.error("Error in Reading File: " + file.getAbsolutePath(), e);
		}
		return null;
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
			logger.error("Error in Reading File: " + file.getAbsolutePath(), e);
		}
		return null;
	}

	public static void writePageInfo(PageInfo info) {
		File file = new File(AppConstants.FOLDER, info.getPageName());
		try {
			FileOutputStream fout = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(fout));
			out.writeObject(info);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("Error in Writing File: " + file.getAbsolutePath(), e);
		}
	}

	public static void writeDiffInfo(Differentiator differ) {
		File file = new File(AppConstants.DIFF_FOLDER, differ.getName());
		try {
			FileOutputStream fout = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(fout));
			out.writeObject(differ);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("Error in Writing File: " + file.getAbsolutePath(), e);
		}
	}

	public static Differentiator readDiffInfo(File file) {
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
			Differentiator diff = (Differentiator) in.readObject();
			in.close();
			return diff;
		} catch (IOException | ClassNotFoundException e) {
			logger.error("Error in Writing File: " + file.getAbsolutePath(), e);
		}
		return null;
	}
}
