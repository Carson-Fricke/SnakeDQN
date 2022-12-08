package game;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Player implements Operator, KeyListener {
	private int id;
	
	private HashMap<Integer, Direction> keyMap;
	
	private Queue<Direction> moveQueue;
	
	private Snake snake;
	
	private Board board;
	
	public Player(int id) {
		this.id = id;
		this.keyMap = ControlKeys.getKeyMap(ControlKeys.getPlayerKeys(this.id));
		this.moveQueue = new LinkedList<>();
	}

	@Override
	public void linkSnake(Snake snake) {
		this.snake = snake;
		switch (this.id) {
		case 1:
			this.snake.setColor(Color.RED);
			break;
		case 2:
			this.snake.setColor(Color.BLUE);
			break;
		case 3:
			this.snake.setColor(Color.YELLOW);
			break;
		case 4:
			this.snake.setColor(Color.GREEN.darker().darker());
			break;
		}
	}
	
	@Override
	public void linkBoard(Board board) {
		this.board = board;
		this.board.addKeyListener(this);
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
		return this.moveQueue.poll();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int key = arg0.getKeyCode();
		if (this.keyMap.containsKey(key) && this.moveQueue.size() < 3) {
			this.moveQueue.add(this.keyMap.get(key));
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	enum ControlKeys {
		ARROWS,
		WASD,
		TFGH,
		IJKL;
		
		public static ControlKeys getPlayerKeys(int i) {
			switch (i) {
			    case 1:
			    	return ARROWS;
			    case 2:
			    	return WASD;
			    case 3:
			    	return TFGH;
			    case 4: 
			    	return IJKL;
			    default: return null;
			}
		}

		public static HashMap<Integer, Direction> getKeyMap(ControlKeys keys) {
			switch (keys) {
			    case ARROWS:
			    	HashMap<Integer, Direction> keyMap = new HashMap<Integer, Direction>();
			    	keyMap.put(37, Direction.LEFT);
			    	keyMap.put(38, Direction.UP);
			    	keyMap.put(39, Direction.RIGHT);
			    	keyMap.put(40, Direction.DOWN);
			    	return keyMap;
			    case WASD:
					HashMap<Integer, Direction> keyMap1 = new HashMap<Integer, Direction>();
			    	keyMap1.put(65, Direction.LEFT);
			    	keyMap1.put(87, Direction.UP);
			    	keyMap1.put(68, Direction.RIGHT);
			    	keyMap1.put(83, Direction.DOWN);
			    	return keyMap1;
			    case TFGH:
			    	HashMap<Integer, Direction> keyMap2 = new HashMap<Integer, Direction>();
			    	keyMap2.put(70, Direction.LEFT);
			    	keyMap2.put(84, Direction.UP);
			    	keyMap2.put(72, Direction.RIGHT);
			    	keyMap2.put(71, Direction.DOWN);
			    	return keyMap2;
				case IJKL:
					HashMap<Integer, Direction> keyMap3 = new HashMap<Integer, Direction>();
			    	keyMap3.put(74, Direction.LEFT);
			    	keyMap3.put(73, Direction.UP);
			    	keyMap3.put(76, Direction.RIGHT);
			    	keyMap3.put(75, Direction.DOWN);
			    	return keyMap3;
				default:
					return null;
			}
		}
	}
}
