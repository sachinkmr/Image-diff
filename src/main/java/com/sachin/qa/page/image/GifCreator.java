package com.sachin.qa.page.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GifCreator {
	protected static final Logger logger = LoggerFactory.getLogger(GifCreator.class);

	public static void createGif(String preImage, String postImage, String gifImage) {
		try {
			BufferedImage firstImage = ImageIO.read(Files.newInputStream(Paths.get(preImage)));
			BufferedImage secondImage = ImageIO.read(Files.newInputStream(Paths.get(postImage)));
			ImageOutputStream output = new FileImageOutputStream(new File(gifImage));
			GifSequenceWriter writer = new GifSequenceWriter(output, firstImage.getType(), 500, true);
			writer.writeToSequence(firstImage);
			writer.writeToSequence(secondImage);
			writer.close();
			output.close();
		} catch (IOException ex) {
			logger.warn("Unable to create gif image", ex);
		}
	}

}
