package com.sachin.qa.page.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.page.Featurable;
import com.sachin.qa.page.PageInfo;
import com.sachin.qa.page.diff.DiffInfo;
import com.sachin.qa.page.js.JsDiffInfo;

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
			BufferedImage img1 = ImageIO.read(Files.newInputStream(Paths.get(preImage)));
			BufferedImage img2 = ImageIO.read(Files.newInputStream(Paths.get(postImage)));
			ImageDiff diff = new ImageDiffer().makeDiff(img1, img2).withDiffSizeTrigger(AppConstants.IGNORED_PIXELS);
			matched = !diff.hasDiff();
			if (diff.hasDiff()) {
				diffSize = diff.getDiffSize();
				BufferedImage diffImage = diff.getMarkedImage();
				ImageIO.write(diffImage, "png", Files.newOutputStream(Paths.get(png.getAbsolutePath())));
				GifCreator.createGif(preImage, postImage, gif.getAbsolutePath());
				properties.put("png", png.getAbsolutePath());
				properties.put("gif", gif.getAbsolutePath());
			}
			processed = true;
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
