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
	BufferedImage personImage; 
	BufferedImage backgroundImage;
	BufferedImage matteImage; 
	BufferedImage keyMixedImage;

	int width; // width of the image
	int height; // height of the image

	public ChromaKeying() {
		// Get an image from the specified file in the current directory
		try {
			personImage = ImageIO.read(new File("OriginalCropped.jpg"));
			backgroundImage = ImageIO.read(new File("Background.jpg"));
		} catch (Exception e) {
			System.out.println("Cannot load the provided image");
		}
		
		this.setTitle("Assignment 1 - Chroma Keying");
		this.setVisible(true);

		width = personImage.getWidth();
		height = personImage.getHeight();

		matteImage = ChromaKeyMatte(personImage);
		keyMixedImage = KeyMix(personImage, backgroundImage, keyMixedImage);

		//Anonymous inner-class listener to terminate program
		this.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);//terminate the program
			}
		});
	}
	
	public BufferedImage ChromaKeyMatte(BufferedImage img) {

		int width = img.getWidth(); 
		int height = img.getHeight(); 

		WritableRaster wRaster = img.copyData(null);
		BufferedImage copy = new BufferedImage(img.getColorModel(), wRaster, img.isAlphaPremultiplied(), null);

		//apply the operation to each pixel
		for (int x = 0; x < width; x++) { 
			for (int y = 0; y < height; y++) {
//				int rgb = img.getRGB(x, y);
//				copy.setRGB(x, y, rbg);
			}
		}
		return copy; 
	}
	
	public BufferedImage KeyMix(BufferedImage fimg, BufferedImage bimg, BufferedImage matte) {

		int width = fimg.getWidth(); 
		int height = fimg.getHeight(); 

		WritableRaster wRaster = fimg.copyData(null);
		BufferedImage copy = new BufferedImage(fimg.getColorModel(), wRaster, fimg.isAlphaPremultiplied(), null);

		//apply the operation to each pixel
		for (int x = 0; x < width; x++) { 
			for (int y = 0; y < height; y++) {
				
//				red = (reds[0] * reds[2]) + ((1.0f-reds[2]) * reds[1]);
//				green = (greens[0] * greens[2]) + ((1.0f-greens[2]) * greens[1]);
//				blue = (blues[0] * blues[2]) + ((1.0f-blues[2]) * blues[1]);
//				
//				return new Color((int)(red*255), (int)(green*255), (int)(blue*255)).getRGB();
//				
//				int rgb = img.getRGB(x, y);
//				copy.setRGB(x, y, rbg);
			}
		}
		return copy; 
	}

	public void paint(Graphics g) {
		// if working with different images, this may need to be adjusted
		int w = width / 3; 
		int h = height / 3;

		this.setSize(w * 2 + 75, h * 2 + 150);

		g.drawImage(personImage, 25, 50, w, h, this);
		g.drawImage(backgroundImage, 25 + w + 25, 50, w, h, this);

		g.setColor(Color.BLACK);
		Font f1 = new Font("Verdana", Font.BOLD, 15); 
		g.setFont(f1);
		g.drawString("Person", 25, 45); 
		g.drawString("Background", 50 + w, 45); 

		g.drawImage(matteImage, 25, 100 + h, w, h, this);
		g.drawImage(keyMixedImage, 25 + w + 25, 100 + h, w, h, this);

		g.drawString("Matte", 25, 95 + h); 
		g.drawString("Final Image", 60 + w, 95 + h);  
	}

	public static void main(String[] args) {
		ChromaKeying img = new ChromaKeying();// instantiate this object
		img.repaint(); // render the image
	} // end main
}
