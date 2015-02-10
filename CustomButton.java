/*
 * CustomButton.java
 * Suraj Rampure
 * 
 * This class extends JButton to make buttons to my taste
 * The images of these buttons are replaced with files found in the Images folder
 */

import javax.swing.*;

public class CustomButton extends JButton {
	
	// Constructor method
	public CustomButton(String s) {
		
		// Disabling the default view
		setContentAreaFilled(false);
		setBorderPainted(false);
		
		// Loading the new icons and setting them
		ImageIcon up = new ImageIcon("src/Images/" + s + "Up.png");
		ImageIcon hover = new ImageIcon("src/Images/" + s + "Hover.png");
		ImageIcon down = new ImageIcon("src/Images/" + s + "Down.png");
		
		setIcon(up);
		setRolloverIcon(hover);
		setPressedIcon(down);
		
			
	}
	
}