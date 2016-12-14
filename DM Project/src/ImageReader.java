import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageReader {
	// Dimensions of the image
	public final int width;
	public final int height;

	public final int[][] pixels;

	public ImageReader(String file) throws IOException {
		// create a new BufferedImage for the test file, will throw an
		BufferedImage bi = ImageIO.read(new File(file));

		width = bi.getWidth();
		height = bi.getHeight();

//		 System.out.println("width, height: " + width + " " + height);

		int[] rawPixels = new int[width * height];
		rawPixels = bi.getRGB(0, 0, width, height, null, 0, width);
		// Creates the Pixel object array
		pixels = new int[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Fills the array with the respective pixels
				pixels[i][j] = rawPixels[i * width + j];
				// System.out.format("Row %02d, Column %02d: ", j+1, i+1);
				// System.out.println(p.toString());
				// System.out.println();
			}
		}
	}

}
