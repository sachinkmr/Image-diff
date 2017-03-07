package com.sachin.qa.image.diff;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.image.ImageInfo;

import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

public class DiffFinder implements Runnable {
	protected static final Logger logger = LoggerFactory.getLogger(DiffFinder.class);
	private ImageInfo info;

	public DiffFinder(ImageInfo info) {
		this.info = info;
	}

	@Override
	public void run() {
		try {
			BufferedImage img1 = ImageIO.read(Files.newInputStream(Paths.get(info.getImagePathOld())));
			BufferedImage img2 = ImageIO.read(Files.newInputStream(Paths.get(info.getImagePathNew())));
			ImageDiff diff = new ImageDiffer().makeDiff(img1, img2).withDiffSizeTrigger(AppConstants.IGNORED_PIXELS);
			info.setMatched(diff.hasDiff());
			if (diff.hasDiff()) {
				BufferedImage diffImage = diff.getMarkedImage();
				ImageIO.write(diffImage, "png", Files.newOutputStream(Paths.get(info.getDiffImage())));
				GifCreator.createGif(info);
			} else {
				info.setDiffGiff("");
				info.setDiffImage("");
			}
			info.setProcessed(true);
		} catch (IOException e) {
			logger.warn("Unable to write image to disk", e);
		}
	}

}
