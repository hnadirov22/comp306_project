package objects;

import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import sql.HomePage;
import sql.LogIn;
import sql.PatientProfile;
import sql.PendingAppointments;

public class JMenuClass {
	
	private JFrame frame;
	private Patient patient;
	
	private JMenuBar menuBar;
    private JMenu settingsMenu;
    private JMenuItem homeMenuItem;
    private JMenuItem profileMenuItem;
    private JMenuItem logoutMenuItem;
	
	public JMenuClass(JFrame frame,Patient patient) {
		this.frame = frame;
		this.patient = patient;
		menuBar = new JMenuBar();
		settingsMenu = new JMenu(patient.getName() + " " + patient.getSurname());
		homeMenuItem = new JMenuItem("Home Page");
		profileMenuItem = new JMenuItem("Profile Page");
		logoutMenuItem = new JMenuItem("Log out");
		
		
		settingsMenu.setFont(new Font("Objektiv Mk1",Font.BOLD, 15) );
		
		
		homeMenuItem.addActionListener(e -> goToHomePage());
        profileMenuItem.addActionListener(e -> goToProfilePage());
        logoutMenuItem.addActionListener(e -> logout());

        settingsMenu.add(homeMenuItem);
        settingsMenu.add(profileMenuItem);
        settingsMenu.add(logoutMenuItem);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(settingsMenu);
		}
	

	public void goToHomePage() {
		new HomePage(patient).setVisible(true);
        frame.dispose();
    }
	
	public void logout() {
        // Code to navigate to the Login class
        new LogIn().setVisible(true);
        frame.dispose();
    }
    
    public void goToProfilePage() {
		// TODO Auto-generated method stub
    	new PatientProfile(patient).setVisible(true);
    	frame.dispose();	
	}
    
    public void add(JMenuItem item) {
    	settingsMenu.add(item);
    }


	public JMenuBar getMenuBar() {
		return menuBar;
	}
    
    
}
