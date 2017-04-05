package com.sachin.qa.dgt.page.html;

import com.sachin.qa.dgt.page.PageInfo;
import com.sachin.qa.dgt.page.diff.DiffInfo;

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
