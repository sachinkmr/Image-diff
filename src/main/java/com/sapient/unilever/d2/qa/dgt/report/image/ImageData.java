package com.sapient.unilever.d2.qa.dgt.report.image;

import java.io.File;
import java.util.regex.Pattern;

import com.sapient.unilever.d2.qa.dgt.AppConstants;

public class ImageData {
	private String url;
	private String browser;
	private boolean matched;
	private String pre;
	private String png;
	private String gif;
	private int diff;
	private String img;

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		if (AppConstants.WEB) {
			String path = new File(AppConstants.FOLDER).getAbsolutePath().substring(0,
					AppConstants.FOLDER.indexOf("DGT"));
			this.img = img.replaceAll(Pattern.quote(path), "http://10.207.16.9/");
		} else
			this.img = img;
		this.img = this.img.replaceAll(Pattern.quote("\\"), "/");
	}

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
			this.pre = pre.replaceAll(Pattern.quote(path), "http://10.207.16.9/");
		} else
			this.pre = pre;
		this.pre = this.pre.replaceAll(Pattern.quote("\\"), "/");
	}

	public String getPng() {
		return png;
	}

	public void setPng(String png) {
		if (AppConstants.WEB) {
			String path = new File(AppConstants.FOLDER).getAbsolutePath().substring(0,
					AppConstants.FOLDER.indexOf("DGT"));
			this.png = png.replaceAll(Pattern.quote(path), "http://10.207.16.9/");
		} else
			this.png = png;
		this.png = this.png.replaceAll(Pattern.quote("\\"), "/");
	}

	public String getGif() {
		return gif;
	}

	public void setGif(String gif) {
		if (AppConstants.WEB) {
			String path = new File(AppConstants.FOLDER).getAbsolutePath().substring(0,
					AppConstants.FOLDER.indexOf("DGT"));
			this.gif = gif.replaceAll(Pattern.quote(path), "http://10.207.16.9/");
		}
		this.gif = gif;
		this.gif = this.gif.replaceAll(Pattern.quote("\\"), "/");
	}

	public int getDiff() {
		return diff;
	}

	public void setDiff(int diff) {
		this.diff = diff;
	}

}
