/*
 * IntroFrame.java
 * Suraj Rampure
 * 
 * This extended JFrame class contains the main menu for the game. It has two button options – "Play" or "How-To",
 * which redirect to extended JFrames for the game (GameFrame) and instructions (InstructFrame), respectively.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class IntroFrame extends JFrame implements ActionListener {

	CustomButton playButton, howButton;
	JLabel background;	// No JPanel and paintComponent, so the background image must be drawn this way
	
	// Constructor method
	public IntroFrame(Point p) {
		
		super("Stack");
		
		setSize(1024, 790);			// Slightly more than 768 to compensate for the menu bar
		setLayout(null);
		setLocation(p.x, p.y);		// Sets location to where the previous JFrame was (convention for the entire project)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		// playButton – launches game when clicked
		playButton = new CustomButton("Play");
		playButton.setBounds(192, 533, 256, 96);
		playButton.addActionListener(this);
		add(playButton);
		
		// howButton – launches instructions page when clicked
		howButton = new CustomButton("How");
		howButton.setBounds(576, 533, 256, 96);
		howButton.addActionListener(this);
		add(howButton);
		
		// "STACK" background
		background = new JLabel(new ImageIcon("src/Images/Intro Screen.png"));
		background.setSize(1024, 768);
		add(background);
		
		setVisible(true);
		
	}
	
	// ActionListener implementation
	public void actionPerformed (ActionEvent evt) {
		
		Object src = evt.getSource();
		
		// New GameFrame is created at the position of IntroFrame, and IntroFrame becomes invisible
		if (src == playButton) {
			
			Point p = this.getLocationOnScreen();
			
			new GameFrame (p);
			
			setVisible(false);
			
		}
		
		// New InstructFrame is created at the position of IntroFrame, and IntroFrame becomes invisible
		if (src == howButton) {
			
			Point p = this.getLocationOnScreen();
			
			new InstructFrame (p);
			
			setVisible(false);
			
		}

	}
	
	// Main method
	public static void main (String [] args) {
		
		new IntroFrame(new Point (0, 0));
		
	}
	
}