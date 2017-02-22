package com.sachin.qa.spider.helpers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public Page readPage(File file) {
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
			Page link = (Page) in.readObject();
			in.close();
			return link;
		} catch (IOException | ClassNotFoundException e) {
			logger.debug("Error in Reading File: " + file.getAbsolutePath(), e);
		}
		return null;
	}
}
