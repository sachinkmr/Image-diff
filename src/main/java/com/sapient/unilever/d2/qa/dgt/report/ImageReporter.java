package com.sapient.unilever.d2.qa.dgt.report;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.page.PageInfo;
import com.sapient.unilever.d2.qa.dgt.page.diff.Differentiator;
import com.sapient.unilever.d2.qa.dgt.page.image.ImageDiffInfo;
import com.sapient.unilever.d2.qa.dgt.report.image.ImageData;
import com.sapient.unilever.d2.qa.dgt.utils.StreamUtils;

public class ImageReporter extends Reporter {
	private List<ImageData> images;

	public ImageReporter() {
		super();
		this.images = new ArrayList<>();
		if (AppConstants.HAS_DIFF) {
			getDiffInfo();
		} else {
			readData();
		}

	}

	public List<ImageData> getImages() {
		return images;
	}

	@Override
	protected void readData() {
		File[] files = new File(AppConstants.FOLDER).listFiles((File dir, String name) -> {
			return name.endsWith(".info");
		});
		for (File file : files) {
			PageInfo pageInfo = StreamUtils.readPageInfo(file);
			ImageData data = new ImageData();
			data.setUrl(pageInfo.getPageUrl());
			data.setBrowser(pageInfo.getBrowserName());
			data.setPre(pageInfo.getTypes().get(0).getResourcePath());
			images.add(data);
		}
	}

	@Override
	protected void getDiffInfo() {
		File[] files = new File(AppConstants.DIFF_FOLDER).listFiles((File dir, String name) -> {
			return name.endsWith(".diff");
		});
		for (File file : files) {
			Differentiator diff = StreamUtils.readDiffInfo(file);
			ImageDiffInfo image = (ImageDiffInfo) diff.getDiffInfos().get(0);
			if (image.isMatched())
				passedPage++;
			else
				failedPage++;
			ImageData data = new ImageData();
			data.setUrl(image.getPreInfo().getPageUrl());
			data.setBrowser(image.getPreInfo().getBrowserName());
			data.setPre(image.getPreInfo().getTypes().get(0).getResourcePath());
			data.setPost(image.getPostInfo().getTypes().get(0).getResourcePath());
			data.setMatched(image.isMatched());
			data.setPng(image.getProperties().get("png"));
			data.setGif(image.getProperties().get("gif"));
			data.setDiff(image.getDiffSize());
			image = null;
			images.add(data);
		}
	}

}
