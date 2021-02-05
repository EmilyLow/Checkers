package emilylow.checkerspackage;

import java.awt.BorderLayout;

import javax.swing.JFrame;


public class CheckersFrame extends JFrame {
	private StatusWindow window;
	public CheckersFrame() 
	{
		window = new StatusWindow();
		
		add(new Board(window), BorderLayout.CENTER);
		add(window, BorderLayout.NORTH);
		pack();
	}
}
