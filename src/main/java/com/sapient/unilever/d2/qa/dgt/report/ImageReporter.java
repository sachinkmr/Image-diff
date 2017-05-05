package com.sapient.unilever.d2.qa.dgt.report;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.page.PageInfo;
import com.sapient.unilever.d2.qa.dgt.page.diff.Differentiator;
import com.sapient.unilever.d2.qa.dgt.page.image.ImageDiffInfo;
import com.sapient.unilever.d2.qa.dgt.report.image.ImageData;
import com.sapient.unilever.d2.qa.dgt.utils.StreamUtils;

public class ImageReporter extends Reporter {
    private Map<String, List<ImageData>> category;

    public ImageReporter() {
	super();
	this.category = new HashMap<>();
	if (AppConstants.HAS_DIFF) {
	    getDiffInfo();
	} else {
	    readData();
	}
    }

    public Map<String, List<ImageData>> getCategory() {
	return category;
    }

    public List<ImageData> getImages() {
	List<ImageData> images = new ArrayList<>();
	for (String key : category.keySet()) {
	    images.addAll(category.get(key));
	}
	return images;
    }

    @Override
    protected void readData() {
	File[] files = new File(AppConstants.FOLDER).listFiles((File dir, String name) -> {
	    return name.endsWith(".info");
	});
	List<ImageData> images = new ArrayList<>();
	for (File file : files) {
	    PageInfo pageInfo = StreamUtils.readPageInfo(file);
	    ImageData data = new ImageData();
	    data.setUrl(pageInfo.getPageUrl());
	    data.setBrowser(pageInfo.getBrowserName());
	    data.setImg(pageInfo.getType(".png").getResourcePath());
	    images.add(data);
	    passedPage++;
	}
	category.put("sac", images);
    }

    @Override
    protected void getDiffInfo() {
	File[] files = new File(AppConstants.DIFF_FOLDER).listFiles((File dir, String name) -> {
	    return name.endsWith(".diff");
	});
	List<ImageData> mismatched = new ArrayList<>();
	List<ImageData> matched = new ArrayList<>();
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
	    data.setImg(image.getPostInfo().getType(".png").getResourcePath());
	    data.setPre(image.getPreInfo().getType(".png").getResourcePath());
	    data.setMatched(image.isMatched());
	    data.setPng(image.getProperties().get("png"));
	    data.setGif(image.getProperties().get("gif"));
	    data.setDiff(image.getDiffSize());
	    if (image.isMatched()) {
		matched.add(data);
	    } else {
		mismatched.add(data);
	    }
	}
	category.put("Matched", matched);
	category.put("MisMatched", mismatched);
    }

}
