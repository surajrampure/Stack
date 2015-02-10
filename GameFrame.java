/*
 * GameFrame.java
 * Suraj Rampure
 * 
 * This class is the JFrame in which the GamePanel (which contains all of the game logic) is contained
 * This class holds the buttons for the screens that appear if the user has passed or failed the current level
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameFrame extends JFrame implements ActionListener {
	
	Timer myTimer;		// Regulates actionPerformed
	GamePanel game;		// GamePanel does not have static fields and methods – we create a GamePanel object
	CustomButton tryAgainButton, menuButton, advanceLevelButton, finalMenuButton;	// Various buttons, not all used at once
	
	// Constructor method
	public GameFrame (Point p) {
		
		super("Stack");
		
		setSize(1024, 790);
		setLocation(p.x, p.y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		myTimer = new Timer (5, this);		// Using a very small timer delay since it is visually delayed further in actionPerformed
		
		game = new GamePanel (this);		
		
		// tryAgainButton – When the user fails a level, this allows them to restart the level
		tryAgainButton = new CustomButton("Again");
		tryAgainButton.setBounds(192, 533, 256, 96);
		tryAgainButton.addActionListener(this);
		add(tryAgainButton);
		tryAgainButton.setVisible(false);
		
		// advanceLevelButton – when the user passes a level, this allows them to advance to the next level
		advanceLevelButton = new CustomButton("Next");
		advanceLevelButton.setBounds(192, 533, 256, 96);
		advanceLevelButton.addActionListener(this);
		add(advanceLevelButton);
		advanceLevelButton.setVisible(false);
		
		// menuButton – present when the user fails AND passes; takes them back to an IntroFrame
		menuButton = new CustomButton("Menu");
		menuButton.setBounds(576, 533, 256, 96);
		menuButton.addActionListener(this);
		add(menuButton);
		menuButton.setVisible(false);
		
		// finalMenuButton – same functionality as the above move button, however this is only used on the screen
		// where the user has beaten all 25 levels, as on this screen it is in a different spot
		finalMenuButton = new CustomButton("Menu");
		finalMenuButton.setBounds(384, 533, 256, 96);
		finalMenuButton.addActionListener(this);
		add(finalMenuButton);
		finalMenuButton.setVisible(false);
		
		add(game);
		
		setVisible(true);
	}

	// ActionListener implementation
	public void actionPerformed (ActionEvent evt) {
		
		Object src = evt.getSource();
		
		// If the user is currently playing the game, the other buttons are invisible
		// and methods from the GamePanel class must be called
		// 40 - currentLevel makes successive levels faster and is an extra delay
		if (! game.wait) {
			
			tryAgainButton.setVisible(false);
			menuButton.setVisible(false);
			advanceLevelButton.setVisible(false);
			finalMenuButton.setVisible(false);

			game.frameCount ++;			// We only draw if frameCount has reached a certain number
			
			game.getMovement();		
			
			if (src == myTimer && game.frameCount % (40 - game.currentLevel) == 0) {

				game.controlMoves();
				game.frameCount = 0;
				
			}
			
			game.repaint();

		}
		
		
		// Sets the buttons that must be visible when a certain game flag becomes true
		if (game.failScreen) {
			
			tryAgainButton.setVisible(true);
			menuButton.setVisible(true);
			
			
		}
		
		if (game.passScreen) {
			
			advanceLevelButton.setVisible(true);
			menuButton.setVisible(true);
			
		}
		
		if (game.finalPassScreen) {
			
			finalMenuButton.setVisible(true);
			
			
		}
		
		
		// New IntroFrame if the menu button is clicked
		if (src == menuButton) {
			Point p = this.getLocationOnScreen();
			new IntroFrame(p);
			setVisible(false);
			
		}
		
		// Starts the same level again if tryAgain is clicked
		if (src == tryAgainButton) {
			game.failScreen = false;
			game.wait = false;
			game.requestFocus();

		}
		
		// Starts the next level if advanceLevel is clicked
		if (src == advanceLevelButton) {
			game.passScreen = false;
			game.wait = false;
			game.requestFocus();

		}
		
		// Back to an IntroFrame (only once they've completed all 25 levels)
		if (src == finalMenuButton) {
			Point p = this.getLocationOnScreen();
			new IntroFrame(p);
			setVisible(false);
			
		}
		
		
	}
	
	// start – called by the game object to start the timer when it is ready
	public void start() {
		myTimer.start();
	}

	// Main method
	public static void main (String [] args) {
		new GameFrame(new Point(0, 0));
	}
	
}