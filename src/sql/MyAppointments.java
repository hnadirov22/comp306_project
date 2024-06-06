//package sql;
//
//import javax.swing.*;
//import javax.swing.border.CompoundBorder;
//import javax.swing.border.EmptyBorder;
//import javax.swing.border.LineBorder;
//
//import objects.DatabaseConnection;
//import objects.JMenuClass;
//import objects.Patient;
//
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.sql.*;
//import java.util.*;
//import java.util.List;
//
//public class MyAppointments extends JFrame {
//    private static Map<Integer, List<Object>> appointmentsMap = new HashMap<>();
//    private int patientID;
//    private Patient patient;
//    private JFrame inputFrame;
//
//    public MyAppointments(Patient patient) {
//        this.patientID = patient.getId();
//        this.patient = patient;
//        populateAppointmentsMap();
//        createInputFrame(this);
//    }
//
//    public static void main(String[] args) {
//        EventQueue.invokeLater(() -> {
//            try {
//                // Creating a dummy patient object for demonstration purposes
//                //Patient dummyPatient = new Patient(1, "male", "John", 30, "Doe", "dummy", "");
//                MyAppointments frame = new MyAppointments(null);
//                //frame.setVisible(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    private void populateAppointmentsMap() {
//        try (Connection connection = DatabaseConnection.getConnection()) {
//            String query = "SELECT appointment.id, p_id, appointment_date, d_id, stat, billing, department_name, d_name, surname, age " +
//                    "FROM appointment INNER JOIN doctor ON doctor.id = appointment.d_id WHERE p_id = ?";
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setInt(1, patientID);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                int pId = resultSet.getInt("p_id");
//                Timestamp appointmentDate = resultSet.getTimestamp("appointment_date");
//                int dId = resultSet.getInt("d_id");
//                String stat = resultSet.getString("stat");
//                double billing = resultSet.getDouble("billing");
//                String departmentName = resultSet.getString("department_name");
//                String doctorName = resultSet.getString("d_name");
//                String surname = resultSet.getString("surname");
//                int age = resultSet.getInt("age");
//
//                List<Object> appointmentInfo = new ArrayList<>();
//                appointmentInfo.add(pId);
//                appointmentInfo.add(appointmentDate);
//                appointmentInfo.add(dId);
//                appointmentInfo.add(stat);
//                appointmentInfo.add(billing);
//                appointmentInfo.add(departmentName);
//                appointmentInfo.add(doctorName);
//                appointmentInfo.add(surname);
//                appointmentInfo.add(age);
//
//                appointmentsMap.put(id, appointmentInfo);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void createInputFrame(JFrame mainFrame) {
//        inputFrame = new JFrame("Filter Appointments");
//        inputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        inputFrame.setSize(400, 350);
//
//        JPanel inputPanel = new JPanel(new GridBagLayout());
//        inputPanel.setBackground(Color.decode("#00008B"));
//        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 10, 10);
//        gbc.gridx = 0;
//        gbc.gridy = GridBagConstraints.RELATIVE;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//
//        JLabel doctorNameLabel = new JLabel("Doctor Name:");
//        doctorNameLabel.setForeground(Color.WHITE);
//        JComboBox<String> doctorNameComboBox = new JComboBox<>(getDoctorNames());
//        doctorNameComboBox.setPreferredSize(new Dimension(200, 25));
//
//        JLabel statusLabel = new JLabel("Status:");
//        statusLabel.setForeground(Color.WHITE);
//        JPanel statusPanel = new JPanel();
//        statusPanel.setBackground(Color.decode("#00008B"));
//        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
//        JCheckBox confirmedCheckbox = new JCheckBox("Confirmed");
//        JCheckBox cancelledCheckbox = new JCheckBox("Cancelled");
//        confirmedCheckbox.setBackground(Color.decode("#00008B"));
//        cancelledCheckbox.setBackground(Color.decode("#00008B"));
//        confirmedCheckbox.setForeground(Color.WHITE);
//        cancelledCheckbox.setForeground(Color.WHITE);
//        statusPanel.add(confirmedCheckbox);
//        statusPanel.add(cancelledCheckbox);
//
//        JButton submitButton = new JButton("Submit");
//        submitButton.setPreferredSize(new Dimension(200, 30));
//
//        inputPanel.add(doctorNameLabel, gbc);
//        inputPanel.add(doctorNameComboBox, gbc);
//        inputPanel.add(statusLabel, gbc);
//        inputPanel.add(statusPanel, gbc);
//        inputPanel.add(submitButton, gbc);
//
//        inputFrame.add(inputPanel);
//        JMenuClass menuItem = new JMenuClass(this, patient);
//        JMenuItem operation = new JMenuItem("Operations");
//        operation.addActionListener(e -> goToOperations());
//        menuItem.add(operation);
//
//        JMenuItem test = new JMenuItem("Test Results");
//        test.addActionListener(e -> goToTest());
//        menuItem.add(test);
//
//        inputFrame.setJMenuBar(menuItem.getMenuBar());
//        inputFrame.setVisible(true);
//        mainFrame.setVisible(false);
//
//        submitButton.addActionListener(e -> {
//            String doctorName = (String) doctorNameComboBox.getSelectedItem();
//            if ("All".equals(doctorName)) {
//                doctorName = "";
//            }
//            List<String> statuses = new ArrayList<>();
//            if (confirmedCheckbox.isSelected()) statuses.add("confirmed");
//            if (cancelledCheckbox.isSelected()) statuses.add("cancelled");
//            inputFrame.dispose();
//            showAppointments(mainFrame, doctorName, statuses);
//        });
//    }
//
//    private String[] getDoctorNames() {
//        Set<String> doctorNames = new HashSet<>();
//        doctorNames.add("All");
//        doctorNames.addAll(appointmentsMap.values().stream()
//                .map(info -> (String) info.get(6))
//                .distinct()
//                .toList());
//        return doctorNames.toArray(new String[0]);
//    }
//
//    private void showAppointments(JFrame mainFrame, String doctorName, List<String> statuses) {
//        JFrame frame = new JFrame("Appointments");
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setSize(800, 600);
//        frame.setLayout(new BorderLayout());
//
//        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Panel for back button
//        JButton backButton = new JButton("Filter Options");
//        backButton.setFocusPainted(false);
//        topPanel.add(backButton);
//        frame.add(topPanel, BorderLayout.NORTH);
//
//        JPanel mainPanel = new JPanel(new GridBagLayout());
//        mainPanel.setBackground(Color.WHITE);
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridwidth = GridBagConstraints.REMAINDER;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.weightx = 1.0;
//        gbc.anchor = GridBagConstraints.NORTH;
//        gbc.insets = new Insets(10, 0, 10, 0);
//
//        for (Map.Entry<Integer, List<Object>> entry : appointmentsMap.entrySet()) {
//            int id = entry.getKey();
//            List<Object> appointmentInfo = entry.getValue();
//
//            String appointmentDoctorName = (String) appointmentInfo.get(6);
//            Timestamp appointmentDate = (Timestamp) appointmentInfo.get(1);
//            String appointmentStatus = (String) appointmentInfo.get(3);
//
//            boolean matchesDoctorName = doctorName == null || doctorName.isEmpty() || doctorName.equals(appointmentDoctorName);
//            boolean matchesStatus = statuses.isEmpty() || statuses.contains(appointmentStatus);
//
//            if (matchesDoctorName && matchesStatus) {
//                JPanel panel = new JPanel();
//                panel.setBackground(new Color(35, 63, 148));
//                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//                panel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(10, 10, 10, 10)));
//
//                JLabel doctorNameLabel = new JLabel("Doctor Name: " + appointmentInfo.get(6));
//                doctorNameLabel.setForeground(Color.WHITE);
//                JLabel appointmentDateLabel = new JLabel("Appointment Date: " + appointmentInfo.get(1));
//                appointmentDateLabel.setForeground(Color.WHITE);
//                JLabel statusLabel = new JLabel("Status: " + appointmentInfo.get(3));
//                statusLabel.setForeground(Color.WHITE);
//
//                panel.add(doctorNameLabel);
//                panel.add(appointmentDateLabel);
//                panel.add(statusLabel);
//
//                mainPanel.add(panel, gbc);
//
//                panel.addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mouseClicked(MouseEvent e) {
//                        JFrame detailFrame = new JFrame("Detailed Information");
//                        detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                        detailFrame.setSize(400, 300);
//
//                        JPanel detailPanel = new JPanel(new GridBagLayout());
//                        detailPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
//                        GridBagConstraints dGbc = new GridBagConstraints();
//                        dGbc.insets = new Insets(5, 5, 5, 5);
//                        dGbc.anchor = GridBagConstraints.WEST;
//                        dGbc.gridx = 0;
//                        dGbc.gridy = GridBagConstraints.RELATIVE;
//
//                        Font font = new Font("Arial", Font.PLAIN, 14);
//
//                        JLabel appointmentIdLabel = new JLabel("Appointment ID: " + id);
//                        appointmentIdLabel.setFont(font);
//                        detailPanel.add(appointmentIdLabel, dGbc);
//
//                        JLabel patientIdLabel = new JLabel("Patient ID: " + appointmentInfo.get(0));
//                        patientIdLabel.setFont(font);
//                        detailPanel.add(patientIdLabel, dGbc);
//
//                        JLabel appointmentDateDetailLabel = new JLabel("Appointment Date: " + appointmentInfo.get(1));
//                        appointmentDateDetailLabel.setFont(font);
//                        detailPanel.add(appointmentDateDetailLabel, dGbc);
//
//                        JLabel doctorIdLabel = new JLabel("Doctor ID: " + appointmentInfo.get(2));
//                        doctorIdLabel.setFont(font);
//                        detailPanel.add(doctorIdLabel, dGbc);
//
//                        JLabel statusDetailLabel = new JLabel("Status: " + appointmentInfo.get(3));
//                        statusDetailLabel.setFont(font);
//                        detailPanel.add(statusDetailLabel, dGbc);
//
//                        JLabel billingLabel = new JLabel("Billing: $" + appointmentInfo.get(4));
//                        billingLabel.setFont(font);
//                        detailPanel.add(billingLabel, dGbc);
//
//                        JLabel departmentLabel = new JLabel("Department: " + appointmentInfo.get(5));
//                        departmentLabel.setFont(font);
//                        detailPanel.add(departmentLabel, dGbc);
//
//                        JLabel doctorNameDetailLabel = new JLabel("Doctor Name: " + appointmentInfo.get(6));
//                        doctorNameDetailLabel.setFont(font);
//                        detailPanel.add(doctorNameDetailLabel, dGbc);
//
//                        JLabel ageLabel = new JLabel("Age: " + appointmentInfo.get(7));
//                        ageLabel.setFont(font);
//                        detailPanel.add(ageLabel, dGbc);
//
//                        detailFrame.add(detailPanel);
//                        detailFrame.setVisible(true);
//                    }
//                });
//            }
//        }
//
//        JScrollPane scrollPane = new JScrollPane(mainPanel);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//
//        backButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                frame.dispose(); // Close appointments frame
//                createInputFrame(mainFrame); // Reopen filter input frame
//            }
//        });
//
//        frame.add(scrollPane, BorderLayout.CENTER);
//        frame.setVisible(true);
//        mainFrame.setVisible(false);
//    }
//
//    public void goToOperations() {
//        new PastOperations(patient).setVisible(true);
//        inputFrame.dispose();
//    }
//
//    public void goToTest() {
//        new TestResults(patient).setVisible(true);
//        inputFrame.dispose();
//    }
//}
package sql;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import objects.DatabaseConnection;
import objects.JMenuClass;
import objects.Patient;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.*;
import java.util.List;

public class MyAppointments extends JFrame {
    private static Map<Integer, List<Object>> appointmentsMap = new HashMap<>();
    private int patientID;
    private Patient patient;
    private JFrame inputFrame;

    public MyAppointments(Patient patient) {
        this.patientID = patient.getId();
        this.patient = patient;
        populateAppointmentsMap();
        createInputFrame(this);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                // Creating a dummy patient object for demonstration purposes
                MyAppointments frame = new MyAppointments(new Patient(1, "male", "John", 30, "Doe", "dummy", ""));
                //frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void populateAppointmentsMap() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT appointment.id, p_id, appointment_date, d_id, stat, billing, department_name, d_name, surname, age " +
                    "FROM appointment INNER JOIN doctor ON doctor.id = appointment.d_id WHERE p_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, patientID);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createInputFrame(JFrame mainFrame) {
        inputFrame = new JFrame("Filter Appointments");
        inputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputFrame.setSize(800, 600); // Same size as TestResults

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        inputFrame.setContentPane(contentPane);

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.white);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JPanel headerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerButtonPanel.setBackground(Color.decode("#00008B"));

        JButton appointmentsButton = createHeaderButton("Appointments");
        appointmentsButton.setBackground(Color.decode("#00008B"));
        appointmentsButton.setForeground(Color.decode("#ADD8E6")); // Light blue color for the current page
        headerButtonPanel.add(appointmentsButton);

        JButton operationsButton = createHeaderButton("Operations");
        operationsButton.addActionListener(e -> goToOperations());
        headerButtonPanel.add(operationsButton);

        JButton testResultsButton = createHeaderButton("Test Results");
        testResultsButton.addActionListener(e -> goToTest());
        headerButtonPanel.add(testResultsButton);

        headerPanel.add(headerButtonPanel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Adding whitespace between buttons and input panel
        contentPane.add(headerPanel, BorderLayout.NORTH);

        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.decode("#00008B"));
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel doctorNameLabel = new JLabel("Doctor Name:");
        doctorNameLabel.setForeground(Color.WHITE);
        JComboBox<String> doctorNameComboBox = new JComboBox<>(getDoctorNames());
        doctorNameComboBox.setPreferredSize(new Dimension(200, 25));

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setForeground(Color.WHITE);
        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(Color.decode("#00008B"));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        JCheckBox confirmedCheckbox = new JCheckBox("Confirmed");
        JCheckBox cancelledCheckbox = new JCheckBox("Cancelled");
        confirmedCheckbox.setBackground(Color.decode("#00008B"));
        cancelledCheckbox.setBackground(Color.decode("#00008B"));
        confirmedCheckbox.setForeground(Color.WHITE);
        cancelledCheckbox.setForeground(Color.WHITE);
        statusPanel.add(confirmedCheckbox);
        statusPanel.add(cancelledCheckbox);

        JButton submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(200, 30));

        inputPanel.add(doctorNameLabel, gbc);
        inputPanel.add(doctorNameComboBox, gbc);
        inputPanel.add(statusLabel, gbc);
        inputPanel.add(statusPanel, gbc);
        inputPanel.add(submitButton, gbc);

        contentPane.add(inputPanel, BorderLayout.CENTER);

        JMenuClass menuItem = new JMenuClass(this, patient);
        JMenuItem operation = new JMenuItem("Operations");
        operation.addActionListener(e -> goToOperations());
        menuItem.add(operation);

        JMenuItem test = new JMenuItem("Test Results");
        test.addActionListener(e -> goToTest());
        menuItem.add(test);

        inputFrame.setJMenuBar(menuItem.getMenuBar());
        inputFrame.setVisible(true);
        mainFrame.setVisible(false);

        submitButton.addActionListener(e -> {
            String doctorName = (String) doctorNameComboBox.getSelectedItem();
            if ("All".equals(doctorName)) {
                doctorName = "";
            }
            List<String> statuses = new ArrayList<>();
            if (confirmedCheckbox.isSelected()) statuses.add("confirmed");
            if (cancelledCheckbox.isSelected()) statuses.add("cancelled");
            inputFrame.dispose();
            showAppointments(mainFrame, doctorName, statuses);
        });
    }

    private JButton createHeaderButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(Color.decode("#00008B"));
        button.setForeground(Color.white);
        button.setFont(new Font("Objektiv Mk1", Font.BOLD, 15));
        return button;
    }

    private String[] getDoctorNames() {
        Set<String> doctorNames = new HashSet<>();
        doctorNames.add("All");
        doctorNames.addAll(appointmentsMap.values().stream()
                .map(info -> (String) info.get(6))
                .distinct()
                .toList());
        return doctorNames.toArray(new String[0]);
    }

    private void showAppointments(JFrame mainFrame, String doctorName, List<String> statuses) {
        JFrame frame = new JFrame("Appointments");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600); // Same size as TestResults
        frame.setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.white);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JPanel headerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerButtonPanel.setBackground(Color.decode("#00008B"));

        JButton appointmentsButton = createHeaderButton("Appointments");
        appointmentsButton.setForeground(Color.decode("#ADD8E6")); // Light blue color for the current page
        appointmentsButton.setBackground(Color.decode("#00008E"));
        headerButtonPanel.add(appointmentsButton);

        JButton operationsButton = createHeaderButton("Operations");
        operationsButton.addActionListener(e -> goToOperations());
        headerButtonPanel.add(operationsButton);

        JButton testResultsButton = createHeaderButton("Test Results");
        testResultsButton.addActionListener(e -> goToTest());
        headerButtonPanel.add(testResultsButton);

        headerPanel.add(headerButtonPanel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Adding whitespace between buttons and main panel
        frame.add(headerPanel, BorderLayout.NORTH);

        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(10, 0, 10, 0);

        for (Map.Entry<Integer, List<Object>> entry : appointmentsMap.entrySet()) {
            int id = entry.getKey();
            List<Object> appointmentInfo = entry.getValue();

            String appointmentDoctorName = (String) appointmentInfo.get(6);
            Timestamp appointmentDate = (Timestamp) appointmentInfo.get(1);
            String appointmentStatus = (String) appointmentInfo.get(3);

            boolean matchesDoctorName = doctorName == null || doctorName.isEmpty() || doctorName.equals(appointmentDoctorName);
            boolean matchesStatus = statuses.isEmpty() || statuses.contains(appointmentStatus);

            if (matchesDoctorName && matchesStatus) {
                JPanel panel = new JPanel();
                panel.setBackground(new Color(35, 63, 148));
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(10, 10, 10, 10)));

                JLabel doctorNameLabel = new JLabel("Doctor Name: " + appointmentInfo.get(6));
                doctorNameLabel.setForeground(Color.WHITE);
                JLabel appointmentDateLabel = new JLabel("Appointment Date: " + appointmentInfo.get(1));
                appointmentDateLabel.setForeground(Color.WHITE);
                JLabel statusLabel = new JLabel("Status: " + appointmentInfo.get(3));
                statusLabel.setForeground(Color.WHITE);

                panel.add(doctorNameLabel);
                panel.add(appointmentDateLabel);
                panel.add(statusLabel);

                mainPanel.add(panel, gbc);

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
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
        mainFrame.setVisible(false);
    }

    public void goToOperations() {
        new PastOperations(patient).setVisible(true);
        inputFrame.dispose();
        this.dispose();
    }

    public void goToTest() {
        new TestResults(patient).setVisible(true);
        inputFrame.dispose();
        this.dispose();
    }
}

