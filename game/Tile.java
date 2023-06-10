package game;
import java.awt.Color;
import java.awt.Graphics;

public interface Tile {
	int getX();
	
	int getY();
	
	boolean isCollidable();
	
	void draw(Graphics g, int x, int y, int width, int height);
	
	static void drawDefault(Graphics g, int x, int y, int width, int height) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
	}
	
	static void drawScan(Graphics g, int x, int y, int width, int height, Color c) {
		g.setColor(c);
		g.fillRect(x, y, width, height);
	}
}
