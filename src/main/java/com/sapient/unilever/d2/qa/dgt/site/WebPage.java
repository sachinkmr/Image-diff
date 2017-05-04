package com.sapient.unilever.d2.qa.dgt.site;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WebPage {
    private String template;
    private String url;

    public WebPage(String url) {
	this.url = url;
    }

    public String getTemplate() {
	return this.template;
    }

    public void setTemplate(String html) {
	Document document = Jsoup.parse(html);
	for (Element e : document.select("section>script")) {
	    String str = e.html();
	    if (str.contains("pageType")) {
		Pattern p1 = Pattern.compile("pageType.*\",");
		Matcher m1 = p1.matcher(str);
		if (m1.find()) {
		    String type = m1.group().substring(m1.group().indexOf("pageType") + 8, m1.group().indexOf("\","))
			    .replaceAll("[: \"]", "").trim();
		    type = type.isEmpty() ? "GenericPage" : type;
		    this.template = type;
		    break;
		}
	    }
	}
	template = template == null || template.isEmpty() ? "GenericPage" : template;
    }

    public String getUrl() {
	return this.url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((url == null) ? 0 : url.hashCode());
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
	WebPage other = (WebPage) obj;
	if (url == null) {
	    if (other.url != null)
		return false;
	} else if (!url.equals(other.url))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return url;
    }

}
