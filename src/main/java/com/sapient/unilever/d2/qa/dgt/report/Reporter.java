package com.sapient.unilever.d2.qa.dgt.report;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.page.diff.DiffInfo;

public abstract class Reporter {
	protected int passedCount;
	protected int failedCount;

	Reporter() {
		AppConstants.END_TIME = System.currentTimeMillis();
	}

	protected abstract void readData(String location);

	protected abstract DiffInfo getDiffInfo();

}
