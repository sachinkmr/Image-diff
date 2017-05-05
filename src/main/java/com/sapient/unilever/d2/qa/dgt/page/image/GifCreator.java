package com.sapient.unilever.d2.qa.dgt.page.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
			InputStream preStream = Files.newInputStream(Paths.get(preImage));
			InputStream postStream = Files.newInputStream(Paths.get(postImage));
			BufferedImage firstImage = ImageIO.read(preStream);
			BufferedImage secondImage = ImageIO.read(postStream);
			ImageOutputStream output = new FileImageOutputStream(new File(gifImage));
			GifSequenceWriter writer = new GifSequenceWriter(output, firstImage.getType(), 500, true);
			writer.writeToSequence(firstImage);
			writer.writeToSequence(secondImage);
			writer.close();
			preStream.close();
			postStream.close();
			output.flush();
			output.close();
		} catch (IOException ex) {
			logger.warn("Unable to create gif image", ex);
		}
	}

}
