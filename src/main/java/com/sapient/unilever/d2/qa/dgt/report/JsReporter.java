package com.sapient.unilever.d2.qa.dgt.report;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.db.DBManager;
import com.sapient.unilever.d2.qa.dgt.page.PageInfo;
import com.sapient.unilever.d2.qa.dgt.page.diff.DiffInfo;
import com.sapient.unilever.d2.qa.dgt.report.js.JsCategory;
import com.sapient.unilever.d2.qa.dgt.report.js.JsCategoryType;
import com.sapient.unilever.d2.qa.dgt.report.js.JsError;
import com.sapient.unilever.d2.qa.dgt.report.js.JsErrorType;
import com.sapient.unilever.d2.qa.dgt.report.js.JsPage;
import com.sapient.unilever.d2.qa.dgt.utils.StreamUtils;

public class JsReporter extends Reporter {
    protected static final Logger LOGGER = LoggerFactory.getLogger(JsReporter.class);
    private Set<JsCategory> categories;
    private String diff = "no";
    private int errors;
    private int warnings;

    public String getDiff() {
	return diff;
    }

    public Set<JsPage> getPages() {
	Set<JsPage> p = new HashSet<>();
	for (JsCategory cat : categories) {
	    p.addAll(cat.getPages());
	}
	return p;
    }

    public int getErrors() {
	return errors;
    }

    public int getWarnings() {
	return warnings;
    }

    public int getTotalCount() {
	return errors + warnings;
    }

    public JsReporter() {
	super();
	readData();
    }

    @Override
    protected void readData() {
	categories = new HashSet<>();
	File[] files = new File(AppConstants.FOLDER).listFiles((File dir, String name) -> {
	    return name.endsWith(".info");
	});
	for (File file : files) {
	    PageInfo pageInfo = StreamUtils.readPageInfo(file);
	    String path = pageInfo.getTypes().get(1).getResourcePath();
	    try {
		List<String> list = FileUtils.readLines(new File(path), "utf-8");
		if (list.size() > 3) {
		    String browserName = list.get(1).replace("Logging console logs for: ", "").trim();
		    String pageUrl = list.get(1).replace("Browser Type: ", "").trim();
		    for (int i = 3; i < list.size(); i++) {
			JsPage page = new JsPage(pageUrl, browserName);
			JsErrorType type = page.addJsError(list.get(i), pageUrl, browserName);
			if (type.equals(JsErrorType.ERROR.toString()))
			    errors++;
			if (type.equals(JsErrorType.WARNING.toString()))
			    warnings++;
			String cat = JsCategoryType.getJsCategoryType(list.get(i)).toString();
			JsCategory category = new JsCategory(cat);
			if (categories.contains(cat)) {
			    category = getCategory(cat);
			} else {
			    categories.add(category);
			}
			category.addPage(page);
			dumpIntoDB(page);
		    }
		    if (list.toString().contains("SEVERE")) {
			failedPage++;
		    }
		} else if (list.size() > 3 && !list.toString().contains("SEVERE")) {
		    passedPage++;
		}
	    } catch (IOException e) {
		LOGGER.error("Unable to read jsLog file : " + path, e);
	    }
	}
    }

    private JsCategory getCategory(String cat) {
	for (JsCategory c : categories) {
	    if (c.equals(cat))
		return c;
	}
	return null;
    }

    @Override
    public DiffInfo getDiffInfo() {
	diff = "yes";
	return null;
    }

    public void dumpIntoDB(JsPage page) {
	new Thread(() -> {
	    DBManager manager = new DBManager();
	    Document arr = new Document("url", page.getUrl());
	    arr.append("browser", page.getBrowser());
	    arr.append("type", "JS Logs");
	    JSONArray errors = new JSONArray();
	    for (JsError error : page.getJsError()) {
		JSONObject errorDocument = new JSONObject();
		errorDocument.put("Error Type", error.getErrorType());
		errorDocument.put("Error Category", error.getCategory().toString());
		errorDocument.put("Error Message", error.getMessage());
		errors.put(errorDocument);
	    }
	    arr.append("errors", Document.parse(errors.toString()));
	    try {
		manager.getMongoDB().getCollection(AppConstants.TIME_STAMP).insertOne(arr);
	    } catch (Exception ex) {
		LoggerFactory.getLogger(JsReporter.class).error("Error in inserting:", ex);
	    }
	    manager.close();
	}).start();
    }

}
