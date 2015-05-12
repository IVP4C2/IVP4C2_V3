package nl.edu.avans.ivp4c2.presentation;

import javax.swing.JFrame;
import javax.swing.JPanel;

import nl.edu.avans.ivp4c2.manager.BarManager;

public class BarGUIFrame extends JFrame {
	BarManager barmanager;
	
	public BarGUIFrame(BarManager barmanager) {
		this.barmanager = barmanager;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Bedieningsysteem");
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		JPanel panel = new BarGUI(barmanager);
		this.setContentPane(panel);
		this.setVisible(true);
	}
}
