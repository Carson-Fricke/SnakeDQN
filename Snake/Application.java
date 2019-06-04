package com.caibear.snake;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Application {
	private JFrame frame;
	
	private Board board;
	
	public Application() {
		Setting s = new Setting();
		//s.addOperator(o);
		this.board = new Board(s);
		init();
	}
	
	private void init() {
		this.frame = new JFrame();
		this.frame.setUndecorated(true);
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
		this.board.start();
		show();
	}
	
	private void show() {
		this.frame.pack();
		this.frame.setEnabled(true);
		this.frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Application();
	}
}
