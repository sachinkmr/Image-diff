package com.sapient.unilever.d2.qa.dgt.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.AppConstants;

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

    public static String getURLHtml(String... data) {
	SSLContext sslContext = null;
	try {
	    sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();
	} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e1) {
	    logger.error("Unable to add SSL Context.\n", e1);
	}
	CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	if (data[1] != null && !data[1].isEmpty()) {
	    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(data[1], data[2]));
	}
	CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext)
		.setSSLHostnameVerifier(new NoopHostnameVerifier()).setDefaultCredentialsProvider(credentialsProvider)
		.build();
	HttpGet httpGet = new HttpGet(data[0]);
	String result = null;
	try {
	    HttpResponse response = client.execute(httpGet);
	    result = EntityUtils.toString(response.getEntity(), "utf-8");
	} catch (ParseException | IOException e) {
	    logger.error("Error fetching response.\n", e);
	}
	return result;
    }

}
