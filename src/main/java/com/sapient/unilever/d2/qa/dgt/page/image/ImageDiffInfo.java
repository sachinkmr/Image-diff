package com.sapient.unilever.d2.qa.dgt.page.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.page.Featurable;
import com.sapient.unilever.d2.qa.dgt.page.PageInfo;
import com.sapient.unilever.d2.qa.dgt.page.diff.DiffInfo;
import com.sapient.unilever.d2.qa.dgt.page.js.JsDiffInfo;

import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

public class ImageDiffInfo extends DiffInfo {
    private static final long serialVersionUID = 1L;
    protected static final Logger logger = LoggerFactory.getLogger(ImageDiffInfo.class);

    public ImageDiffInfo(PageInfo preInfo, PageInfo postInfo) {
	super(".png", preInfo, postInfo);
	this.resourceFolder = AppConstants.DIFF_FOLDER + File.separator + "png";
    }

    @Override
    public DiffInfo call() {
	try {
	    File png = new File(this.resourceFolder, super.name + ".png");
	    png.getParentFile().mkdirs();
	    File gif = new File(AppConstants.DIFF_FOLDER + File.separator + "gif", super.name + ".gif");
	    gif.getParentFile().mkdirs();
	    properties.put("png", "");
	    properties.put("gif", "");
	    diffSize = 0;
	    String preImage = getFeatureType(preInfo.getTypes()).getResourcePath();
	    String postImage = getFeatureType(postInfo.getTypes()).getResourcePath();
	    InputStream preStream = Files.newInputStream(Paths.get(preImage));
	    InputStream postStream = Files.newInputStream(Paths.get(postImage));
	    OutputStream pngStream = Files.newOutputStream(Paths.get(png.getAbsolutePath()));
	    BufferedImage img1 = ImageIO.read(preStream);
	    BufferedImage img2 = ImageIO.read(postStream);
	    ImageDiff diff = new ImageDiffer().makeDiff(img1, img2).withDiffSizeTrigger(AppConstants.IGNORED_PIXELS);
	    matched = !diff.hasDiff();
	    if (diff.hasDiff()) {
		diffSize = diff.getDiffSize();
		BufferedImage diffImage = diff.getMarkedImage();
		ImageIO.write(diffImage, "png", pngStream);
		GifCreator.createGif(preImage, postImage, gif.getAbsolutePath());
		properties.put("png", png.getAbsolutePath());
		properties.put("gif", gif.getAbsolutePath());
	    }
	    processed = true;
	    preStream.close();
	    postStream.close();
	    pngStream.flush();
	    pngStream.close();
	} catch (Exception e) {
	    LoggerFactory.getLogger(JsDiffInfo.class).error("Error in calculating image diff", e);
	    this.errors.put(preInfo.getPageUrl(), e);
	}
	return this;
    }

    private Featurable getFeatureType(List<Featurable> types) {
	for (Featurable f : types) {
	    if (f.getType().equals(".png"))
		return f;
	}
	return null;
    }

}
