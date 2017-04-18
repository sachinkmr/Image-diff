package com.sapient.unilever.d2.qa.dgt.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;
import com.sapient.unilever.d2.qa.dgt.AppConstants;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class HTMLReporter {
	private Configuration cfg;
	protected static final Logger LOGGER = LoggerFactory.getLogger(HTMLReporter.class);

	public HTMLReporter() {
		cfg = new Configuration(Configuration.VERSION_2_3_26);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		cfg.setLogTemplateExceptions(false);
		try {
			String s = Resources.getResource("templates").getFile();
			s = s.substring(1);
			cfg.setDirectoryForTemplateLoading(new File(s));
		} catch (IOException e) {
			LOGGER.error("Unable to load template file", e);
		}

	}

	public void generateJSReport(JsReporter reporter) {
		try {
			Map<String, JsReporter> root = new HashMap<>();
			root.put("dashboard", reporter);
			Template temp = cfg.getTemplate("jsTemplate.ftl");
			File file = new File(AppConstants.FOLDER, "JsReport.html");
			Writer out = new FileWriter(file);
			temp.process(root, out);
			out.close();
			if (AppConstants.WEB) {
				String path = new File(AppConstants.FOLDER).getAbsolutePath().substring(0,
						AppConstants.FOLDER.indexOf("DGT"));
				try {
					URL url = new URL(file.getAbsolutePath().replaceAll(Pattern.quote(path), "http://10.207.16.9/")
							.replaceAll(Pattern.quote("\\"), "/"));
					URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
							url.getQuery(), url.getRef());
					url = uri.toURL();
					System.out.println("Report at: " + uri);
				} catch (MalformedURLException | URISyntaxException e) {
					LoggerFactory.getLogger(CSVReporter.class).debug("Error encoding report path", e);
				}
			} else {
				System.out.println("Report at: " + file.getAbsolutePath());
			}
		} catch (IOException | TemplateException e) {
			LOGGER.error("Unable generate file", e);
		}
	}

	public void generateImageReport(ImageReporter reporter) {
		try {
			Map<String, ImageReporter> root = new HashMap<>();
			root.put("dashboard", reporter);
			Template temp = cfg.getTemplate("image.ftl");
			File file = new File(AppConstants.DIFF_FOLDER, "ImageReport.html");
			Writer out = new FileWriter(file);
			temp.process(root, out);
			out.close();
			if (AppConstants.WEB) {
				String path = new File(AppConstants.FOLDER).getAbsolutePath().substring(0,
						AppConstants.FOLDER.indexOf("DGT"));
				try {
					URL url = new URL(file.getAbsolutePath().replaceAll(Pattern.quote(path), "http://10.207.16.9/")
							.replaceAll(Pattern.quote("\\"), "/"));
					URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
							url.getQuery(), url.getRef());
					url = uri.toURL();
					System.out.println("Report at: " + uri);
				} catch (MalformedURLException | URISyntaxException e) {
					LoggerFactory.getLogger(CSVReporter.class).debug("Error encoding report path", e);
				}
			} else {
				System.out.println("Report at: " + file.getAbsolutePath());
			}
		} catch (IOException | TemplateException e) {
			LOGGER.error("Unable generate file", e);
		}
	}

}
