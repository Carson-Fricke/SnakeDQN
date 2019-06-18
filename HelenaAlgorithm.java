import java.awt.Point;

import com.caibear.snake.Board;
import com.caibear.snake.Direction;
import com.caibear.snake.Food;
import com.caibear.snake.Operator;
import com.caibear.snake.Snake;

public class HelenaAlgorithm implements Operator{
	// Helena's Algorithm
	// Not as good as the others, since I spent most of my time on the graphics
	// (and procrastinated)
	// I have three factors to choose a direction: 
	//   1) don't immediately die (check for available space directly ahead)
	//   2) get food (minimize distance to it)
	//	 3) kill other snakes (go to spot in front of their heads)
	//   4) maximize available space (using a flood fill)
	
	private Snake snake;
	private Board board;
	
	public HelenaAlgorithm(Snake s, Board b) {
		linkSnake(s);
		linkBoard(b);
	}
	
	public HelenaAlgorithm() {
		this.snake = null;
		this.board = null;
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
	public Direction getMove() {
		float lw = 0; //the directions
		float rw = 0;
		float uw = 0;
		float dw = 0;
		float deathWeight = 1000; //adjust importance of various things
		float foodWeight = 10;
		float killWeight = 5;
		float spaceWeight = 10;
		int myFoodBonus = 5;
		Point myHead = new Point();
		Point myFood = new Point();
		boolean myFoodFound = false;
		Point[] otherHead = new Point[this.board.getCreatures().size() - 1];
		//array of space in front of other snakes' heads for kill weight
		int otherHeadFound = 0;
		Point[] otherFood = new Point[this.board.getCreatures().size() - 1];
		int otherFoodFound = 0;
		
		for(int x = 0; x < board.getWidth(); x++) {
			for(int y = 0; y < board.getHeight(); y++) { 
				if(board.get(x, y) != null) { //if it's a thing
					if (board.get(x, y) instanceof Snake.Segment) { //if it's a snake segment
						if(((Snake.Segment)board.get(x, y)).getParent() == null) { //if it's a head
							if (((Snake.Segment)board.get(x, y)) == this.snake.getHead()) { //if it's MY head
								myHead = new Point(x, y);
							} else { //not my head
								if(otherHeadFound < otherHead.length) {
									otherHead[otherHeadFound] = new Point(((Snake.Segment)board.get(x, y)).getSnake().getDirection().x, ((Snake.Segment)board.get(x, y)).getSnake().getDirection().y);
									//adds space in front of other snake's head to list
								}
								otherHeadFound++;
							}
						}
					} else if (board.get(x, y) instanceof Food) { //food
						if(((Food)board.get(x, y)).getOwner() == this.snake) { //if it's my food
							myFood = new Point(x, y);
							myFoodFound = true;
						} else { //not my food
							if(otherFoodFound < otherFood.length) {
								otherFood[otherFoodFound] = new Point(x, y);
							}
							otherFoodFound++;
						}
					}
				}
			}
		}
		
		//don't die
		if(!board.isClearPosition((int)myHead.getX()+1,(int)myHead.getY())) {
			rw = -1 * deathWeight;
		} else if(!board.isClearPosition((int)myHead.getX()-1,(int)myHead.getY())) {
			lw = -1 * deathWeight;
		} else if(!board.isClearPosition((int)myHead.getX(),(int)myHead.getY()-1)) {
			uw = -1 * deathWeight;
		} else if(!board.isClearPosition((int)myHead.getX(),(int)myHead.getY()+1)) {
			dw = -1 * deathWeight;
		}
		
		//get closer to my food
		if(myFoodFound) {
			if (myFood.getX() > myHead.getX()) {
				rw += myFoodBonus*foodWeight;
			} else if (myFood.getX() < myHead.getX()) {
				lw += myFoodBonus*foodWeight;
			}
			if (myFood.getY() > myHead.getY()) {
				dw += myFoodBonus*foodWeight;
			} else if (myFood.getY() < myHead.getY()) {
				uw += myFoodBonus*foodWeight;
			}
		}
		
		//get closer to other food
		for(int i = 0; i < otherFoodFound; i++) {
			if (otherFood[i].getX() > myHead.getX()) {
				rw += foodWeight;
			} else if (otherFood[i].getX() < myHead.getX()) {
				lw += foodWeight;
			}
			if (otherFood[i].getY() > myHead.getY()) {
				dw += foodWeight;
			} else if (otherFood[i].getY() < myHead.getY()) {
				uw += foodWeight;
			}
		}
		
		//get closer to the space in front of other heads
		for(int i = 0; i < otherHeadFound; i++) {
			if (otherHead[i].getX() > myHead.getX()) {
				rw += killWeight;
			} else if (otherHead[i].getX() < myHead.getX()) {
				lw += killWeight;
			}
			if (otherHead[i].getY() > myHead.getY()) {
				dw += killWeight;
			} else if (otherHead[i].getY() < myHead.getY()) {
				uw += killWeight;
			}
		}
		
		rw += spaceWeight * (clearSpace(new Point((int)myHead.getX()+1,(int)myHead.getY()), new boolean[board.width()][board.height()]));
		lw += spaceWeight * (clearSpace(new Point((int)myHead.getX()-1,(int)myHead.getY()), new boolean[board.width()][board.height()]));
		uw += spaceWeight * (clearSpace(new Point((int)myHead.getX(),(int)myHead.getY()-1), new boolean[board.width()][board.height()]));
		dw += spaceWeight * (clearSpace(new Point((int)myHead.getX(),(int)myHead.getY()+1), new boolean[board.width()][board.height()]));
		
		if(rw >= lw && rw >= dw && rw >= uw) {
			return Direction.RIGHT;
		} else if (lw >= dw && lw >= uw) {
			return Direction.LEFT;
		} else if (dw >= uw) {
			return Direction.DOWN;
		} else {
			return Direction.UP;
		}
	}

	private int clearSpace(Point startPoint, boolean[][] bcBoard) {
		if(!board.isClearPosition((int)startPoint.getX(), (int)startPoint.getY())||bcBoard[(int)startPoint.getX()][(int)startPoint.getY()]) {
			//not clear position or have already gone on
			//won't have error w/ bcBoard because it will short-circuit if you go off the edge
			return 0;
		}
		bcBoard[(int)startPoint.getX()][(int)startPoint.getY()] = true; //counts curr square
		return 1+clearSpace(new Point((int)startPoint.getX()+1, (int)startPoint.getY()), bcBoard)
				+clearSpace(new Point((int)startPoint.getX()-1, (int)startPoint.getY()), bcBoard)
				+clearSpace(new Point((int)startPoint.getX(), (int)startPoint.getY()-1), bcBoard)
				+clearSpace(new Point((int)startPoint.getX(), (int)startPoint.getY()+1), bcBoard);
	}
	
	@Override
	public void linkSnake(Snake s) {
		this.snake = s;
	}

	@Override
	public void linkBoard(Board b) {
		this.board = b;
	}

}
