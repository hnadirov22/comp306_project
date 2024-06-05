package sql;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import objects.Patient;




public class HomePage extends JFrame implements ActionListener,MouseListener{
	
	JPanel panel;
	JPanel northPanel;
	JPanel westPanel;
	JPanel eastPanel;
	JPanel southPanel;
	JLabel titleLabel;
	
	JButton appointment;
	JButton records;
	JButton vaccine;
	JButton profile;
	
	Color backgroundColor;
	
	Patient patient;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomePage frame = new HomePage(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public HomePage(Patient patient) {
		this.patient=patient;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,500);
		setLayout(new BorderLayout());
		
		backgroundColor = Color.decode("#00008B");
		
		northPanel = new JPanel();
		westPanel = new JPanel();
		eastPanel = new JPanel();
		southPanel = new JPanel();
		
	
		
		
		northPanel.setBackground(backgroundColor);
		eastPanel.setBackground(backgroundColor);
		westPanel.setBackground(backgroundColor);
		southPanel.setBackground(backgroundColor);
		
		
		
		northPanel.setPreferredSize(new Dimension(200,200));
		westPanel.setPreferredSize(new Dimension(100,100));
		eastPanel.setPreferredSize(new Dimension(100,100));
		southPanel.setPreferredSize(new Dimension(100,100));
		
		
		add(northPanel,BorderLayout.NORTH);
		add(westPanel,BorderLayout.WEST);
		add(eastPanel,BorderLayout.EAST);
		add(southPanel,BorderLayout.SOUTH);
		
		
		titleLabel = new JLabel("Home Page");
		titleLabel.setBorder(new EmptyBorder(30, 0, 0, 0));  
        Font font = new Font("Century Gothic",Font.BOLD, 70); 
        titleLabel.setFont(font);
        titleLabel.setForeground(Color.white);
        
        
        northPanel.add(titleLabel);

		
		panel = new JPanel();
		panel.setLayout(new GridLayout(2,2,40,40));
		panel.setBackground(backgroundColor);
		
		
		appointment = new JButton("Appointment");
		this.addButton(appointment);

		records = new JButton("Records");
		this.addButton(records);
		
		vaccine = new JButton("Vaccine");
		this.addButton(vaccine);
		
		profile  = new JButton("Profile");
		this.addButton(profile);
		
		add(panel,BorderLayout.CENTER);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
//		JButton source = (JButton)e.getSource();
//        source.setBorderPainted(true);
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
//		JButton source = (JButton)e.getSource();
//        source.setBorderPainted(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==appointment) {
			dispose();
			PendingAppointments window = new PendingAppointments(patient);
			window.setVisible(true);
		}
		
		else if(e.getSource()==vaccine) {
			dispose();
			Vaccine window = new Vaccine(patient);
			window.setVisible(true);
		}
		
		else if(e.getSource()==records) {
			dispose();
			MyAppointments window = new MyAppointments(patient);
			//window.setVisible(true);
		}
		
		else if(e.getSource()==profile) {
			dispose();
			PatientProfile window = new PatientProfile(patient);
			window.setVisible(true);
		}
	}
	
	public void addButton(JButton button) {
		
		button.setFocusPainted(false);
        button.setBorderPainted(true); 
        // 
		button.setBackground(backgroundColor);
		button.setForeground(Color.white);
		
		button.addActionListener(this);
		button.addMouseListener(this);
	
		button.setFont(new Font("Century Gothic",Font.BOLD, 50));
		panel.add(button);
	}
	
	


}








