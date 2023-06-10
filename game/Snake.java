package game;
import java.awt.Color;
import java.awt.Graphics;

public class Snake implements Creature {
	private Segment head;
	
	private Operator operator;
	
	private Food food;
	
	private Board board;
	
	private Direction direction;
	
	private Direction oldDir;
	
	private Color color = Color.RED;
	
	private int length;
	
	private boolean alive;
	
	private int eat;
		
	public Snake(Operator operator, int x, int y, int length, Direction dir) {
		this.length = length;
		this.operator = operator;
		this.board = operator.getBoard();
		this.operator.linkSnake(this);
		this.alive = true;
		this.eat = length - 1;
		this.head = new Snake.Segment(x, y, dir, 1);
		this.direction = dir;
		this.oldDir = dir;
	}
	
	public void update() {
		if (this.alive) {
			Direction dir = this.operator.getMove();
			this.direction = dir == null ? this.direction : dir;
		}
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public Food getFood() {
		return this.food;
	}
	
	public void setFood(Food f) {
		this.food = f;
	}
	
	public Direction getDirection() {
		return this.direction;
	}
	
	public Operator getOperator() {
		return this.operator;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public Segment getHead() {
		return this.head;
	}
	
	public int getX() {
		return this.head.x;
	}

	public boolean isAlive() {
		return this.alive;
	}
	
	public int getY() {
		return this.head.y;
	}
	
	public void move() {
		if (this.alive) {
			boolean turnKill = !this.oldDir.equals(this.direction);
			
			int x = this.head.x + this.direction.x;
			int y = this.head.y + this.direction.y;
			
			if (!this.board.validPosition(x, y)) {
				if (turnKill) {
					this.direction = this.oldDir;
					move();
					return;
				}
				this.alive = false;
				this.operator.onDeath();
				return;
			}
			
			Tile t = this.board.get(x, y);
			
			if (t != null && t.isCollidable()) {
				if (turnKill) {
					this.direction = this.oldDir;
					move();
					return;
				}
				this.alive = false;
				this.operator.onDeath();
				return;
			} else {
				this.oldDir = this.direction;
				
				if (t instanceof Food) {
					if (((Food) t).getOwner() == this) {
						this.eat += this.board.getSetting().getLengthPerFood();
					} else {
						this.eat += this.board.getSetting().getLengthPerFood() / 3;
					}
					
					this.board.spawnFood(((Food) t).getOwner());
					this.operator.onFood();
				}
				
				int e = this.eat;
				
				if (e > 0) {
					Segment s = this.head.getTail();
					
					x = s.x;
					y = s.y;
					Direction d = s.direction;
					
					this.head.move(this.direction);
					
					eat--;
					length++;
					
					s.child = new Segment(s, x, y, d, 1);
				} else {
					this.head.move(this.direction);
				}
			}
		}
	}
	
	public class Segment implements Tile {
		private Segment child;
		
		private Segment parent;
		
		private Direction direction;
		
		private int x, y;
		
		// Head Constructor
		public Segment(int x, int y, Direction d, int length) {
			this(null, x, y, d, length);
		}
		
		public Segment getTail() {
			if (this.child == null) {
				return this;
			} else {
				return this.child.getTail();
			}
		}
		
		//Body Constructor
		public Segment(Segment parent, int x, int y, Direction d, int length) {
			this.parent = parent;
			this.direction = d;
			this.x = x;
			this.y = y;
			Snake.this.board.set(x, y, this);
			if (length > 1) {
				this.child = new Segment(this, x - d.x, y - d.y, d, length - 1);
			}
		}
		
		public void move(Direction d) {
			assert d != null;
			Direction temp = null;
			if (this.child != null) {
				temp = this.direction;
			}
			this.direction = d;
			if (Snake.this.board.get(x, y) == this)
				Snake.this.board.set(this.x, this.y, null);
			this.x += d.x;
			this.y += d.y;
			Snake.this.board.set(this.x, this.y, this);
			if (this.child != null) {
				this.child.move(temp);
			}
		}
		
		public Segment getChild() {
			return this.child;
		}
		
		public Segment getParent() {
			return this.parent;
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
			return true;
		}
		
		public Snake getSnake() {
			return Snake.this;
		}

		@Override
		public void draw(Graphics g, int x, int y, int width, int height) {
			if (this.parent == null) {
				g.setColor(Snake.this.color.darker());
			} else {
				g.setColor(Snake.this.color);
			}
			g.fillRect(x, y, width, height);
			g.setColor(Color.BLACK);
			g.drawRect(x, y, width, height);
		}
	}
}