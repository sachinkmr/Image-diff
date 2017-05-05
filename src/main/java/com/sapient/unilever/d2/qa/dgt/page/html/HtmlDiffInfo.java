package com.sapient.unilever.d2.qa.dgt.page.html;

import com.sapient.unilever.d2.qa.dgt.page.PageInfo;
import com.sapient.unilever.d2.qa.dgt.page.diff.DiffInfo;

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
