package game;

public enum Direction {
	RIGHT(1, 0),
	UP(0, -1),
	LEFT(-1, 0),
	DOWN(0, 1);
	
	
	public final int x, y;
	
	private Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Direction getRight() {
		switch (this) {
		case UP:
			return RIGHT;
		case RIGHT:
			return DOWN;
		case DOWN:
			return LEFT;
		case LEFT:
			return UP;
		}
		return null;
	}
	
	public Direction getLeft() {
		switch (this) {
		case UP:
			return LEFT;
		case LEFT:
			return DOWN;
		case DOWN:
			return RIGHT;
		case RIGHT:
			return UP;
		}
		return null;
	}
}
