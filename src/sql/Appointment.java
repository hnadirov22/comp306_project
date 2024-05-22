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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import objects.Doctor;

public class Appointment extends JFrame implements ActionListener,MouseListener{
	JPanel topPanel;
	JPanel northPanel;
	JPanel westPanel;
	JPanel eastPanel;
	JPanel southPanel;
	JPanel main;
	JPanel bottom;
	
	JPanel top;
	JPanel down;
	
	JLabel topText;
	
	JLabel doctor1;
	JLabel doctor2;
	JLabel doctor3;
	JLabel doctor4;
	JLabel doctor5;
	
	
	
	
	
	JLabel dLabel1;
	JLabel dLabel2;
	JLabel dLabel3;
	JLabel dLabel4;
	JLabel dLabel5;
	JLabel dLabel6;
	
	
	
	JLabel doctor2Name;
	JLabel doctor3Name;
	JLabel doctor4Name;
	JLabel doctor5Name;
	
//	ArrayList<Doctor> doctorList;
	HashMap<JLabel,Doctor> panelMap;
	ArrayList<JLabel> doctorLabels;
	ArrayList<JButton> buttonList;
//	ArrayList<String> surname;
	String url;
	
	Font font;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Appointment frame = new Appointment();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public Appointment() {
//		dname =new ArrayList<String>();
//		surname = new ArrayList<String>();
		doctorLabels = new ArrayList<JLabel>();
		buttonList = new ArrayList<JButton>();
		
		panelMap = new HashMap<JLabel,Doctor>();
		url = "jdbc:mysql://localhost:3306/hospital";
		
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,500);
		
//		setLayout(new BorderLayout());
		
		bottom = new JPanel();
		top = new JPanel();
		down = new JPanel();
		
		topPanel=new JPanel();
		topPanel.setPreferredSize(new Dimension(200,200));
		//topPanel.setBackground(Color.decode("#00008B"));
		
		topPanel.setLayout(new GridLayout(2,1,0,0));
		
		topPanel.add(top);
		topPanel.add(down);
		
		top.setBackground(Color.decode("#00008B"));
		down.setBackground(Color.white);
		
		topText = new JLabel("Select a Doctor");
		font = new Font("Objektiv Mk1",Font.BOLD, 40); 
		topText.setFont(font);
		topText.setForeground(Color.decode("#A9A9A9"));
		topText.setBorder(new EmptyBorder(20, 0, 0, 0));  
		down.add(topText);
		

		bottom.setLayout(new GridLayout(5,1,20,20));

		doctor1 = new JLabel();
		this.createFields(doctor1);
		
		
		doctor2 = new JLabel();
		this.createFields(doctor2);
		
		doctor3 = new JLabel();
		this.createFields(doctor3);
		
		
		doctor4 = new JLabel();
		this.createFields(doctor4);
		
		
		doctor5 = new JLabel();
		this.createFields(doctor5);
		
		
		doctorLabels.add(doctor1);
		doctorLabels.add(doctor2);
		doctorLabels.add(doctor3);
		doctorLabels.add(doctor4);
		doctorLabels.add(doctor5);
		
		createHashMapList();
		displayInfo();
		
		main=new JPanel();
		main.setLayout(new BorderLayout());
		main.add(topPanel,BorderLayout.NORTH);

		main.add(bottom,BorderLayout.CENTER);
		
		add(main);
		
		
		
		JScrollPane scrollPane = new JScrollPane(main);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Add the JScrollPane to the frame
        add(scrollPane, BorderLayout.CENTER);
        
        
        
        
        
        
        
        
        
        
    
		
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
	
	public void createHashMapList() {
		
		try {
    	    Class.forName("com.mysql.cj.jdbc.Driver");
    	    Connection connection = DriverManager.getConnection(url,"root","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");
    	    Statement statement = connection.createStatement();
    	    
    	    ResultSet resultSet = statement.executeQuery("select * from doctor");
    	    
    	    int myindex = 0;
    	    while(resultSet.next()) {
//    	    	dname.add(resultSet.getString(1));
//    	    	doctorList.add(new Doctor(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getInt(5)));
    	    	panelMap.put(doctorLabels.get(myindex), new Doctor(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getInt(5)));
    	    	myindex+=1;
    	    }
    	    
    	    connection.close();
    		}
    		catch(Exception e) {
    			System.out.println(e);
    		}
	}
	
	
	
	public void setFields(JLabel doctor,JLabel content) {
		doctor.setHorizontalAlignment(SwingConstants.CENTER);
        doctor.setVerticalAlignment(SwingConstants.CENTER);
		doctor.setFont(font);
		doctor.setForeground(Color.decode("#A9A9A9"));
		this.populateField(doctor,content);
	}
	
	public void displayPicture(JLabel doctor,JLabel component,String picture) {
		doctor.setPreferredSize(new Dimension(getWidth(),400));
		this.populateField(doctor,component);
		doctor.setBackground(Color.white);
		
		BufferedImage originalImage = loadImage("C:\\Users\\HP\\Desktop\\doctors\\"+picture);

        // Add a listener to resize the icon when the label's size changes
		doctor.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (originalImage != null) {
                    // Resize the image to fit the label
                    Image resizedImage = getScaledImage(originalImage, doctor.getWidth(), doctor.getHeight());
                    doctor.setIcon(new ImageIcon(resizedImage));
                    
                }
            }
        });
	}
	
	public void displayInfo() {;
		
		int myindex = 1;
		 for (Entry<JLabel, Doctor> entry : panelMap.entrySet()) {
			 setFields(new JLabel("Name: "+entry.getValue().getName()),entry.getKey());
			 setFields(new JLabel("Surname: "+entry.getValue().getSurname()),entry.getKey());
			 displayPicture(new JLabel(),entry.getKey(),"d"+Integer.toString(myindex)+".jpg");
			 setFields(new JLabel("Age: "+entry.getValue().getAge()),entry.getKey());
			 setFields(new JLabel("Department: "+entry.getValue().getDepartmentName()),entry.getKey());
			 JButton button = new JButton();
		     addButton(button, entry.getKey(),new JLabel());
			 myindex+=1;
			 
			 
			
			 
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

	    inside.setLayout(new GridBagLayout());

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 10; 
	    gbc.gridy = 4; 
	    
	    buttonList.add(button);

	    inside.add(button, gbc);
	    
	    label.add(inside);
	}

}
