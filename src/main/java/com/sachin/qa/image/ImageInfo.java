package com.sachin.qa.image;

import java.awt.image.BufferedImage;

public class ImageInfo {
	private final String pageUrl;
	private String imagePathOld;
	private BufferedImage imageOld;
	private String imagePathNew;
	private BufferedImage imageNew;
	private boolean matched;
	private boolean processed;
	private String diffImage;
	private String diffGiff;

	public ImageInfo(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public String getImagePathOld() {
		return imagePathOld;
	}

	public void setImagePathOld(String imagePathOld) {
		this.imagePathOld = imagePathOld;
	}

	public BufferedImage getImageOld() {
		return imageOld;
	}

	public void setImageOld(BufferedImage imageOld) {
		this.imageOld = imageOld;
	}

	public String getImagePathNew() {
		return imagePathNew;
	}

	public void setImagePathNew(String imagePathNew) {
		this.imagePathNew = imagePathNew;
	}

	public BufferedImage getImageNew() {
		return imageNew;
	}

	public void setImageNew(BufferedImage imageNew) {
		this.imageNew = imageNew;
	}

	public boolean isMatched() {
		return matched;
	}

	public void setMatched(boolean matched) {
		this.matched = matched;
	}

	public String getDiffImage() {
		return diffImage;
	}

	public void setDiffImage(String diffImage) {
		this.diffImage = diffImage;
	}

	public String getDiffGiff() {
		return diffGiff;
	}

	public void setDiffGiff(String diffGiff) {
		this.diffGiff = diffGiff;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pageUrl == null) ? 0 : pageUrl.hashCode());
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
		ImageInfo other = (ImageInfo) obj;
		if (pageUrl == null) {
			if (other.pageUrl != null)
				return false;
		} else if (!pageUrl.equals(other.pageUrl))
			return false;
		return true;
	}

}
