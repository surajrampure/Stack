/*
 * GamePanel.java
 * Suraj Rampure
 * 
 * This class holds all of the mechanics and logic for the actual game.
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class GamePanel extends JPanel implements KeyListener {
	
	// We accept a GameFrame as a parameter so that we can start its timer after everything is loaded here
	GameFrame s;
	
	// Images for the grid squares to be drawn, current background and backgrounds for pop ups
	Image block, blockSide, playingBack, failBack, passBack, finalPassBack;
	
	// Holds all 25 background images
	ArrayList <Image> backgrounds = new ArrayList <Image> ();
	
	// Current user grid
	int [][] list = new int [8][10];
	
	// Current level's ultimate grid (what the user has to match, and what is shown on the right)
	int [][] sideList = new int[8][10];
	
	// Variables holding the currentX and currentY coordinates along with xDir (1 for right, -1 for left) and currentLevel ([1, 25])
	int currentX = 0, currentY = 0, xDir = 1, currentLevel = 1;
	int frameCount = 0, blockLen, numLevels;
	
	final int EMPTY = 0;
	final int FILLED = 1;
	
	// Number of blocks at each Y level
	int [] numBlocks;
	
	// Flags that hold whether or not to show the playing screen or the pass/fail/game completed screem
	boolean wait = false, failScreen = false, passScreen = false, finalPassScreen = false;
	
	// Flags that determine if the space is clicked and ready to be clicked (doesn't registered repeated KeyEvents)
	boolean isSpace = false, spaceReady = true;
	
	// Holds all 25 Level objects
	ArrayList <Level> ListOfLevels = new ArrayList <Level> ();
	
	// Constructor method
	public GamePanel(GameFrame stacker) {
		
		s = stacker;
		
		importLevels();
		
		// Image loading
		block = new ImageIcon("src/Images/Block.png").getImage();
		blockSide = new ImageIcon("src/Images/Block Side.png").getImage();
		failBack = new ImageIcon("src/Images/Fail Screen.png").getImage();
		passBack = new ImageIcon("src/Images/Pass Screen.png").getImage();
		finalPassBack = new ImageIcon("src/Images/Pass Screen Final.png").getImage();
		
		for (int i = 0; i < 25; i ++) {
			backgrounds.add(new ImageIcon(String.format("src/Images/Playing Screens/PS%d.png", i + 1)).getImage());

		}
		
		// Sets numBlocks, sideList and playingBack to the correct values
		updateElements();

		addKeyListener(this);
		
		setVisible(true);

	
	}

	public void addNotify() {

		super.addNotify();
		requestFocus();
		s.start();
	}

	// Imports all levels
	public void importLevels () {
		
		numLevels = 25;
		
		Scanner s = null;
		
		for (int i = 0; i < numLevels; i ++) {
			
			try {
				s = new Scanner(new File(String.format("src/Levels/Level%d.txt", i+1)));	// Level1.txt through Level25.txt
			}
			
			catch (IOException ex) {
				System.out.println(ex);
			}
			
			ListOfLevels.add(new Level(s));	// The Level class accepts a Scanner in the constructor
		}
	
	}
	
	// Sets numBlocks, sideList and playingBack to the correct values based on the current level
	public void updateElements () {
		numBlocks = ListOfLevels.get(currentLevel -1).numBlocks;
		
		sideList = ListOfLevels.get(currentLevel -1).arrayToMatch;
		
		playingBack = backgrounds.get(currentLevel -1);

		
	}

	// checkPass – checks to see if the user's grid up to and including the currentY value is 
	// the same as the array on the side
	public boolean checkPass () {
		for (int x = 0; x < 8; x ++) {
			for (int y = 0; y < currentY + 1; y ++) {
				if (list[x][y] != sideList[x][y]) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	// KeyListener implementation
	
	// KeyTyped – not used at all
	public void keyTyped(KeyEvent evt) {
	}
	
	// keyPressed – sets isSpace to true the first time the space bar is pressed
	// the move() method sets it false after currentY is increased, and spaceReady
	// is only made true once the key is released
	// The result – isSpace returns true ONCE no matter how long the user holds down the space bar
	public void keyPressed(KeyEvent evt) {
	
		if (evt.getKeyCode() == KeyEvent.VK_SPACE && spaceReady) {
			isSpace = true;
			spaceReady = false;
		}

		
	}

	// keyReleased – isSpace is obviously false however the user can now hit the spaceBar again
	public void keyReleased(KeyEvent evt) {
		isSpace = false;
		spaceReady = true;
	}
	
	// getMovement – checks when the user hits the spacebar and handles the multiple cases
	// 	If they're at the top of the grid and finish correctly – advance the level and show that screen
	//	If they're at the top of the LAST grid and finish correctly – they beat the game and show that screen
	//  If they messed up, show the fail screen
	//  If none of those things, add one to the currentY and reset currentX and xDir
	public void getMovement() {
		
		if (isSpace) {
			if (checkPass() && currentY == 9) {
				if (currentLevel < numLevels) {
					advanceLevel();
					passScreen = true;
					wait = true;
				
				}
				
				else {
					finalPassScreen = true;
					wait = true;
				}
				
				
			}
			
			else if (! checkPass()) {
				fail();
				failScreen = true;
				wait = true;
			}
			
			else {
				if (! wait)  {currentY ++; currentX = 0; xDir = 1;}
				
			}
			isSpace = false;
		}
		
	}
	
	// If the user is playing, this just calls move()
	public void controlMoves() {
		
		if (! wait) {
			move();
		}
	}

	// move – moves the current horizontal set of blocks left and right until the user hits space
	public void move() {

		currentX += xDir;				// x goes either right or left
		blockLen = numBlocks[currentY];			// each height level has a different block sequence length
		
		// Sets the spaces before the block sequence to EMPTY (0)
		for (int x = 0; x < blockLen; x ++) {
			list[Math.min(currentX + x - xDir, 7)][currentY] = EMPTY;
				
		}
	
		// Fills in the current spaces
		for (int x = 0; x < blockLen; x ++) {
			list[Math.min(currentX + x, 7)][currentY] = FILLED;
			
		}

		// Switches direction if the block reaches either end
		if (currentX == 0 || currentX + blockLen - 1 == 7) {
			xDir *= -1;
		}
		
		
		
	}

	// advanceLevel – increases the currentLevel, resets currentX, currentY, xDir, the grid,
	// updates numBlocks, playingBack and sideList based off of the new level
	public void advanceLevel() {

		currentLevel ++;
		currentX = 0;
		currentY = 0;
		xDir = 1;
		list = new int [8][10];
		updateElements();
		
		wait = false;

	}
	
	// fail – simply resets currentX, currentY, xDir and the grid
	// Nothing happens to the level number
	public void fail() {

		currentX = 0;
		currentY = 0;			
		xDir = 1;
		list = new int [8][10];
		
		wait = false;


	}
	
	// DRAWING
	
	// Draws the current arrangement of the main user grid
	public void drawBlocks (Graphics g) {
		
		for (int x = 0; x < 8; x ++) {
			
			for (int y = 0; y < 10; y ++) {
				
				if (list[x][y] == FILLED) {

					g.drawImage(block, 77 + 64*x, 642 - 64*y, this);
				}
			}
			
		}
	}

	// Draws the grid that the user must match on the fide
	public void drawSideGrid (Graphics g) {
	
		for (int x = 0; x < 8; x ++) {
			for (int y = 0; y < 10; y ++) {
				
				if (sideList[x][y] == FILLED) {
					g.drawImage(blockSide, 632 + 40*x, 625 - 40*y, this);
				}
			}
		}
		
	}

	// Draws everything
    public void paintComponent(Graphics g){

    	
    	// If the user is playing, it draws the playing elements
    	if (! wait) {

   		 g.drawImage(playingBack, 0, 0, this);

   		 drawSideGrid(g);

   		 drawBlocks(g);
   		 
    	}
    	
    	// If failScreen is true – it doesn't draw the playing background but instead the failBack
    	// Same goes for passScreen and finalPassScreen
    	if (failScreen) {
    		g.drawImage(failBack, 0, 0, this);
    	}
    	
    	if (passScreen) {
    		g.drawImage(passBack, 0, 0, this);

    	}
    	
    	if (finalPassScreen) {
    		g.drawImage(finalPassBack, 0, 0, this);
    		
    	}
    	

    }


	
}