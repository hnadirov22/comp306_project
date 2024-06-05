package sql;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import objects.DatabaseConnection;
import objects.Doctor;
import objects.JMenuClass;
import objects.Patient;

public class PendingAppointments extends JFrame implements ActionListener {

    private Patient myPatient;
    private static Map<Integer, List<Object>> appointmentsMap = new HashMap<>();
    private JButton apply;
    private JButton create;
    private JTextField tField;
    private JPanel mainPanel;
    
    
    

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                PendingAppointments frame = new PendingAppointments(null);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public PendingAppointments(Patient myPatient) {
        this.myPatient = myPatient;
        apply = new JButton("Apply");
        create = new JButton("Create");
        apply.addActionListener(this);
        create.addActionListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Create topPanel with GridBagLayout
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(Color.decode("#00008B"));
        topPanel.setBorder(new EmptyBorder(10, 20, 10, 20)); // Add padding around the panel
        GridBagConstraints gbd = new GridBagConstraints();
        gbd.insets = new Insets(5, 10, 5, 10); // Margin between components

        // Header label
        JLabel headerLabel = new JLabel("My Appointments");
        headerLabel.setFont(new Font("Objektiv Mk1", Font.BOLD, 24));
        headerLabel.setForeground(Color.white);
        gbd.gridx = 0;
        gbd.gridy = 0;
        gbd.gridwidth = 4;
        gbd.anchor = GridBagConstraints.CENTER;
        topPanel.add(headerLabel, gbd);
        
        
        
        JMenuClass menuItem = new JMenuClass(this,myPatient);
        setJMenuBar(menuItem.getMenuBar());

        // Reset gridwidth for the rest of the components
        gbd.gridwidth = 1;
        gbd.anchor = GridBagConstraints.WEST;

        // Doctor Name label
        JLabel dName = new JLabel("Doctor Name:");
        dName.setFont(new Font("Objektiv Mk1", Font.BOLD, 15));
        dName.setForeground(Color.white);
        gbd.gridx = 0;
        gbd.gridy = 1;
        topPanel.add(dName, gbd);

        // Text field
        tField = new JTextField(10);
        tField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbd.gridx = 1;
        gbd.gridy = 1;
        gbd.insets = new Insets(5, 5, 5, 5);
        topPanel.add(tField, gbd);

        // Apply button
        apply.setFont(new Font("Objektiv Mk1", Font.BOLD, 15));
        apply.setBackground(Color.white);
        apply.setForeground(Color.decode("#00008B"));
        apply.setFocusPainted(false);
        gbd.gridx = 2;
        gbd.gridy = 1;
        gbd.insets = new Insets(5, 20, 5, 85);
        topPanel.add(apply, gbd);

        // Create button
        create.setFont(new Font("Objektiv Mk1", Font.BOLD, 15));
        create.setBackground(Color.white);
        create.setForeground(Color.decode("#00008B"));
        create.setFocusPainted(false);
        gbd.gridx = 3;
        gbd.gridy = 1;
        //gbc.insets = new Insets(5, 5, 5, 85);
        topPanel.add(create, gbd);

        add(topPanel, BorderLayout.NORTH);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        populateAppointmentsMap();
        showAppointments(appointmentsMap);
    }

    protected void populateAppointmentsMap() {
    	appointmentsMap.clear();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DatabaseConnection.getConnection();

            String query = "SELECT appointment.id, p_id, appointment_date, d_id, stat, billing, department_name, d_name, surname, age " +
                    "FROM appointment INNER JOIN doctor ON doctor.id = appointment.d_id WHERE p_id = ? and stat = 'pending'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, myPatient.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int pId = resultSet.getInt("p_id");
                Timestamp appointmentDate = resultSet.getTimestamp("appointment_date");
                int dId = resultSet.getInt("d_id");
                String stat = resultSet.getString("stat");
                double billing = resultSet.getDouble("billing");
                String departmentName = resultSet.getString("department_name");
                String doctorName = resultSet.getString("d_name");
                String surname = resultSet.getString("surname");
                int age = resultSet.getInt("age");

                List<Object> appointmentInfo = new ArrayList<>();
                appointmentInfo.add(pId);
                appointmentInfo.add(appointmentDate);
                appointmentInfo.add(dId);
                appointmentInfo.add(stat);
                appointmentInfo.add(billing);
                appointmentInfo.add(departmentName);
                appointmentInfo.add(doctorName);
                appointmentInfo.add(surname);
                appointmentInfo.add(age);
                

                appointmentsMap.put(id, appointmentInfo);
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showAppointments(Map<Integer, List<Object>> viewMap) {
        mainPanel.removeAll(); // Clear the main panel before adding new components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(10, 0, 10, 0);

        for (Map.Entry<Integer, List<Object>> entry : viewMap.entrySet()) {
            int id = entry.getKey();
            List<Object> appointmentInfo = entry.getValue();

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(new Color(35, 63, 148));
            panel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(10, 10, 10, 10)));

            // Create a panel for the labels and add it to the left side
            JPanel labelPanel = new JPanel(new GridBagLayout());
            labelPanel.setBackground(new Color(35, 63, 148));
            GridBagConstraints labelGbc = new GridBagConstraints();
            labelGbc.anchor = GridBagConstraints.WEST;
            labelGbc.insets = new Insets(5, 0, 5, 0);

            JLabel doctorNameLabel = new JLabel("Doctor Name: " + appointmentInfo.get(6));
            doctorNameLabel.setForeground(Color.WHITE);
            labelGbc.gridx = 0;
            labelGbc.gridy = 0;
            labelPanel.add(doctorNameLabel, labelGbc);

            JLabel appointmentDateLabel = new JLabel("Appointment Date: " + appointmentInfo.get(1));
            appointmentDateLabel.setForeground(Color.WHITE);
            labelGbc.gridy = 1;
            labelPanel.add(appointmentDateLabel, labelGbc);

            JLabel statusLabel = new JLabel("Status: " + appointmentInfo.get(3));
            statusLabel.setForeground(Color.WHITE);
            labelGbc.gridy = 2;
            labelPanel.add(statusLabel, labelGbc);

            panel.add(labelPanel, BorderLayout.WEST);

            // Create a panel for the buttons and add it to the center
            JPanel buttonPanel = new JPanel(new GridBagLayout());
            buttonPanel.setOpaque(false); // Make the button panel transparent
            GridBagConstraints buttonGbc = new GridBagConstraints();
            buttonGbc.insets = new Insets(0, 10, 0, 10); // Add margin between buttons
            buttonGbc.gridx = 0;
            buttonGbc.gridy = 0;
            buttonGbc.anchor = GridBagConstraints.CENTER;

            JButton updateButton = new JButton("Update");
            updateButton.setFont(new Font("Objektiv Mk1", Font.BOLD, 15));
            updateButton.setBackground(Color.white);
            updateButton.setForeground(Color.decode("#00008B"));
            updateButton.setFocusPainted(false);

            JButton cancelButton = new JButton("Cancel");
            cancelButton.setFont(new Font("Objektiv Mk1", Font.BOLD, 15));
            cancelButton.setBackground(Color.white);
            cancelButton.setForeground(Color.decode("#00008B"));
            cancelButton.setFocusPainted(false);

            buttonPanel.add(updateButton, buttonGbc);

            buttonGbc.gridx = 1; // Move to the next column
            buttonPanel.add(cancelButton, buttonGbc);

            panel.add(buttonPanel, BorderLayout.CENTER);

            mainPanel.add(panel, gbc);

            // Add action listeners for the buttons
            updateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle the update action here
//                	JFrame updateFrame = new JFrame();
//                	updateFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                	updateFrame.setSize(400, 350);
//                	updateFrame.setLayout(new GridBagLayout());
//                	GridBagConstraints margins = new GridBagConstraints();
//                	margins.insets=new Insets(0, 0, 35, 5);
//                	margins.gridx=0;
//                	margins.gridy=0;
//                	JLabel newDate = new JLabel("Select a Date");
//                	newDate.setFont(new Font("Objektiv Mk1", Font.BOLD, 15));
//                	updateFrame.add(newDate,margins);
//                	
//                	margins.insets=new Insets(0, 5, 35, 0);
//                	margins.gridx++;
//                	JTextField dateField = new JTextField(10);
//                	dateField.setFont(new Font("Arial", Font.PLAIN, 16));
//                	updateFrame.add(dateField,margins);
//                	
//                	
//                	margins.insets=new Insets(0, 0, 0, 0);
//                	margins.gridx=0;
//                	margins.gridy=1;
//                	JLabel hour = new JLabel("Select the hour");
//                	hour.setFont(new Font("Objektiv Mk1", Font.BOLD, 15));
//                	updateFrame.add(hour,margins);
//                	
//                	JComboBox<String> hourBox = new JComboBox<String>(hourList);
//                	hourBox.setFont(new Font("Objektiv Mk1", Font.BOLD, 15));
//                	margins.insets=new Insets(0, 5, 0, 0);
//                	margins.gridx=1;
//                	margins.gridy=1;
//                	updateFrame.add(hourBox,margins);
//                	
//                	
//                	
//                	
//                	margins.insets=new Insets(20, 0, 0, 120);
//                	margins.gridx=1;
//                	margins.gridy=2;
//                	confirm = new JButton("Confirm");
//                	confirm.setFont(new Font("Objektiv Mk1", Font.BOLD, 15));
//                    confirm.setBackground(Color.white);
//                    confirm.setForeground(Color.decode("#00008B"));
//                    confirm.setFocusPainted(false);
//                    updateFrame.add(confirm,margins);
//                    
//                    
//                    
//                    
//                    confirm.addActionListener(new ActionListener() {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            // Get the selected item from the JComboBox
//                        	if(dateField.getText().isEmpty()) {updateFrame.dispose();}
//                        	
//                        	else {
//	                    	    Timestamp time = createTimestamp(dateField.getText(),(String)hourBox.getSelectedItem());
//	                    	    
//	                    	    if(time==null) {JOptionPane.showMessageDialog(updateFrame, "Please enter a valid date.", "Error", JOptionPane.ERROR_MESSAGE);}
//	                    	    
//	                    	    else {
//	                    	    	
//	                    	    	try {
//	                    	    		String url = "jdbc:mysql://localhost:3306/hospital";
//		                    	    	Class.forName("com.mysql.cj.jdbc.Driver");
//		                        	    Connection connection = DatabaseConnection.getConnection();
//		                        	    
//		                        	    
//		                        	    String sql = "update appointment set appointment_date = ? where id = ?";
//		                        	    
//		                        	    
//		                        	    PreparedStatement statement = connection.prepareStatement(sql);
//		                        	    statement.setTimestamp(1, time);
//		                        	    statement.setInt(2, id);
//		                        	    
//		                        	    int rows = statement.executeUpdate();
//		                        	    updateFrame.dispose();
//		                        	    populateAppointmentsMap();
//		                                showAppointments(appointmentsMap);
//		                    	    	connection.close();
//	                    	    	
//	                    	    	}
//	                    	    	catch (SQLIntegrityConstraintViolationException ex) {
//	                                    JOptionPane.showMessageDialog(updateFrame, "This appointment time is already taken.", "Error", JOptionPane.ERROR_MESSAGE);
//	                                    
//	                    	    	}
//	                    	    	
//	                    	    	catch(Exception error) {
//	                    	    		
//	                        			System.out.println(error);
//	                        		}
//	                    	    	
//	                    	    }
//	                    	    
//                        } 
//                        		}
//                        		
//                        }
//                    );              	
//                	updateFrame.setVisible(true);
                	CreateAppointment appointment =new CreateAppointment(id,(Integer)appointmentInfo.get(2),(Integer)appointmentInfo.get(0),false);
                	appointment.setVisible(true);
                	
                	appointment.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            if (appointment.isUpdated()) {
                                populateAppointmentsMap();
                                showAppointments(appointmentsMap);
                            }
                        }
                    });
                }
            });

            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	try {
                	    Class.forName("com.mysql.cj.jdbc.Driver");
                	    Connection connection = DatabaseConnection.getConnection();
                	    
                	    
                	    String sql = "update appointment set stat = 'cancelled' where id = ?";
                	    
                	    
                	    PreparedStatement statement = connection.prepareStatement(sql);
                	    statement.setInt(1, id);
                	    
                	    statement.executeUpdate();
                	    populateAppointmentsMap();
                        showAppointments(appointmentsMap);
                	    connection.close();
                		}
                		catch(Exception error) {
                			System.out.println(error);
                		}
                }
            });

            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JFrame detailFrame = new JFrame("Detailed Information");
                    detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    detailFrame.setSize(400, 300);

                    JPanel detailPanel = new JPanel(new GridBagLayout());
                    detailPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
                    GridBagConstraints dGbc = new GridBagConstraints();
                    dGbc.insets = new Insets(5, 5, 5, 5);
                    dGbc.anchor = GridBagConstraints.WEST;
                    dGbc.gridx = 0;
                    dGbc.gridy = GridBagConstraints.RELATIVE;

                    Font font = new Font("Arial", Font.PLAIN, 14);

                    JLabel appointmentIdLabel = new JLabel("Appointment ID: " + id);
                    appointmentIdLabel.setFont(font);
                    detailPanel.add(appointmentIdLabel, dGbc);

                    JLabel patientIdLabel = new JLabel("Patient ID: " + appointmentInfo.get(0));
                    patientIdLabel.setFont(font);
                    detailPanel.add(patientIdLabel, dGbc);

                    JLabel appointmentDateDetailLabel = new JLabel("Appointment Date: " + appointmentInfo.get(1));
                    appointmentDateDetailLabel.setFont(font);
                    detailPanel.add(appointmentDateDetailLabel, dGbc);

                    JLabel doctorIdLabel = new JLabel("Doctor ID: " + appointmentInfo.get(2));
                    doctorIdLabel.setFont(font);
                    detailPanel.add(doctorIdLabel, dGbc);

                    JLabel statusDetailLabel = new JLabel("Status: " + appointmentInfo.get(3));
                    statusDetailLabel.setFont(font);
                    detailPanel.add(statusDetailLabel, dGbc);

                    JLabel billingLabel = new JLabel("Billing: $" + appointmentInfo.get(4));
                    billingLabel.setFont(font);
                    detailPanel.add(billingLabel, dGbc);

                    JLabel departmentLabel = new JLabel("Department: " + appointmentInfo.get(5));
                    departmentLabel.setFont(font);
                    detailPanel.add(departmentLabel, dGbc);

                    JLabel doctorNameDetailLabel = new JLabel("Doctor Name: " + appointmentInfo.get(6));
                    doctorNameDetailLabel.setFont(font);
                    detailPanel.add(doctorNameDetailLabel, dGbc);

                    JLabel ageLabel = new JLabel("Age: " + appointmentInfo.get(7));
                    ageLabel.setFont(font);
                    detailPanel.add(ageLabel, dGbc);

                    detailFrame.add(detailPanel);
                    detailFrame.setVisible(true);
                }
            });
        }
        System.out.println("dkmf");

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == apply) {
            if (tField.getText().isEmpty()) {
            	populateAppointmentsMap();
                showAppointments(appointmentsMap);
            } else {
                Map<Integer, List<Object>> filteredMap = new HashMap<>();
                String filterText = tField.getText().toLowerCase();

                for (Map.Entry<Integer, List<Object>> entry : appointmentsMap.entrySet()) {
                    List<Object> appointmentDetail = entry.getValue();
                    String doctorName = (String) appointmentDetail.get(6);

                    if (doctorName.toLowerCase().contains(filterText)) {
                        filteredMap.put(entry.getKey(), appointmentDetail);
                    }
                }

                showAppointments(filteredMap);
            }
        } else if (e.getSource() == create) {
            // Handle the create action here
        	dispose();
            Appointment app = new Appointment(myPatient);
            app.setVisible(true);
        }
        
        
    }
    

}
