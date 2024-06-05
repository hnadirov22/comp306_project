package sql;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import objects.DatabaseConnection;
import objects.Doctor;
import objects.JMenuClass;
import objects.Patient;

public class Appointment extends JFrame implements ActionListener,MouseListener{
	JPanel topPanel;
	JPanel northPanel;
	JPanel westPanel;
	JPanel eastPanel;
	JPanel southPanel;
	JPanel main;
	JPanel bottom;
	
	JLabel topLabel;
	JPanel down;
	
	JLabel topText;
	JButton apply;
	
	HashMap<JLabel,Doctor> panelMap;
	ArrayList<JLabel> doctorLabels;
	ArrayList<String> path;
	ArrayList<Doctor> search;
	ArrayList<Doctor> fullList;
	String url;
	
	Font font;
	
	JComboBox<String> departmentComboBox;
	JLabel selectNameLabel;
	JTextField selectName;
	
	
	JPanel down1panel;
	JPanel down2panel;
	JPanel down3panel;
	
	JPanel bottomPanel;
	
	
	JComboBox<String> detailBox;
	
	private Patient patient;
	


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Appointment frame = new Appointment(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public Appointment(Patient patient) {
//		path = new ArrayList<String>();
//		path.add("d1.jpg");
//		path.add("d2.jpg");
//		path.add("d3.jpg");
//		path.add("d4.jpg");
//		path.add("d5.jpg");
		this.patient = patient;
		fullList = new ArrayList<Doctor>();
		doctorLabels = new ArrayList<JLabel>();
		search =new ArrayList<Doctor>();
		
		
		panelMap = new HashMap<JLabel,Doctor>();
		url = "jdbc:mysql://localhost:3306/hospital";

		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,500);
		font = new Font("Objektiv Mk1",Font.BOLD, 40); 

		
		bottom = new JPanel();

		
		topPanel= new JPanel();
		topPanel.setLayout(new GridLayout(2,1,0,0));
		topPanel.setBorder(new EmptyBorder(0,0,20,0));

		
		topLabel=new JLabel("Select a Doctor");
		topLabel.setFont(font);
		topLabel.setOpaque(true);
		topLabel.setBackground(Color.decode("#00008B"));
		topLabel.setForeground(Color.white);
		topLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topLabel.setVerticalAlignment(SwingConstants.CENTER);
		
		topPanel.add(topLabel);
		
		bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.decode("#00008B"));
		bottomPanel.setLayout(new GridBagLayout());
		
		
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        JLabel departmentLabel = new JLabel("Department");
        departmentLabel.setFont(new Font("Objektiv Mk1",Font.BOLD, 20) );
        departmentLabel.setForeground(Color.white);
        bottomPanel.add(departmentLabel, gbc);


        gbc.insets = new Insets(5, 5, 5, 85);
        gbc.gridx++;
        String[] department = {"None","Cardiology", "Neurology","Orthopedics","Pediatrics","Dermatology"};
        departmentComboBox = new JComboBox<>(department);
        departmentComboBox.addActionListener(this);
        bottomPanel.add(departmentComboBox, gbc);
        
        gbc.insets = new Insets(5, 40, 5, 5);
        gbc.gridx++;
        JLabel procedure = new JLabel("Procedure");
        procedure.setFont(new Font("Objektiv Mk1",Font.BOLD, 20) );
        procedure.setForeground(Color.white);
        bottomPanel.add(procedure, gbc);
        
        
        gbc.insets = new Insets(5, 5, 5, 85);
        gbc.gridx++;
        String [] empty = {"None"};
        detailBox = new JComboBox<>(empty);
        bottomPanel.add(detailBox, gbc);
        

        gbc.insets = new Insets(5, 40, 5, 5);
        gbc.gridx++;
        selectNameLabel = new JLabel("Name");
        selectNameLabel.setFont(new Font("Objektiv Mk1",Font.BOLD, 20) );
        selectNameLabel.setForeground(Color.white);
        bottomPanel.add(selectNameLabel, gbc);

        gbc.gridx++;
        gbc.insets = new Insets(5, 20, 5, 85);
        selectName = new JTextField(10);
        selectName.setFont(new Font("Arial", Font.PLAIN, 16));
        bottomPanel.add(selectName, gbc);

        gbc.gridx ++;
        gbc.insets = new Insets(5, 40, 5, 5);
        apply = new JButton("Apply");
        bottomPanel.add(apply, gbc);
        
        apply.setFont(new Font("Objektiv Mk1", Font.BOLD, 20));
	    apply.setBackground(Color.white);
	    apply.setForeground(Color.decode("#00008B"));
	    apply.setFocusPainted(false);
	    apply.addActionListener(this);
		

		
		topPanel.add(bottomPanel);
		




		initialize(getNumberOfRows());
		createDoctorList();
		//displayInfo();
		
		main=new JPanel();
		main.setLayout(new BorderLayout());
		main.add(topPanel,BorderLayout.NORTH);

		main.add(bottom,BorderLayout.CENTER);
		
		add(main);
		
		
		
		JScrollPane scrollPane = new JScrollPane(main);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);
        
        JMenuClass menuItem = new JMenuClass(this,patient);
        JMenuItem appointmentMenu = new JMenuItem("Appointment");
        appointmentMenu.addActionListener(e -> goToAppointment());
//        menuItem.getMenuBar().add(appointmentMenu);
        menuItem.add(appointmentMenu);
        setJMenuBar(menuItem.getMenuBar());
        
        

        
        
    
		
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
		// TODO Auto-generated method stub
		for (Doctor doc : panelMap.values()) {
			if(e.getSource()==doc.getButton()) {
				CreateAppointment appointment =new CreateAppointment(getNumberOfAppoitments()+1,doc.getId(),patient.getId(),true);
            	appointment.setVisible(true);
            	
//            	appointment.addWindowListener(new WindowAdapter() {
//                    @Override
//                    public void windowClosed(WindowEvent e) {
//                        if (appointment.isUpdated()) {
//                            populateAppointmentsMap();
//                            showAppointments(appointmentsMap);
//                        }
//                    }
//                });
            	break;

			}
		}
		
		if(e.getSource()==apply) {
			if(departmentComboBox.getSelectedItem().equals("None") && selectName.getText().isEmpty()) {
				 updateSearchMap(fullList);
				
			}
			
			else {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
		    	    Connection connection = DatabaseConnection.getConnection();
		    	    
		    	    String sql;
		    	    PreparedStatement statement;
		    	    if(!departmentComboBox.getSelectedItem().equals("None") && selectName.getText().isEmpty()) {
			    	    sql = "select * from doctor where department_name = ?";
			    	    statement = connection.prepareStatement(sql);
			    	    statement.setString(1, (String)departmentComboBox.getSelectedItem());
		    	    }
		    	    
		    	    else if(departmentComboBox.getSelectedItem().equals("None") && !selectName.getText().isEmpty()) {
		    	    	sql = "select * from doctor where d_name = ?";
			    	    statement = connection.prepareStatement(sql);
			    	    statement.setString(1, (String)selectName.getText());
		    	    }
		    	    
		    	    else {
		    	    	sql = "select * from doctor where d_name = ? and department_name = ?";
			    	    statement = connection.prepareStatement(sql);
			    	    statement.setString(1, (String)selectName.getText());
			    	    statement.setString(2, (String)departmentComboBox.getSelectedItem());
		    	    }
	

		    	    
		    	    ResultSet resultSet = statement.executeQuery();
		    	    
		    	    if (!resultSet.next()) {
		    	        JOptionPane.showMessageDialog(this, "No match found", "Error", JOptionPane.ERROR_MESSAGE);
		    	    } else {
		    	        do {
		    	            search.add(new Doctor(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5), new JButton(), Integer.toString(resultSet.getInt(1))+".jpg"));
		    	            //System.out.println("selected");
		    	        } while (resultSet.next());
		    	    }
		    	    
		    	    
		    	    connection.close();
		    	    
		    	    updateSearchMap(search);
		    	    search.clear();
				}
				
				catch(Exception error) {
					System.out.println(error);
				}
				
			}
				
			
		}
		
		
		if(e.getSource()==departmentComboBox) {
			ArrayList<String> detail = getDetail();
	        String[] detailArray = detail.toArray(new String[0]);
	        detailBox.removeAllItems();
	        for (String item : detailArray) {
	            detailBox.addItem(item);
	        }
		}
		
	}
	
	public void populateField(JLabel doctor,JLabel component) {
		doctor.setOpaque(true);
		doctor.setBackground(Color.decode("#00008B"));
		component.add(doctor);
	}
	
	public void createFields(JLabel doctor) {
		doctor.setPreferredSize(new Dimension(400,400));
		doctor.setOpaque(true);
		doctor.setBackground(Color.decode("#00008B"));
		
		doctor.setLayout(new GridLayout(2,2,0,0));
		bottom.add(doctor);
	}
	
	private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	private Image getScaledImage(BufferedImage srcImg, int width, int height) {
        Image scaledImage = srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        return bufferedImage;
    }
	
	public void createDoctorList() {
		
		try {
    	    Class.forName("com.mysql.cj.jdbc.Driver");
    	    Connection connection = DatabaseConnection.getConnection();
    	    Statement statement = connection.createStatement();
    	    
    	    ResultSet resultSet = statement.executeQuery("select * from doctor");
    	    
    	    
    	    while(resultSet.next()) {
    	    	fullList.add( new Doctor(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getInt(5),new JButton(),Integer.toString(resultSet.getInt(1))+".jpg"));
    	    	
    	    }
    	    
    	    connection.close();
    	    updateSearchMap(fullList);
    		}
    		catch(Exception e) {
    			System.out.println(e);
    		}
	}
	
	
	
	public void setFields(JLabel doctor,JLabel content) {
		doctor.setHorizontalAlignment(SwingConstants.CENTER);
        doctor.setVerticalAlignment(SwingConstants.CENTER);
		doctor.setFont(font);
		doctor.setForeground(Color.white);
		populateField(doctor,content);
	}
	
	public void displayPicture(JLabel doctor,JLabel component,String picture) {
		doctor.setPreferredSize(new Dimension(getWidth(),400));
		populateField(doctor,component);
		doctor.setBackground(Color.white);
		BufferedImage originalImage = loadImage("C:\\Users\\HP\\eclipse-workspace\\Hospital\\doctors\\"+picture);

		doctor.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (originalImage != null) {
                    Image resizedImage = getScaledImage(originalImage, doctor.getWidth(), doctor.getHeight());
                    doctor.setIcon(new ImageIcon(resizedImage));
                    
                }
            }
        });
	}
	
	public void displayInfo() {;
		 for (Entry<JLabel, Doctor> entry : panelMap.entrySet()) {
			 setFields(new JLabel("Name: "+entry.getValue().getName()),entry.getKey());
			 setFields(new JLabel("Surname: "+entry.getValue().getSurname()),entry.getKey());
			 displayPicture(new JLabel(),entry.getKey(),entry.getValue().getPicture());
			 setFields(new JLabel("Age: "+entry.getValue().getAge()),entry.getKey());
			 setFields(new JLabel("Department: "+entry.getValue().getDepartmentName()),entry.getKey());
		     addButton(entry.getValue().getButton(), entry.getKey(),new JLabel());

	        }
		
	}
	
	public void addButton(JButton button, JLabel label,JLabel inside) {
	    // Set the text and font for the button
	    button.setText("Select");
	    button.setFont(new Font("Objektiv Mk1", Font.BOLD, 20));
	    button.setBackground(Color.white);
	    button.setForeground(Color.decode("#00008B"));
	    button.setPreferredSize(new Dimension(150,50));
	    button.setFocusPainted(false);
	    button.addActionListener(this);
	    
	 

	    inside.setLayout(new GridBagLayout());

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 10; 
	    gbc.gridy = 4; 
	    
	

	    inside.add(button, gbc);
	    
	    label.add(inside);
	}
	
	
	public Integer getNumberOfRows() {
    	
    	try {
    	    Class.forName("com.mysql.cj.jdbc.Driver");
    	    Connection connection = DatabaseConnection.getConnection();
    	    Statement statement = connection.createStatement();
    	    
    	    
    	    ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM doctor");
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
    	    connection.close();
    	    
    		}
    		catch(Exception e) {
    			System.out.println(e);
    		}
		return null;
    }
	
	private ArrayList<String> getDetail(){
		try {
			ArrayList<String> procedure = new ArrayList<String>();
			
    	    Class.forName("com.mysql.cj.jdbc.Driver");
    	    Connection connection = DatabaseConnection.getConnection();
    	    String sql;
    	    PreparedStatement statement;
    	    
    	    sql = "select distinct description from operation where d_id in (select id from doctor where department_name = ?)";
    	    statement = connection.prepareStatement(sql);
    	    statement.setString(1, (String)departmentComboBox.getSelectedItem());
    	    
    	    
    	    ResultSet resultSet = statement.executeQuery();
            
    	    procedure.add("None");
    	    if (!resultSet.next()) {
    	        return procedure;
    	    }
    	    
    	    else {
    	    	
    	    	do {
    	            procedure.add(resultSet.getString(1));
    	        } while (resultSet.next());
    	    	
    	    }
    	    
    	    
            
    	    connection.close();
    	    
    	    return procedure;
    	    
    		}
    		catch(Exception e) {
    			System.out.println(e);
    		}
		
		return null;
	}
	
	private void initialize(int number) {
		for(int i=0;i<number;i++) {
			JLabel doctor = new JLabel();
			createFields(doctor);
			doctorLabels.add(doctor);
		}
	}
	
	private void updateSearchMap(ArrayList<Doctor> doctorList) {
		doctorLabels.clear();
		bottom.removeAll();
		bottom.setLayout(new GridLayout(doctorList.size(),1,20,20));
		initialize(doctorList.size());
		panelMap.clear();
		
		for(int i = 0;i<doctorList.size();i++) {
			panelMap.put(doctorLabels.get(i), doctorList.get(i));
			
		}
		displayInfo();
		
		bottom.revalidate();
		bottom.repaint();
		
		
	}
	
	public Integer getNumberOfAppoitments() {
    	
    	try {
    	    Class.forName("com.mysql.cj.jdbc.Driver");
    	    Connection connection = DatabaseConnection.getConnection();
    	    Statement statement = connection.createStatement();
    	    
    	    
    	    ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM appointment");
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
    	    connection.close();
    	    
    		}
    		catch(Exception e) {
    			System.out.println(e);
    		}
		return null;
    }
	
	public void goToAppointment() {
		// TODO Auto-generated method stub
    	new PendingAppointments(patient).setVisible(true);
    	this.dispose();	
	}

}

