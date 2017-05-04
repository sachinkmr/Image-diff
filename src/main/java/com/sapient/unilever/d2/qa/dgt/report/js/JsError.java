package com.sapient.unilever.d2.qa.dgt.report.js;

public class JsError {
    private JsErrorType type;
    private String message;
    private String pageUrl;
    private String browserName;
    private JsCategoryType category;

    public JsCategoryType getCategory() {
	return category;
    }

    public String getBrowserName() {
	return browserName;
    }

    public JsErrorType getType() {
	return type;
    }

    public String getErrorType() {
	return type.toString();
    }

    public String getMessage() {
	return message;
    }

    public String getPageUrl() {
	return pageUrl;
    }

    public void setCategory(String errorString) {
	this.category = JsCategoryType.getJsCategoryType(errorString);
    }

    public void setType(String type) {
	this.type = JsErrorType.getJsErrorType(type);
    }

    public void setBrowserName(String browserName) {
	this.browserName = browserName;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public void setPageUrl(String pageUrl) {
	this.pageUrl = pageUrl;
    }

    @Override
    public String toString() {
	return type.toString() + " : " + message;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((message == null) ? 0 : message.hashCode());
	result = prime * result + ((type == null) ? 0 : type.hashCode());
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
	JsError other = (JsError) obj;
	if (message == null) {
	    if (other.message != null)
		return false;
	} else if (!message.equals(other.message))
	    return false;
	if (type != other.type)
	    return false;
	return true;
    }

}
