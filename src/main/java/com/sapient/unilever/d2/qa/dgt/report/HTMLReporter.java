package com.sapient.unilever.d2.qa.dgt.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

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
			String s = Resources.getResource("com/sapient/unilever/d2/qa/dgt/templates").getFile();
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
			Writer out = new FileWriter(new File(AppConstants.FOLDER, "JsReport.html"));
			temp.process(root, out);
			out.close();
		} catch (IOException | TemplateException e) {
			LOGGER.error("Unable generate file", e);
		}
	}

	public void generateImageReport(ImageReporter reporter) {
		try {
			Map<String, ImageReporter> root = new HashMap<>();
			root.put("dashboard", reporter);
			Template temp = cfg.getTemplate("image.ftl");
			Writer out = new FileWriter(new File(AppConstants.FOLDER, "ImageReport.html"));
			temp.process(root, out);
			out.close();
		} catch (IOException | TemplateException e) {
			LOGGER.error("Unable generate file", e);
		}
	}

}
