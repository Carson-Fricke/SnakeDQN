package game;

public interface Operator {
	Snake getSnake();
	
	Board getBoard();
	
	Direction getMove();
	
	void linkSnake(Snake s);
	
	void linkBoard(Board b);
	
	default void onDeath() {
		
	}
	
	default void onFood() {
		
	}
}
