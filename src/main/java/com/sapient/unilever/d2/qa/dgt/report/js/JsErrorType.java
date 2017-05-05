package com.sapient.unilever.d2.qa.dgt.report.js;

import java.util.logging.Level;

public enum JsErrorType {
    ERROR, WARNING, INFO, OTHER;

    public static JsErrorType getJsErrorType(String type) {
	if (type.equals(Level.SEVERE.toString()))
	    return JsErrorType.ERROR;
	if (type.equals(Level.WARNING.toString()))
	    return JsErrorType.WARNING;
	return OTHER;
    }
}
