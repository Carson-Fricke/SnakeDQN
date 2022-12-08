package game;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Sprite {
	private static Texture snake;
	
	private static Texture food;
	
	public static class Texture {
		private File file;
		
		private BufferedImage[] images;
		
		public Texture(String path) {
			try {
				this.file = new File(path);
				if (!this.file.exists()) {
					throw new IOException(String.format("Unable to load from file!", path));
				} else {
					BufferedImage sheet = ImageIO.read(this.file);
					
					int rows = sheet.getHeight() / 32;
					int cols = sheet.getWidth() / 32;
					
					int width = 32;
					int height = 32;
					
					BufferedImage[] sprites = new BufferedImage[rows * cols];
					
					for (int i = 0; i < rows; i++) {
					    for (int j = 0; j < cols; j++) {
					        sprites[(i * cols) + j] = sheet.getSubimage(j * width, i * height, width, height);
					    }
					}
					
					this.images = sprites;
				}
			} catch (IOException e) {
				new IOException(String.format("Error loading %s! : %s", path, e.getMessage())).printStackTrace();
			}
		}
		
		public BufferedImage[] getImages() {
			return this.images;
		}
		
		public BufferedImage getImage(int i) {
			return this.images[i];
		}
	}
	
	public static class ColorTexture extends Texture {
		private Map<Integer, BufferedImage[]> colorMap;

		public ColorTexture(String path) {
			super(path);
			this.colorMap = new HashMap<>();
			this.colorMap.put(Color.WHITE.getRGB(), getImages());
		}
		
		public BufferedImage[] getImages(Color c) {
			if (this.colorMap.containsKey(c)) {
				return this.colorMap.get(c);
			} else {
				this.colorMap.put(c.getRGB(), colorImages(c));
				return getImages(c);
			}
		}
		
		public BufferedImage getImage(int i, Color c) {
			return getImages(c)[i];
		}
		
		private BufferedImage[] colorImages(Color color) {
			BufferedImage[] array = new BufferedImage[getImages().length];
			
			for (int i = 0; i < array.length; i++) {
				array[i] = colorImage(i, color);
			}
			
			return array;
		}
		
		private BufferedImage colorImage(int index, Color color) {
			int r = color.getRed();
			int g = color.getGreen();
			int b = color.getBlue();
			int a = color.getAlpha();
			BufferedImage tintedSprite = new BufferedImage(32, 32, BufferedImage.TRANSLUCENT);
		    Graphics2D graphics = tintedSprite.createGraphics();
		    graphics.drawImage(getImage(index), 0, 0, null);
		    graphics.dispose();
		    for (int i = 0; i < tintedSprite.getWidth(); i++) {
  			    for (int j = 0; j < tintedSprite.getHeight(); j++) {
			        int ax = tintedSprite.getColorModel().getAlpha(tintedSprite.getRaster().getDataElements(i, j, null));
			        int rx = tintedSprite.getColorModel().getRed(tintedSprite.getRaster().getDataElements(i, j, null));
			        int gx = tintedSprite.getColorModel().getGreen(tintedSprite.getRaster().getDataElements(i, j, null));
			        int bx = tintedSprite.getColorModel().getBlue(tintedSprite.getRaster().getDataElements(i, j, null));
			        rx *= (r / 255.0);
			        gx *= (g / 255.0);
			        bx *= (b / 255.0);
			        ax *= (a / 255.0);
			        tintedSprite.setRGB(i, j, (ax << 24) | (rx << 16) | (gx << 8) | (bx));
			    }
		    }
			
			return tintedSprite;
		}
	}
}
