/*
 * InstructFrame.java
 * Suraj Rampure
 * 
 * This class displays a simple image that shows the user how to play Stack.
 * It also features a back button.
 * The code is pretty self explanatory.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class InstructFrame extends JFrame implements ActionListener {
	
	JLabel background;
	CustomButton backButton;

	// Constructor method
	public InstructFrame(Point p) {
		
		super("Stack");
		
		setSize(1024, 790);
		setLayout(null);
		setLocation(p.x, p.y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		// backButton – takes the user back to an IntroFrame
		backButton = new CustomButton("Back");
		backButton.setBounds(39, 20, 74, 52);
		backButton.addActionListener(this);
		add(backButton);
		
		// background with the instructional image to display
		background = new JLabel(new ImageIcon("src/Images/Instructions.png"));
		background.setSize(1024, 768);
		add(background);
		
		setVisible(true);
	}
	
	// ActionListener implementation
	public void actionPerformed (ActionEvent evt) {
		
		Object src = evt.getSource();
		
		// New IntroFrame at the current position
		if (src == backButton) {
			Point p = this.getLocationOnScreen();
			
			new IntroFrame(p);
			
			setVisible(false);
		}
	}
	
	// Main method
	public static void main (String [] args) {
		new InstructFrame(new Point (0, 0));
	}
	
}
