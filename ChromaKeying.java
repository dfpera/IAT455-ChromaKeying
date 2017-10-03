//
//
// File ChromaKeying.java
// IAT455 - Assignment 1
// ChromaKeying
//

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import javax.imageio.ImageIO;

class ChromaKeying extends Frame { 
	// Declare all images for UI
	BufferedImage personImage; 
	BufferedImage backgroundImage;
	BufferedImage matteImage; 
	BufferedImage keyMixedImage;

	int width; // Width of the image
	int height; // Height of the image

	public ChromaKeying() {
		// Get source images from the current directory
		try {
			personImage = ImageIO.read(new File("OriginalCropped.jpg"));
			backgroundImage = ImageIO.read(new File("Background.jpg"));
		} catch (Exception e) {
			System.out.println("Cannot load the provided image");
		}
		
		// Set window title
		this.setTitle("Assignment 1 - Chroma Keying");
		this.setVisible(true);

		// Get dimensions of the image
		width = personImage.getWidth();
		height = personImage.getHeight();

		// Process the matte and key mix images
		matteImage = ChromaKeyMatte(personImage);
		keyMixedImage = KeyMix(personImage, backgroundImage, matteImage);

		//Anonymous inner-class listener to terminate program
		this.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	public BufferedImage ChromaKeyMatte(BufferedImage img) {
		// Get dimensions of image
		int width = img.getWidth(); 
		int height = img.getHeight(); 

		// Duplicate image
		WritableRaster wRaster = img.copyData(null);
		BufferedImage copy = new BufferedImage(img.getColorModel(), wRaster, img.isAlphaPremultiplied(), null);

		// Process each pixel of img
		for (int x = 0; x < width; x++) { 
			for (int y = 0; y < height; y++) {
				// Initialize each color channel of pixel
				int rgb = img.getRGB(x, y);
				int alpha = (rgb >>> 24) & 0xff;
				int red = (rgb >>> 16) & 0xff;
				int green = (rgb >>> 8) & 0xff;
				int blue = rgb & 0xff; 
				
				// Convert to HSB
				float[] hsb = new float[3];
				Color.RGBtoHSB(red, green, blue, hsb);
				
				// Check if HSB values are within volume of chrominance key, then set matte to black
				if (hsb[0] >= 0.3f && hsb[0] <= 0.5f && hsb[1] >= 0.25f && hsb[2] >= 0.2f) {
					copy.setRGB(x, y, new Color(0, 0, 0).getRGB());
				// If not set matte to white
				} else {
					copy.setRGB(x, y, new Color(255, 255, 255).getRGB());
				}
			}
		}
		return copy; 
	}
	
	public BufferedImage KeyMix(BufferedImage fimg, BufferedImage bimg, BufferedImage matte) {
		// Get dimensions of image
		int width = fimg.getWidth(); 
		int height = fimg.getHeight(); 

		// Duplicate image
		WritableRaster wRaster = fimg.copyData(null);
		BufferedImage copy = new BufferedImage(fimg.getColorModel(), wRaster, fimg.isAlphaPremultiplied(), null);

		// Process each pixel of images
		for (int x = 0; x < width; x++) { 
			for (int y = 0; y < height; y++) {
				// Declare each color channel of pixels
				int[] rgbs = new int[3];
				float[] reds = new float[3];
				float[] greens = new float[3];
				float[] blues = new float[3];
				float red, green, blue;
				
				// Get each image's rgb values
				rgbs[0] = fimg.getRGB(x, y);
				rgbs[1] = bimg.getRGB(x, y);
				rgbs[2] = matte.getRGB(x, y);
				
				// Normalize each RGB value and assign it to it's associated channel
				for (int i = 0; i < rgbs.length; i++) {
					reds[i] = (float)((rgbs[i] >>> 16) & 0xff) / 255;
					greens[i] = (float)((rgbs[i] >>> 8) & 0xff) / 255;
					blues[i] = (float)(rgbs[i] & 0xff) / 255; 
				}
				
				// Compute Key Mix: O = (A x M) + [(1 - M) x B]
				red = (reds[0] * reds[2]) + ((1.0f-reds[2]) * reds[1]);
				green = (greens[0] * greens[2]) + ((1.0f-greens[2]) * greens[1]);
				blue = (blues[0] * blues[2]) + ((1.0f-blues[2]) * blues[1]);
				
				// Set color of output image
				copy.setRGB(x, y, new Color((int)(red*255), (int)(green*255), (int)(blue*255)).getRGB());
			}
		}
		return copy; 
	}

	public void paint(Graphics g) {
		// Shrink original image to fit in screen
		int w = width / 3; 
		int h = height / 3;

		// Window dimensions
		this.setSize(w * 2 + 75, h * 2 + 150);

		// Draw top row images
		g.drawImage(personImage, 25, 50, w, h, this);
		g.drawImage(backgroundImage, 25 + w + 25, 50, w, h, this);

		// Draw top row labels
		g.setColor(Color.BLACK);
		Font f1 = new Font("Verdana", Font.BOLD, 15); 
		g.setFont(f1);
		g.drawString("Person", 25, 45); 
		g.drawString("Background", 50 + w, 45); 

		// Draw bottom row images
		g.drawImage(matteImage, 25, 100 + h, w, h, this);
		g.drawImage(keyMixedImage, 25 + w + 25, 100 + h, w, h, this);

		// Draw bottom row labels
		g.drawString("Matte", 25, 95 + h); 
		g.drawString("Final Image", 60 + w, 95 + h);  
	}

	public static void main(String[] args) {
		ChromaKeying img = new ChromaKeying();
		img.repaint();
	}
}
