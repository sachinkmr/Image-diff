package com.sapient.unilever.d2.qa.dgt.report.js;

public enum JsCategoryType {
	NETWORK, MISCELLANEOUS, ANALYTICS, CQ_LIB, THIRD_PARTY, INTEGRATIONS, FONT_FILE, CODE, FILE, REQUEST;

	public static JsCategoryType getJsCategoryType(String error) {
		if (error.contains(" 404 ")) {
			return NETWORK;
		}
		if (error.contains(" 400 ") || error.contains(" 401 ")) {
			return REQUEST;
		}
		if (error.contains(" 500 ") || error.contains(" 501 ") || error.contains(" 502 ") || error.contains(" 503 ")) {
			return FILE;
		}
		if (error.contains("/satellite-") || error.contains("/linkid.js") || error.contains("/ec.js")) {
			return ANALYTICS;
		}
		if (error.contains("modern.min.") || error.contains("/tg.js") || error.contains("/satelliteLib-")) {
			return CQ_LIB;
		}
		if (error.contains("/cookies.js") || error.contains("/require.js") || error.contains("/addthis_widget.js")) {
			return THIRD_PARTY;
		}
		if (error.contains("bazaarvoice") || error.contains("kritique") || error.contains("RR_widget.js")
				|| error.contains("shoppable")) {
			return INTEGRATIONS;
		}
		if (error.contains("/config/shoppable.js") || error.contains("/adchoices.js") || error.contains("/main.")) {
			return CODE;
		}
		if (error.contains("use.typekit.com") || error.contains("fast.fonts.net")) {
			return FONT_FILE;
		}
		return MISCELLANEOUS;
	}
}
