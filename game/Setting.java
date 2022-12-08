package game;
import java.util.ArrayList;
import java.util.List;

public class Setting {
	public int fps;
	
	public double ups;
	
	private List<Operator> operators = new ArrayList<>();
	
	private int width = (int) (8);
	
	private int height = (int) (8);
	
	private int length = 20;
	
	private int lengthPerFood = 3;
	
	public Setting() {
		this(60, 10000D);
	}
	
	public Setting(int fps, double ups) {
		this.fps = fps;
		this.ups = ups;
	}
	
	public void addOperator(Operator o) {
		this.operators.add(o);
	}
	
	public void removeOperator(Operator o) {
		this.operators.remove(o);
	}
	
	public List<Operator> getOperators() {
		return this.operators;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public void setLengthPerFood(int length) {
		this.lengthPerFood = length;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public int getLengthPerFood() {
		return this.lengthPerFood;
	}
	
	public Tile[][] getArray() {
		return new Tile[this.width][this.height];
	}
}
