package com.sachin.qa.page;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class D2Page {
	private String template;
	private String site;
	private Map<Integer, String> components;
	protected static final Logger logger = LoggerFactory.getLogger(D2Page.class);

	public D2Page(String html, boolean isHtml) {
		Document document = Jsoup.parse(html);
		setComponents(document);
		template = setTemplate(document);
	}

	public D2Page(String... data) {
		try {
			Connection con = Jsoup.connect(data[0]).timeout(30000).followRedirects(true);
			if (data.length > 1 && null != data[1] && !data[1].trim().isEmpty()) {
				String login = data[1] + ":" + data[2];
				String base64login = new String(Base64.encodeBase64(login.getBytes()));
				con.header("Authorization", "Basic " + base64login);
			}
			this.site = data[0];
			Document document = con.execute().parse();
			setComponents(document);
			template = setTemplate(document);
		} catch (IOException e) {
			logger.error("Error fetching response.\n", e);
		}
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	private void setComponents(Document document) {
		components = new HashMap<>();
		List<Element> elements = document.select("div.component-wrapper> div.component");
		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			try {
				if (e.hasAttr("data-role")) {
					components.put(i, e.attr("data-role"));
				}
			} catch (Exception ex) {
				logger.error("Error fetching component info", ex);
			}
		}
	}

	private String setTemplate(Document document) {
		for (Element e : document.select("section>script")) {
			String str = e.html();
			if (str.contains("pageType")) {
				Pattern p1 = Pattern.compile("pageType.*\",");
				Matcher m1 = p1.matcher(str);
				if (m1.find()) {
					String type = m1.group().substring(m1.group().indexOf("pageType") + 8, m1.group().indexOf("\","))
							.replaceAll("[: \"]", "").trim();
					type = type.isEmpty() ? "GenericPage" : type;
					return type;
				}
			}
		}
		return "GenericPage";
	}

	public Map<Integer, String> getComponents() {
		return components;
	}

	public String getTemplate() {
		return template;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((components == null) ? 0 : components.hashCode());
		result = prime * result + ((template == null) ? 0 : template.hashCode());
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
		if (components == null) {
			if (other.components != null)
				return false;
		} else if (!components.equals(other.components))
			return false;
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (!template.equals(other.template))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return site;
	}
}
