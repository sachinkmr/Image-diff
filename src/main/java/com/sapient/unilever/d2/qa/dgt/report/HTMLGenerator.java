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
import com.sapient.unilever.d2.qa.dgt.BuildType;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class HTMLGenerator {
    private Configuration cfg;
    protected static final Logger LOGGER = LoggerFactory.getLogger(HTMLGenerator.class);

    public HTMLGenerator() {
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

    public void generateJSReport() {
	if (AppConstants.JS_DIFF) {
	    JsReporter reporter = new JsReporter();
	    try {
		Map<String, JsReporter> root = new HashMap<>();
		root.put("dashboard", reporter);
		Template temp = cfg.getTemplate("jsTemplate.ftl");
		File file = null;
		file = new File(AppConstants.FOLDER, "JsReport.html");
		Writer out = new FileWriter(file);
		temp.process(root, out);
		out.close();
		if (AppConstants.WEB) {
		    String path = new File(AppConstants.FOLDER).getAbsolutePath().substring(0,
			    AppConstants.FOLDER.indexOf("DGT"));
		    try {
			URL url = new URL(file.getAbsolutePath().replaceAll(Pattern.quote(path), "http://10.207.16.9/")
				.replaceAll(Pattern.quote("\\"), "/"));
			URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(),
				url.getPath(), url.getQuery(), url.getRef());
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

    public void generateImageReport() {
	if (AppConstants.IMAGE_DIFF) {
	    ImageReporter reporter = new ImageReporter();
	    try {
		Map<String, ImageReporter> root = new HashMap<>();
		root.put("dashboard", reporter);
		Template temp = cfg.getTemplate("image.ftl");
		File file = null;
		if (AppConstants.BUILD_TYPE == BuildType.POST) {
		    reporter.diff = true;
		    file = new File(AppConstants.DIFF_FOLDER, "ImageReport.html");
		    Writer out = new FileWriter(file);
		    temp.process(root, out);
		    out.close();
		    if (AppConstants.WEB) {
			String path = new File(AppConstants.DIFF_FOLDER).getAbsolutePath().substring(0,
				AppConstants.FOLDER.indexOf("DGT"));
			modifyPath(file, path);
		    } else {
			System.out.println("Report at: " + file.getAbsolutePath());
		    }
		}
		file = new File(AppConstants.FOLDER, "ImageReport.html");
		Writer out = new FileWriter(file);
		reporter.diff = false;
		temp.process(root, out);
		out.close();
		if (AppConstants.WEB) {
		    String path = new File(AppConstants.FOLDER).getAbsolutePath().substring(0,
			    AppConstants.FOLDER.indexOf("DGT"));
		    modifyPath(file, path);
		} else {
		    System.out.println("Report at: " + file.getAbsolutePath());
		}
	    } catch (IOException | TemplateException e) {
		LOGGER.error("Unable generate file", e);
	    }
	}
    }

    private static void modifyPath(File file, String path) {
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
    }

}
