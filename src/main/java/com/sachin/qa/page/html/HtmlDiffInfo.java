package com.sachin.qa.page.html;

import com.sachin.qa.page.PageInfo;
import com.sachin.qa.page.diff.DiffInfo;

public class HtmlDiffInfo extends DiffInfo {
	private static final long serialVersionUID = 1L;

	public HtmlDiffInfo(PageInfo preInfo, PageInfo postInfo) {
		super(".html", preInfo, postInfo);

	}

	@Override
	public DiffInfo call() {
		return this;
	}

}
