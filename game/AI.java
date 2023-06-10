package game;

public class AI implements Operator {
	private Snake snake;
	
	public byte[][] array;
	
	public int blocks;
	
	private Board board;
	
	private double foodWeight = 5.0D;

	private double killWeight = 2.5D;
	
	private double deathWeight = -100.0D;

	public AI() {
		
	}

	@Override
	public Direction getMove() {
		Direction[] directions = Direction.values();
		double[] weights = new double[directions.length];
		
		calcWeights(directions, weights);
		
		int max = 0;
		for (int i = 0; i < weights.length; i++) {
			if (weights[i] > weights[max]) {
				max = i;
			}
		}
		return directions[max];
	}
	
	private void calcWeights(Direction[] directions, double[] weights) {
		this.blocks = 0;
		double blockWeight = this.snake.getLength() * 100000 / (this.board.height() * this.board.width());
		for (int i = 0; i < directions.length; i++) {
			Direction d = directions[i];
			double weight = 0.0;
			int x = this.snake.getX() + d.x;
			int y = this.snake.getY() + d.y;
			
			if (!this.board.isClearPosition(x, y)) {
				weight += this.deathWeight;
				weights[i] = weight;
				continue;
			}
			
			int blocks = getBlocks(x, y);
			weight += blocks * blockWeight;
			
			int food = distanceToFood(x, y);
			weight += Math.max(0.0D, foodWeight * (1.0D / Math.max(0.5D, food)));
			
			for (Creature c : this.board.getCreatures()) {
				if (c != this.snake && c instanceof Snake) {
					int f = distanceToFood(x, y, (Snake) c);
					weight += Math.max(0.0D, foodWeight / 3 * (1.0D / Math.max(0.5D, f)));
				}
			}
			
			int snake = distanceToSnake(x, y);
			weight += Math.max(0.0D, killWeight * (1.0D / Math.max(0.5D, snake)));
		
			weights[i] = weight;
		}
	}
	
	private int getBlocks(int x, int y) {
		byte[][] array = new byte[this.board.width()][this.board.height()];
		array[x][y] = 1;
		int a = getBlocks(x + 1, y, array, 0);
		int b = getBlocks(x - 1, y, array, 0);
		int c = getBlocks(x, y + 1, array, 0);
		int d = getBlocks(x, y - 1, array, 0);
		int blocks = Math.max(Math.max(a, b), Math.max(c, d));
		if (array[this.snake.getHead().getTail().getX()][this.snake.getHead().getTail().getY()] == 0) {
			blocks = (int) (blocks * (1 - (this.snake.getLength() * 1 / (this.board.width() * this.board.height()))));
		}
		if (blocks > this.blocks) {
			this.array = array;
		}
		return blocks;
	}
	
	private int getBlocks(int x, int y, byte[][] marked, int depth) {
		if (board.isClearPosition(x, y) && marked[x][y] == 0) {
			marked[x][y] = 1;
			int blocks = 1;
			blocks += getBlocks(x + 1, y, marked, depth + 1);
			blocks += getBlocks(x - 1, y, marked, depth + 1);
			blocks += getBlocks(x, y + 1, marked, depth + 1);
			blocks += getBlocks(x, y - 1, marked, depth + 1);
			int adjacent = 0;
			if (!this.board.isClearPosition(x + 1, y) || marked[x + 1][y] > 1) adjacent++;
			if (!this.board.isClearPosition(x - 1, y) || marked[x - 1][y] > 1) adjacent++;
			if (!this.board.isClearPosition(x, y + 1) || marked[x][y + 1] > 1) adjacent++;
			if (!this.board.isClearPosition(x, y - 1) || marked[x][y - 1] > 1) adjacent++;
			
			if (adjacent > 2) {
				marked[x][y]++;
				return 0;
			}
			
			return blocks;
		}
		return 0;
	}
	
	private int distanceToFood(int x, int y) {
		return distanceToFood(x, y, this.snake);
	}
	
	private int distanceToFood(int x, int y, Snake s) {
		Food f = s.getFood();
		if (f != null) {
			return Math.abs(f.getX() - x) + Math.abs(f.getY() - y);
		} else {
			return -1;
		}
	}
	
	private int distanceToSnake(int x, int y) {
		int range = 10;
		int min = range;
		for (Creature c : this.board.getCreatures()) {
			if (c instanceof Snake && c != this.snake) {
				Snake s = (Snake) c;
				int distance = Math.abs(s.getX() + s.getDirection().x * 2 - x) + Math.abs(s.getY() + s.getDirection().y * 2 - y);
				if (distance < min) {
					min = distance;
				}
			}
		}
		return min == range ? -1 : min;
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
	}
	
	@Override
	public void onDeath() {
		
	}
}
