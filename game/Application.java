package game;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;


public class Application {
	private JFrame frame;
	
	private Board board;
	
	public Application(Setting s) {
		this.board = new Board(s);
		init();
	}
	
	private void init() {
		this.frame = new JFrame();
		this.frame.setUndecorated(false);
		this.frame.setTitle("Snake");
		this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.frame.setFocusable(false);
		this.frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(1);
			}
		});
		this.frame.getContentPane().add(this.board);
		this.frame.getContentPane().setSize(200, 200);
		this.board.start();
		show();
	}
	
	private void show() {
		this.frame.pack();
		this.frame.setEnabled(true);
		this.frame.setVisible(true);
	}
	
	
	public boolean allDead() {
		boolean output = true;
		for (Creature c : this.board.getCreatures()) {
			output = output && !((Snake) c).isAlive();
		}
		return output;
	}

	public void reset() {
		board.init();
		board.start();
	}

	public Board getBoard() {
		return this.board;
	}

}
