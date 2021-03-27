package emilylow.checkerspackage;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;


public class CheckersFrame extends JFrame {
	private StatusWindow window;
	private Board board;
	public CheckersFrame() 
	{
		window = new StatusWindow();
		board = new Board(window);
		
		add(board.getDisplay(), BorderLayout.CENTER);
		add(window, BorderLayout.NORTH);
		//Pack uses the components preferred sizes and is a replacement for setSize()
		pack();
		
		//Bad design
//		setResizable(false);
	}
}
