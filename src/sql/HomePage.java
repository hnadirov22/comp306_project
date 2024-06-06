//
//package sql;
//
//import java.awt.*;
//import java.awt.event.*;
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import objects.Patient;
//
//public class HomePage extends JFrame implements ActionListener, MouseListener {
//
//    private JPanel mainPanel;
//    private JPanel northPanel;
//    private JLabel nameLabel;
//
//    private JButton appointmentButton;
//    private JButton recordsButton;
//    private JButton vaccineButton;
//
//    private Color backgroundColor;
//    private Patient patient;
//
//    public static void main(String[] args) {
//        EventQueue.invokeLater(() -> {
//            try {
//                HomePage frame = new HomePage(null);
//                frame.setVisible(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    public HomePage(Patient patient) {
//        this.patient = patient;
//
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(500, 600);
//        setLayout(new BorderLayout());
//
//        backgroundColor = Color.decode("#00008B");
//
//        // Profile Panel setup
//        northPanel = createProfilePanel();
//        northPanel.setBackground(backgroundColor);
//
//        add(northPanel, BorderLayout.NORTH);
//
//        // Main panel setup for buttons
//        mainPanel = new JPanel();
//        mainPanel.setLayout(new GridLayout(3, 1, 10, 10));
//        mainPanel.setBackground(backgroundColor);
//        mainPanel.setBorder(new EmptyBorder(10, 50, 10, 50));
//
//        // Buttons
//        vaccineButton = createButton("Vaccine");
//        appointmentButton = createButton("Appointment");
//        recordsButton = createButton("Records");
//
//        add(mainPanel, BorderLayout.CENTER);
//    }
//
//    private JPanel createProfilePanel() {
//        JPanel profilePanel = new JPanel(new BorderLayout());
//        profilePanel.setPreferredSize(new Dimension(500, 100));
//        profilePanel.setBackground(backgroundColor);
//
//        nameLabel = new JLabel("Raul Aliyev");
//        nameLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));
//        nameLabel.setForeground(Color.WHITE);
//        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        nameLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
//        nameLabel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                dispose();
//                PatientProfile window = new PatientProfile(patient);
//                window.setVisible(true);
//            }
//        });
//        profilePanel.add(nameLabel, BorderLayout.CENTER);
//
//        return profilePanel;
//    }
//
//    private JButton createButton(String text) {
//        JButton button = new JButton(text);
//        button.setFocusPainted(false);
//        button.setBorderPainted(false);
//        button.setBackground(backgroundColor);
//        button.setForeground(Color.WHITE);
//        button.setFont(new Font("Century Gothic", Font.BOLD, 30));
//        button.addActionListener(this);
//        button.addMouseListener(this);
//
//        JPanel buttonPanel = new JPanel(new BorderLayout());
//        buttonPanel.setBackground(backgroundColor);
//        JLabel dotLabel = new JLabel("\u2022");
//        dotLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));
//        dotLabel.setForeground(Color.WHITE);
//        dotLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        buttonPanel.add(dotLabel, BorderLayout.WEST);
//        buttonPanel.add(button, BorderLayout.CENTER);
//
//        mainPanel.add(buttonPanel);
//        return button;
//    }
//
//    @Override
//    public void mouseClicked(MouseEvent e) {
//        // TODO Auto-generated method stub
//    }
//
//    @Override
//    public void mousePressed(MouseEvent e) {
//        // TODO Auto-generated method stub
//    }
//
//    @Override
//    public void mouseReleased(MouseEvent e) {
//        // TODO Auto-generated method stub
//    }
//
//    @Override
//    public void mouseEntered(MouseEvent e) {
//        // TODO Auto-generated method stub
//    }
//
//    @Override
//    public void mouseExited(MouseEvent e) {
//        // TODO Auto-generated method stub
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == appointmentButton) {
//            dispose();
//            PendingAppointments window = new PendingAppointments(patient);
//            window.setVisible(true);
//        } else if (e.getSource() == vaccineButton) {
//            dispose();
//            Vaccine window = new Vaccine(patient);
//            window.setVisible(true);
//        } else if (e.getSource() == recordsButton) {
//            dispose();
//            MyAppointments window = new MyAppointments(patient);
//            window.setVisible(true);
//        }
//    }
//}
//

package sql;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import objects.Patient;

public class HomePage extends JFrame implements ActionListener, MouseListener {

    private JPanel mainPanel;
    private JPanel northPanel;
    private JLabel nameLabel;

    private JButton appointmentButton;
    private JButton recordsButton;
    private JButton vaccineButton;

    private Color panelColor;
    private Color backgroundColor;
    private Patient patient;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                HomePage frame = new HomePage(null);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public HomePage(Patient patient) {
        this.patient = patient;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLayout(new BorderLayout());

        backgroundColor = Color.WHITE;
        panelColor = Color.decode("#00008B");

        // Profile Panel setup
        northPanel = createProfilePanel();
        northPanel.setBackground(panelColor);

        add(northPanel, BorderLayout.NORTH);

        // Main panel setup for buttons
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1, 10, 10));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(new EmptyBorder(10, 50, 10, 50));

        // Buttons
        vaccineButton = createButton("Vaccine");
        appointmentButton = createButton("Appointment");
        recordsButton = createButton("Records");

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createProfilePanel() {
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setPreferredSize(new Dimension(500, 100));
        profilePanel.setBackground(panelColor);

        nameLabel = new JLabel("Raul Aliyev");
        nameLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        nameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                PatientProfile window = new PatientProfile(patient);
                window.setVisible(true);
            }
        });
        profilePanel.add(nameLabel, BorderLayout.CENTER);

        return profilePanel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(panelColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Century Gothic", Font.BOLD, 30));
        button.addActionListener(this);
        button.addMouseListener(this);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(panelColor);
        JLabel dotLabel = new JLabel("\u2022");
        dotLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));
        dotLabel.setForeground(Color.WHITE);
        dotLabel.setHorizontalAlignment(SwingConstants.CENTER);
        buttonPanel.add(dotLabel, BorderLayout.WEST);
        buttonPanel.add(button, BorderLayout.CENTER);

        mainPanel.add(buttonPanel);
        return button;
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
        if (e.getSource() == appointmentButton) {
            dispose();
            PendingAppointments window = new PendingAppointments(patient);
            window.setVisible(true);
        } else if (e.getSource() == vaccineButton) {
            dispose();
            Vaccine window = new Vaccine(patient);
            window.setVisible(true);
        } else if (e.getSource() == recordsButton) {
            dispose();
//            MyAppointments window = new MyAppointments(patient);
//            TestResults window= new TestResults(patient);
            PastOperations window=new PastOperations(patient);

            window.setVisible(true);
        }
    }
}

