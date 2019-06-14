public class DYNAI implements Operator {

	private Snake snake;
	
	private Board board;
	
	private Direction[][] path;
	
	private final Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
	
	public DYNAI() {}

	@Override
	public Direction getMove() {
		
		return calcOptimalDirection();	
	}
	
	private Direction calcOptimalDirection() {			
		
		int snakeX = this.snake.getHead().getX();
		int snakeY = this.snake.getHead().getY();
		int totalSpace = this.board.width() * this.board.height();
		
		float[][] rewards = calcRewards(new float[this.board.width()][this.board.height()], snakeX, snakeY, 0, 0, 0.998F, totalSpace);	
		
		updateFoodStates(rewards, snakeX, snakeY, this.snake.getFood().getX(), this.snake.getFood().getY(), totalSpace);
				
		avoidInvalidTiles(rewards);
		
		float max = 0F;
		
		Direction optimalDirection = null;
		
		for(Direction d : this.directions) {
		
			if(isValid(snakeX, snakeY, d)) {
				
				Tile currTile = this.board.get(snakeX + d.x, snakeY + d.y);
				
				if(currTile == null || currTile instanceof Food) {
				
					float f = rewards[snakeX + d.x][snakeY + d.y];
					
					if(f > max) {
					
						optimalDirection = d;
						
						max = f;
					}
				}
			}		
		}
		return optimalDirection;
	}
	
	private float[][] calcRewards(float[][] rewards, int x, int y, int x1, int y1, float gamma, int max) {
		
		if(max == 0) return rewards;
		
		if(max == rewards.length * rewards[0].length) {
		
			rewards[x][y] = .975F;
		
		} else {
		
			rewards[x][y] = rewards[x1][y1] * gamma;
		}
		
		calcRewards(rewards, x + path[x][y].x, y + path[x][y].y, x, y, gamma, max-1);
		
		return rewards;
	}

	private void updateFoodStates(float[][] rewards, int x, int y, int foodX, int foodY, int count) {
		
		if(this.snake.getLength() >= (board.width() * board.height() * 0.925) 
				|| this.snake.getHead().getX() >= foodX || x == foodX && y == foodY || count == 0) return;
		
		updateFoodStates(rewards, x + path[x][y].x, y + path[x][y].y, foodX, foodY, count-1);
		
		rewards[x][y] += (board.width() - (distanceToFood(x, y, foodX, foodY))) * (1/(this.snake.getLength() * 0.875)) * (this.board.width() * this.board.height() / 100);
	}
	
	private void avoidInvalidTiles(float[][] rewards) {
		
		for(Direction d : this.directions) {
			
			int snakeX = this.snake.getHead().getX();
			int snakeY = this.snake.getHead().getY();
			
			if(isValid(snakeX, snakeY, d)) {
				
				Tile currTile = this.board.get(snakeX + d.x, snakeY + d.y);
				
				if(currTile != null && !(currTile instanceof Food)) {
					
					rewards[this.snake.getHead().getX() + d.x][this.snake.getHead().getY() + d.y] = 0;
				}
			}
		}
	}
	
	private void createPath(int width, int height) {
		
		this.path = new Direction[width][height];
		
		for(int i = 0; i < this.path.length; i++) {
			
			for(int j = 0; j < this.path[0].length; j++) {
				
				if(i % 2 != 0) {
					
					if (j < this.path[0].length - 2 || i == this.path.length - 1) {
						
						this.path[i][j] = Direction.DOWN;	
						
					} else {
						
						this.path[i][j] = Direction.RIGHT; 
					}
					
				} else {
					
					if(j > 0) {
						
						this.path[i][j] = Direction.UP;
						
					}else {
						
						this.path[i][j] = Direction.RIGHT;
					}
				}
				if(j == this.path[0].length-1 && i > 0) this.path[i][j] = Direction.LEFT;
			}
		}	
	}
	
	private boolean isValid(int x, int y, Direction d) {
		return x + d.x < this.path.length && y + d.y < this.path[0].length && x + d.x >= 0 && y + d.y >= 0;
	}
	
	private double distanceToFood(int x, int y, int x1, int y1) {
		return Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
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
		createPath(board.width(), board.height());
	}
}