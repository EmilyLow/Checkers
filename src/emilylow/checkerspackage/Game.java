package emilylow.checkerspackage;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class Game {
	public static void main(String[] args)
	   { 
		
		 EventQueue.invokeLater(() -> {
			 var frame = new CheckersFrame();
			 frame.setTitle("Checkers");
			 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 frame.setVisible(true);
		 });
		 
	   }
}
