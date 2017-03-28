package com.sachin.qa.page.diff;

import java.io.Serializable;

import com.sachin.qa.page.PageInfo;

public class PageDiffInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private PageInfo pre;
	private PageInfo post;
	private boolean matched;
	private boolean processed;
	private String diffImage;
	private String diffGiff;
	private int diffSize = 0;

	public PageInfo getPre() {
		return pre;
	}

	public void setPre(PageInfo pre) {
		this.pre = pre;
	}

	public PageInfo getPost() {
		return post;
	}

	public void setPost(PageInfo post) {
		this.post = post;
	}

	public boolean isMatched() {
		return matched;
	}

	public void setMatched(boolean matched) {
		this.matched = matched;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
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

	public int getDiffSize() {
		return diffSize;
	}

	public void setDiffSize(int diffSize) {
		this.diffSize = diffSize;
	}

}
