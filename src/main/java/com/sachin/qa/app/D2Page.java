package com.sachin.qa.app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class D2Page {
    private String template;
    private String url;
    

    public D2Page(String url) {
	this.url=url;
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
	result = prime * result + ((this.template == null) ? 0 : this.template.hashCode());
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
	D2Page other = (D2Page) obj;
	if (this.template == null) {
	    if (other.template != null)
		return false;
	} else if (!this.template.equals(other.template))
	    return false;
	return true;
    }

}
