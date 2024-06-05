package sql;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import objects.DatabaseConnection;
import objects.Patient;

public class LogIn extends JFrame implements ActionListener, MouseListener {

    private JPanel main;
    private Font font;
    private Font textFieldFont;
    private JLabel emailLabel;
    private JButton loginButton;
    private JButton signupButton;
    private JTextField emailField;
    private JPasswordField passwordField;
    private String url;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LogIn frame = new LogIn();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LogIn() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);

        font = new Font("Objektiv Mk1", Font.BOLD, 20);
        textFieldFont = new Font("Arial", Font.PLAIN, 16);

        main = new JPanel();
        main.setBackground(Color.decode("#00008B"));
        main.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(main);

        main.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 100, 10);

        // Email Label
        emailLabel = new JLabel("Email:");
        emailLabel.setFont(font);
        emailLabel.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        main.add(emailLabel, gbc);

        // Email Field
        emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(200, 25));
        emailField.setFont(textFieldFont);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        main.add(emailField, gbc);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(font);
        passwordLabel.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        main.add(passwordLabel, gbc);

        // Password Field
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 25));
        passwordField.setFont(textFieldFont);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        main.add(passwordField, gbc);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 35));
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(this);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 40, 0);
        main.add(loginButton, gbc);

        // Sign Up Button
        signupButton = new JButton("Sign Up");
        signupButton.setPreferredSize(new Dimension(100, 35));
        signupButton.setFocusPainted(false);
        signupButton.addActionListener(this);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        main.add(signupButton, gbc);
        
        
        JScrollPane scrollPane = new JScrollPane(main);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        add(scrollPane);

        setVisible(true);
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
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            
            if(email.isEmpty() || password.isEmpty()) {JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);}
            
            else {

	            try {
	                Class.forName("com.mysql.cj.jdbc.Driver");
	                Connection connection = DatabaseConnection.getConnection();
	                Statement statement = connection.createStatement();
	
	                ResultSet resultSet = statement.executeQuery("select * from patient");
	                boolean fail=true;
	
	                while (resultSet.next()) {
	                    if (resultSet.getString(7).equals(email) && resultSet.getString(6).equals(password)) {
	                    	  Patient patient  = new Patient(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getInt(4),resultSet.getString(5),resultSet.getString(6),resultSet.getString(7));
	                    	  HomePage window = new HomePage(patient);
	                    	  fail=false;
	                    	  dispose();
	              			  window.setVisible(true);
	                    }
	                }
	                if(fail) {JOptionPane.showMessageDialog(main, "Please enter valid information.", "Error", JOptionPane.ERROR_MESSAGE);}
	                
	
	                connection.close();
	            } catch (Exception a) {
	                System.out.println(a);
	            }
	            
            }
        }
        
        if (e.getSource() == signupButton) {
        	dispose();
        	SignUpPage view  = new SignUpPage();
        	view.setVisible(true);
        	
        }
        
        
    }
}
