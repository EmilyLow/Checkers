package emilylow.checkerspackage;

import javax.swing.JFrame;


public class CheckersFrame extends JFrame {
	private StatusWindow window;
	public CheckersFrame() 
	{
		window = new StatusWindow();
		
		add(new Board(window));
		pack();
	}
}
