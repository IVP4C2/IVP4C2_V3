package nl.edu.avans.ivp4c2.main;

import javax.swing.JFrame;

import nl.edu.avans.ivp4c2.manager.BarManager;
import nl.edu.avans.ivp4c2.presentation.BarGUIFrame;
public class Main{
	public static void main(String args[] ) {
		BarManager barmanager = new BarManager();
		JFrame frame = new BarGUIFrame(barmanager);
	}
}