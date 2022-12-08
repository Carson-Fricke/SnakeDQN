package game;
import java.util.Deque;
import java.util.LinkedList;

public class AIV2 implements Operator{
	private static Direction[] values = Direction.values();
	
	private Snake snake;
	
	private Board board;
	
	private boolean optimize = true;
	
	private Direction[][] moves;
	
	public boolean[][] area;

	public AIV2() {
		
	}

	@Override
	public Direction getMove() {
		if (this.optimize) {
			optimizePath();
			this.optimize = false;
		}
		return this.moves[this.snake.getX()][this.snake.getY()];
	}
	
	private void createPath() {
		this.moves = new Direction[this.board.width()][this.board.height()];
		this.moves[0][0] = Direction.RIGHT;
		for (int i = 1; i < this.board.height(); i++) {
			this.moves[0][i] = Direction.UP;
		}
		for (int i = 0; i < this.board.height(); i++) {
			for (int j = 1; j < this.board.width(); j++) {
				if (!((j == this.board.width() - 1 && i % 2 == 0) || (j == 1 && i % 2 == 1))) {
					if (i % 2 == 0) {
						this.moves[j][i] = Direction.RIGHT;
					} else {
						this.moves[j][i] = Direction.LEFT;
					}
				} else {
					if (i == this.board.height() - 1) {
						this.moves[j][i] = Direction.LEFT;
					} else {
						this.moves[j][i] = Direction.DOWN;
					}
				}
			}
		}
	}
	
	private void optimizePath() {
		System.out.println("optimize");
		boolean[][] area = new boolean[this.board.width()][this.board.height()];
		int x = this.snake.getX();
		int y = this.snake.getY();
		Tile tail = this.snake.getHead().getTail();
		int tx = tail.getX();
		int ty = tail.getY();
		int dis = 0;
		while (x != tx || y != ty) {
			dis++;
			area[x][y] = true;
			int xtemp = this.moves[x][y].x;
			int ytemp = this.moves[x][y].y;
			x += xtemp;
			y += ytemp;
		}
		int fx = this.snake.getFood().getX();
		int fy = this.snake.getFood().getY();
		int fd = getDistanceToFood(x, y);
		int td = dis;
		LinkedList<Direction> newMoves = new LinkedList<>();
		this.area = area;
		if (optimizePath(this.snake.getX(), this.snake.getY(), fx, fy, fd, tx, ty, td, area, newMoves, 0)) {
			x = this.snake.getX();
			y = this.snake.getY();
			
			for (Direction d : newMoves) {
				this.moves[x][y] = d;
				x += d.x;
				y += d.y;
			}
			System.out.println("true");
		}
	}
	
	private boolean optimizePath(int x, int y, int fx, int fy, int fd, int tx, int ty, int td, boolean[][] area, Deque<Direction> queue, int depth) {
		if (x > 0 && y > 0 && x < area.length && y < area[0].length && area[x][y]) {
			area[x][y] = false;
			depth++;
//			boolean tail = getTail(x, y, area);
//			if (!tail) {
//				area[x][y] = true;
//				System.out.println("    no tail");
//				return false;
//			}
//			int blocks = getBlocks(x, y, area);
//			if (blocks < td - depth) {
//				area[x][y] = true;
//				System.out.println("    not enough blocks " + blocks + " " + (td - depth));
//				return false;
//			}
			if (x == fx && y == fy) {
				if (depth > fd) {
					area[x][y] = true;
					System.out.println("    depth off food " + depth + " " + fd);
					return false;
				}
			} else if (x == tx && y == ty) {
				if (depth == td) {
					System.out.println("    true");
					return true;
				} else {
					area[x][y] = true;
					System.out.println("    depth off tail " + depth + " " + td);
					return false;
				}
			}
			for (Direction d : values) {
				if (optimizePath(x + d.x, y + d.y, fx, fy, fd, tx, ty, td, area, queue, depth)) {
					queue.addFirst(d);
					return true;
				}
			}
			area[x][y] = true;
		}
		return false;
	}
	
	private int getBlocks(int x, int y, boolean[][] area) {
		boolean[][] array = new boolean[area.length][area[0].length];
		int b = getBlocks(x + 1, y, area, array);
		if (b == 0) {
			b = getBlocks(x - 1, y, area, array);
			if (b == 0) {
				b = getBlocks(x, y + 1, area, array);
				if (b == 0) {
					b = getBlocks(x, y - 1, area, array);
				}
			}
		}
//		this.area = array;
		
		return b;
	}
	
	private int getBlocks(int x, int y, boolean[][] area, boolean[][] marked) {
		if (x >= 0 && y >= 0 && x < area.length && y < area[0].length && !marked[x][y] && area[x][y]) {
			marked[x][y] = true;
			int blocks = 1;
			blocks += getBlocks(x + 1, y, area, marked);
			blocks += getBlocks(x - 1, y, area, marked);
			blocks += getBlocks(x, y - 1, area, marked);
			blocks += getBlocks(x, y + 1, area, marked);
			return blocks;
		}
		return 0;
	}
	
	private boolean getTail(int x, int y, boolean[][] area) {
		boolean[][] array = new boolean[area.length][area[0].length];
		Tile t = this.snake.getHead().getTail();
		if (t == null) return false;
		int tx = t.getX();
		int ty = t.getY();
		boolean b = getTail(x + 1, y, area, array, tx, ty);
		if (!b) {
			b = getTail(x - 1, y, area, array, tx, ty);
			if (!b) {
				b = getTail(x, y + 1, area, array, tx, ty);
				if (!b) {
					b = getTail(x, y - 1, area, array, tx, ty);
				}
			}
		}
		return b;
	}
	
	private boolean getTail(int x, int y, boolean[][] area, boolean[][] marked, int tx, int ty) {
		if (x == tx && y == ty) return true;
		if (x >= 0 && y >= 0 && x < area.length && y < area[0].length && !marked[x][y] && area[x][y]) {
			marked[x][y] = true;
			boolean blocks = false;
			if (getTail(x + 1, y, area, marked, tx, ty)) {
				blocks = true;
			} else if (getTail(x - 1, y, area, marked, tx, ty)) {
				blocks = true;
			} else if (getTail(x, y - 1, area, marked, tx, ty)) {
				blocks = true;
			} else if (getTail(x, y + 1, area, marked, tx, ty)) {
				blocks = true;
			}
			return blocks;
		}
		return false;
	}
	
	private int getDistanceToFood(int x, int y) {
		return getDistanceToFood(x, y, this.board.width() * this.board.height());
	}
	
	private int getDistanceToFood(int x, int y, int cap) {
		if (cap > 0 && (x != this.snake.getFood().getX() || y != this.snake.getFood().getY())) {
			return 1 + getDistanceToFood(x + this.moves[x][y].x, y + this.moves[x][y].y, cap - 1);
		} else {
			return 0;
		}
	}
	
	@Override
	public Snake getSnake() {
		return this.snake;
	}

	@Override
	public Board getBoard() {
		return this.board;
	}

	@Override
	public void linkSnake(Snake s) {
		this.snake = s;
	}

	@Override
	public void linkBoard(Board b) {
		this.board = b;
		createPath();
	}
	
	@Override
	public void onFood() {
		this.optimize = true;
	}
}
