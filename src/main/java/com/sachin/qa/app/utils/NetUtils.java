package com.sachin.qa.app.utils;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;

public class NetUtils {
    protected static final Logger logger = LoggerFactory.getLogger(NetUtils.class);


    public static Response getLinkResponse(String... data) throws ParseException, ClientProtocolException, IOException {
	Request request = Request.Get(data[0])
		.addHeader("user-agent",
			AppConstants.PROPERTIES.getProperty("crawler.userAgentString",
				"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0"))
		.addHeader("Accept-Encoding",
			"gzip, compress, deflate, br, identity, exi, pack200-gzip, bzip2, lzma, peerdist, sdch, xpress, xz")
		.connectTimeout(
			Integer.parseInt(AppConstants.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")))
		.socketTimeout(
			Integer.parseInt(AppConstants.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")));
	if (data.length > 1 && null != data[1] && !data[1].trim().isEmpty()) {
	    String login = data[1] + ":" + data[2];
	    String base64login = new String(Base64.encodeBase64(login.getBytes()));
	    request.addHeader("Authorization", "Basic " + base64login);
	}
	return request.execute();
    }


    public static String getRedirectedURL(String... data) {
	try {
	    Connection con = Jsoup.connect(data[0])
		    .header("user-agent",
			    AppConstants.PROPERTIES.getProperty("crawler.userAgentString",
				    "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0"))
		    .timeout(Integer
			    .parseInt(AppConstants.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")))
		    .followRedirects(true);
	    if (data.length > 1 && null != data[1] && !data[1].trim().isEmpty()) {
		String login = data[1] + ":" + data[2];
		String base64login = new String(Base64.encodeBase64(login.getBytes()));
		con.header("Authorization", "Basic " + base64login);
	    }
	    return con.execute().url().toExternalForm();
	} catch (IOException e) {
	    logger.error("Error fetching response.\n", e);
	}
	return null;
    }

}
