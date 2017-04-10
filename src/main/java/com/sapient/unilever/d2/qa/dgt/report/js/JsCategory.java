package com.sapient.unilever.d2.qa.dgt.report.js;

import java.util.HashSet;
import java.util.Set;

public class JsCategory {
	private final String name;
	private Set<JsPage> pages;
	private int errorCount;
	private int warningCount;

	public JsCategory(String name) {
		super();
		this.name = name;
		pages = new HashSet<>();
	}

	public int getErrorCount() {
		return errorCount;
	}

	public int getWarningCount() {
		return warningCount;
	}

	public String getName() {
		return name;
	}

	public void addPage(JsPage page) {
		pages.add(page);
		errorCount += page.getErrorCount();
		warningCount += page.getWarningCount();
	}

	public Set<JsPage> getPages() {
		return pages;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JsCategory other = (JsCategory) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

}
