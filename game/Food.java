package game;
import java.awt.Color;
import java.awt.Graphics;

public class Food implements Tile {
	
	private Snake owner;
	
	private int x, y;
	
	public Food(Snake owner, int x, int y) {
		this.owner = owner;
		this.x = x;
		this.y = y;
		this.owner.setFood(this);
	}
	
	public Snake getOwner() {
		return this.owner;
	}
	
	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public boolean isCollidable() {
		return false;
	}

	@Override
	public void draw(Graphics g, int x, int y, int width, int height) {
		g.setColor(this.owner.getColor().brighter());
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
	}
	
}
