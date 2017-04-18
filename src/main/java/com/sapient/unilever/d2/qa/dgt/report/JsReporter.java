package com.sapient.unilever.d2.qa.dgt.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.db.DBManager;
import com.sapient.unilever.d2.qa.dgt.page.PageInfo;
import com.sapient.unilever.d2.qa.dgt.report.js.JsCategory;
import com.sapient.unilever.d2.qa.dgt.report.js.JsCategoryType;
import com.sapient.unilever.d2.qa.dgt.report.js.JsError;
import com.sapient.unilever.d2.qa.dgt.report.js.JsErrorType;
import com.sapient.unilever.d2.qa.dgt.report.js.JsPage;
import com.sapient.unilever.d2.qa.dgt.utils.StreamUtils;

public class JsReporter extends Reporter {
	protected static final Logger LOGGER = LoggerFactory.getLogger(JsReporter.class);
	private List<JsCategory> categories;
	private int errors;
	private int warnings;

	public List<JsCategory> getCategories() {
		return categories;
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
		categories = new ArrayList<>();
		File[] files = new File(AppConstants.FOLDER).listFiles((File dir, String name) -> {
			return name.endsWith(".info");
		});
		for (File file : files) {
			PageInfo pageInfo = StreamUtils.readPageInfo(file);
			String path = pageInfo.getType(".jsLog").getResourcePath();
			try {
				List<String> list = FileUtils.readLines(new File(path), "utf-8");
				if (list.size() > 3) {
					String pageUrl = list.get(0).replace("Logging console logs for: ", "").trim();
					String browserName = list.get(1).replace("Browser Type: ", "").trim();
					JsPage page = new JsPage(pageUrl, browserName);
					for (int i = 3; i < list.size(); i++) {
						JsErrorType type = page.addJsError(list.get(i), pageUrl, browserName);
						if (type.equals(JsErrorType.ERROR))
							errors++;
						if (type.equals(JsErrorType.WARNING))
							warnings++;
						String cat = JsCategoryType.getJsCategoryType(list.get(i)).toString();
						JsCategory category = new JsCategory(cat);
						category = getCategory(category);
						category.addPage(page);
						categories.add(category);
					}
					dumpIntoDB(page);
					if (list.toString().contains("SEVERE")) {
						failedPage++;
					}
				}
				if (list.size() > 3 && !list.toString().contains("SEVERE")) {
					passedPage++;
				}
			} catch (IOException e) {
				LOGGER.error("Unable to read jsLog file : " + path, e);
			}
		}
	}

	private JsCategory getCategory(JsCategory cat) {
		for (int i = 0; i < categories.size(); i++) {
			if (categories.get(i).getName().equals(cat.getName()))
				return categories.remove(i);
		}
		return cat;
	}

	@Override
	public void getDiffInfo() {

	}

	public void dumpIntoDB(JsPage page) {
		new Thread(() -> {
			DBManager manager = new DBManager();
			Document arr = new Document("url", page.getUrl());
			arr.append("browser", page.getBrowser());
			arr.append("type", "JS Logs");
			JSONObject errors = new JSONObject();
			int i = 1;
			for (JsError error : page.getJsError()) {
				JSONObject errorDocument = new JSONObject();
				errorDocument.put("Type", error.getErrorType());
				errorDocument.put("Category", error.getCategory().toString());
				errorDocument.put("Message", error.getMessage());
				if (error.getType() == JsErrorType.WARNING || error.getType() == JsErrorType.ERROR) {
					errors.put(Integer.toString(i++), errorDocument);
				}
			}
			arr.append("errors", errors.toString());
			try {
				manager.getMongoDB().getCollection(AppConstants.TIME_STAMP).insertOne(arr);
			} catch (Exception ex) {
				LoggerFactory.getLogger(JsReporter.class).error("Error in inserting:", ex);
			}
			manager.close();
		}).start();
	}

}
