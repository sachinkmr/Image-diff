package com.sapient.unilever.d2.qa.dgt.report.image;

import java.io.File;
import java.util.regex.Pattern;

import com.sapient.unilever.d2.qa.dgt.AppConstants;

public class ImageData {
	private String url;
	private String browser;
	private boolean matched;
	private String pre;
	private String post;
	private String png;
	private String gif;
	private int diff;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public boolean isMatched() {
		return matched;
	}

	public void setMatched(boolean matched) {
		this.matched = matched;
	}

	public String getPre() {
		return pre;
	}

	public void setPre(String pre) {
		if (AppConstants.WEB) {
			String path = new File(AppConstants.FOLDER).getAbsolutePath().substring(0,
					AppConstants.FOLDER.indexOf("DGT"));
			this.pre = pre.replaceAll(Pattern.quote(path), "http://10.207.16.9/").replaceAll(Pattern.quote("\\"), "/");
		}
		this.pre = pre;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		if (AppConstants.WEB) {
			String path = new File(AppConstants.FOLDER).getAbsolutePath().substring(0,
					AppConstants.FOLDER.indexOf("DGT"));
			this.post = post.replaceAll(Pattern.quote(path), "http://10.207.16.9/").replaceAll(Pattern.quote("\\"),
					"/");
		}
		this.post = post;
	}

	public String getPng() {
		return png;
	}

	public void setPng(String png) {
		if (AppConstants.WEB) {
			String path = new File(AppConstants.FOLDER).getAbsolutePath().substring(0,
					AppConstants.FOLDER.indexOf("DGT"));
			this.png = png.replaceAll(Pattern.quote(path), "http://10.207.16.9/").replaceAll(Pattern.quote("\\"), "/");
		}
		this.png = png;
	}

	public String getGif() {
		return gif;
	}

	public void setGif(String gif) {
		if (AppConstants.WEB) {
			String path = new File(AppConstants.FOLDER).getAbsolutePath().substring(0,
					AppConstants.FOLDER.indexOf("DGT"));
			this.gif = gif.replaceAll(Pattern.quote(path), "http://10.207.16.9/").replaceAll(Pattern.quote("\\"), "/");
		}
		this.gif = gif;
	}

	public int getDiff() {
		return diff;
	}

	public void setDiff(int diff) {
		this.diff = diff;
	}

}
