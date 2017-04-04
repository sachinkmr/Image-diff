package com.sachin.qa.page.diff;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.page.PageInfo;

public abstract class DiffInfo implements Serializable, Callable<DiffInfo> {

	private static final long serialVersionUID = 1L;
	protected boolean matched;
	protected boolean processed;
	protected PageInfo preInfo;
	protected PageInfo postInfo;
	protected int diffSize;
	protected String resourceFolder;
	protected Map<String, String> properties;
	protected Map<String, Exception> errors;
	protected String name;
	protected String type;

	public DiffInfo(String type, PageInfo preInfo, PageInfo postInfo) {
		this.type = type;
		this.preInfo = preInfo;
		this.postInfo = postInfo;
		this.resourceFolder = AppConstants.DIFF_FOLDER;
		this.name = preInfo.getPageName().replaceAll(".info", "");
		this.properties = new HashMap<>();
		this.errors = new HashMap<>();
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Map<String, Exception> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, Exception> errors) {
		this.errors = errors;
	}

	public String getResourceFolder() {
		return resourceFolder;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getDiffSize() {
		return diffSize;
	}

	public PageInfo getPreInfo() {
		return preInfo;
	}

	public PageInfo getPostInfo() {
		return postInfo;
	}

	public boolean isMatched() {
		return matched;
	}

	public boolean isProcessed() {
		return processed;
	}

}
