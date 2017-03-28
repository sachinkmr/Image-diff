package com.sachin.qa.page.html;

import com.sachin.qa.page.DifferenceType;

public class HtmlType extends DifferenceType {
	public HtmlType(String url) {
		super(url, ".html");
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void apply() throws Exception {

	}

	@Override
	public void close() throws Exception {

	}

	@Override
	public void differ(DifferenceType type) throws Exception {
		// TODO Auto-generated method stub

	}

}
