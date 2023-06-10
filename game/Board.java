package game;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import snakedqn.DQNAgent;

@SuppressWarnings("serial")
public class Board extends JPanel implements Runnable {
	private static final Setting DEFAULT_SETTING = new Setting();
	
	private Tile[][] tiles;
	
	private List<Creature> creatures;
	
	private Setting setting;
	
	private Thread thread;
	
	private boolean running;
	
	public Board() {
		this(Board.DEFAULT_SETTING);
	}
	
	public Board(Setting setting) {
		this.setting = setting;
		this.tiles = this.setting.getArray();
		init();
	}
	
	public void spawnFood(Snake s) {
		int x = (int) (Math.random() * this.tiles.length);
		int y = (int) (Math.random() * this.tiles[0].length);
		while (this.tiles[x][y] != null) {
			x = (int) (Math.random() * this.tiles.length);
			y = (int) (Math.random() * this.tiles[0].length);
		}
		set(x, y, new Food(s, x, y));
	}
	
	public void init() {
		setPreferredSize(new Dimension(1000, 500));
		setFocusable(true);
		this.creatures = new ArrayList<>();
		this.tiles = new Tile[this.setting.getWidth()][this.setting.getHeight()];
		List<Operator> operators = this.setting.getOperators();
		int len = operators.size();
		int space = this.tiles.length / len;
		int pos = space / 2;
		
		for (Operator o : operators) {
			o.linkBoard(this);
			this.creatures.add(new Snake(o, pos, this.tiles[0].length - 1, this.setting.getLength(), Direction.UP));
			pos += space;
		}
		
		for (Creature a : this.creatures) {
			if (a instanceof Snake) {
				spawnFood((Snake) a);
			}
		}
	}
	
	public Setting getSetting() {
		return this.setting;
	}
	
	public List<Creature> getCreatures() {
		return this.creatures;
	}
	
	public void start() {
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	@Override
	public void run() {
		this.running = true;
		long frameTime = 0;
		long updateTime = 0;
		long frameMillis = 1000 / this.setting.fps;
		long updateMillis = (long) (1000 / this.setting.ups);
		while (this.running) {

			if (System.currentTimeMillis() - updateMillis >= updateTime) {
				time_step();
				updateTime = System.currentTimeMillis();
			}


			if (System.currentTimeMillis() - frameMillis >= frameTime) {
				repaint();
				frameTime = System.currentTimeMillis();
			}
			
			
			long frameWait = frameMillis - (System.currentTimeMillis() - frameTime);
			long updateWait = updateMillis - (System.currentTimeMillis() - updateTime);
			long wait = Math.max(0, Math.min(frameWait, updateWait));
			
			try {
				Thread.sleep(wait);
			} catch (InterruptedException e) {
			}

			this.running = false;

			for (Creature a : this.creatures) {
				if (a instanceof Snake) {
					this.running = this.running || ((Snake) a).isAlive();
				}
			}

		}
	}
	
	private void time_step() {
		for (Creature a : this.creatures) {
			a.update();
		}
		Collections.shuffle(this.creatures);
		for (Creature a : this.creatures) {
			a.move();
		}

		DQNAgent.populateReward();
		DQNAgent.populateNextState();
		DQNAgent.addTransition();

	}
	
	public Tile get(int x, int y) {
		return this.tiles[x][y];
	}

	public Tile[][] getTiles() 
	{
		return this.tiles;
	}
	
	public void set(int x, int y, Tile t) {
		this.tiles[x][y] = t;
	}
	
	public int width() {
		return this.tiles.length;
	}
	
	public int height() {
		return this.tiles[0].length;
	}
	
	public boolean isRunning() {
		return this.running;
	}

	public void setUps(int arg) {
		this.setting.ups = arg;
	}

	public boolean validPosition(int x, int y) {
		return x >= 0 && x < this.tiles.length && y >= 0 && y < this.tiles[0].length;
	}
	
	public boolean isClearPosition(int x, int y) {
		return validPosition(x, y) && (this.tiles[x][y] == null || !this.tiles[x][y].isCollidable());
	}
	
	public boolean isEdgePosition(int x, int y) {
		return x == 0 || y == 0 || x == width() - 1 || y == height() - 1;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		int width = getHeight() / this.tiles[0].length;
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[0].length; j++) {
				/*if (this.tiles[i][j] != null) {
					this.tiles[i][j].draw(g, i * width, j * width, width, width);
				} else {
					Tile.drawDefault(g, i * width, j * width, width, width);
				}*/

				try 
				{
					this.tiles[i][j].draw(g, i * width, j * width, width, width);

				}
				catch (java.lang.NullPointerException e) 
				{
					Tile.drawDefault(g, i * width, j * width, width, width);
				}

			}
		}
		/*
		for (Creature c : this.creatures) {
			if (c instanceof Snake && ((Snake) c).getOperator() instanceof AIV2) {
				
				AIV2 ai = (AIV2) ((Snake) c).getOperator();
				Color aic = ai.getSnake().getColor();
				Color color = new Color(aic.getRed(), aic.getBlue(), aic.getGreen(), 180);
				if (ai.area != null) {
					for (int i = 0; i < ai.area.length; i++) {
						for (int j = 0; j < ai.area[0].length; j++) {
							if (ai.area[i][j]) {
								Tile.drawScan(g, i * width, j * width, width, width, color);
							}
						}
					}
				}
			} 
			
		}*/
	}
}
