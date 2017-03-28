package com.sachin.qa.page.diff;

import java.util.List;

import org.slf4j.LoggerFactory;

import com.sachin.qa.page.DifferenceType;
import com.sachin.qa.page.PageInfo;

public class DiffGenerator implements Runnable {
	private PageInfo info1;
	private PageInfo info2;

	public DiffGenerator(PageInfo info1, PageInfo info2) {
		super();
		this.info1 = info1;
		this.info2 = info2;
	}

	@Override
	public void run() {
		for (DifferenceType type : info2.getTypes()) {
			try {
				type.differ(findType(info1.getTypes(), type.getType()));
			} catch (Exception e) {
				LoggerFactory.getLogger(DiffGenerator.class).error("Unable to get " + type + " diff", e);
			}
		}
	}

	private DifferenceType findType(List<DifferenceType> types, String type) {
		for (DifferenceType t : types) {
			if (t.getType().equals(type))
				return t;
		}
		return null;
	}

}
