//
//package sql;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.EventQueue;
//import java.awt.FlowLayout;
//import java.awt.Font;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.swing.BorderFactory;
//import javax.swing.Box;
//import javax.swing.BoxLayout;
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JMenuItem;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.border.EmptyBorder;
//
//import objects.DatabaseConnection;
//import objects.JMenuClass;
//import objects.Operation;
//import objects.OperationTableModel;
//import objects.Patient;
//
//public class PastOperations extends JFrame {
//
//    private static final long serialVersionUID = 1L;
//    private JPanel contentPane;
//    private JTable resultsTable;
//    private int patientID;
//    private Patient patient;
//
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    PastOperations frame = new PastOperations(new Patient(1, "male", "Azad", 22, "ASLANLI", "dummy", "aaslanli21"));
//                    frame.setVisible(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    public PastOperations(Patient p) {
//        this.patient = p;
//        this.patientID = p.getId();
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setBounds(100, 100, 800, 400);
//        contentPane = new JPanel();
//        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//
//        setContentPane(contentPane);
//        contentPane.setLayout(new BorderLayout(0, 0));
//
//        JPanel headerPanel = new JPanel();
//        headerPanel.setBackground(Color.white);
//        contentPane.add(headerPanel, BorderLayout.NORTH);
//        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
//
//        JPanel headerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        headerButtonPanel.setBackground(Color.decode("#00008B"));
//
//        JButton appointmentsButton = createHeaderButton("Appointments");
//        appointmentsButton.addActionListener(e -> goToAppointment());
//        headerButtonPanel.add(appointmentsButton);
//
//        JButton operationsButton = createHeaderButton("Operations");
//        operationsButton.addActionListener(e -> goToOperations());
//        headerButtonPanel.add(operationsButton);
//
//        JButton testResultsButton = createHeaderButton("Test Results");
//        testResultsButton.addActionListener(e -> goToTest());
//        headerButtonPanel.add(testResultsButton);
//
//        headerPanel.add(headerButtonPanel);
//
//        headerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Adding whitespace between buttons and Patient name
//
//        JPanel patientNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        patientNamePanel.setBackground(Color.decode("#00008B"));
//        JLabel patientNameLabel = new JLabel(p.getName() + " " + p.getSurname());
//        patientNameLabel.setFont(new Font("Objektiv Mk1", Font.BOLD, 15));
//        patientNameLabel.setForeground(Color.white);
//        patientNamePanel.add(patientNameLabel);
//        headerPanel.add(patientNamePanel);
//
//        JPanel infoPanel = new JPanel();
//        contentPane.add(infoPanel, BorderLayout.WEST);
//        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
//
//        JScrollPane resultsPane = new JScrollPane();
//        contentPane.add(resultsPane, BorderLayout.CENTER);
//
//        List<Operation> op_List = getOperations();
//        OperationTableModel model = new OperationTableModel(op_List);
//        resultsTable = new JTable(model);
//        resultsPane.setViewportView(resultsTable);
//
//        JPanel billingPanel = new JPanel();
//        billingPanel.setBackground(Color.white);
//        contentPane.add(billingPanel, BorderLayout.EAST);
//        billingPanel.setLayout(new BoxLayout(billingPanel, BoxLayout.Y_AXIS));
//        billingPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Billing Information"));
//
//        JPanel avgBillingPanel = new JPanel();
//        avgBillingPanel.setBackground(Color.white);
//        avgBillingPanel.setLayout(new BoxLayout(avgBillingPanel, BoxLayout.Y_AXIS));
//        avgBillingPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Average Billings"));
//        billingPanel.add(avgBillingPanel);
//
//        Map<String, Float> avgBillingMap = billingToDoctor();
//        for (Map.Entry<String, Float> entry : avgBillingMap.entrySet()) {
//            JLabel label = new JLabel("   " + entry.getKey() + ": " + entry.getValue() + "   ");
//            avgBillingPanel.add(label);
//        }
//
//        billingPanel.setPreferredSize(new Dimension(150, 200));
//
//        billingPanel.add(Box.createRigidArea(new Dimension(0, 20)));
//
//        JPanel totalBillingPanel = new JPanel();
//        totalBillingPanel.setBackground(Color.white);
//        totalBillingPanel.setLayout(new BoxLayout(totalBillingPanel, BoxLayout.Y_AXIS));
//        totalBillingPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Total Billings"));
//        billingPanel.add(totalBillingPanel);
//
//        float totalBilling = getBill();
//        JLabel totalBillingLabel = new JLabel("   Total: " + totalBilling + "   ");
//        totalBillingPanel.add(totalBillingLabel);
//
//        JMenuClass menuItem = new JMenuClass(this, p);
//
//        JMenuItem appointment = new JMenuItem("Appointments");
//        appointment.addActionListener(e -> goToAppointment());
//        menuItem.add(appointment);
//
//        JMenuItem test = new JMenuItem("Test Results");
//        test.addActionListener(e -> goToTest());
//        menuItem.add(test);
//
//        setJMenuBar(menuItem.getMenuBar());
//    }
//
//    private JButton createHeaderButton(String text) {
//        JButton button = new JButton(text);
//        button.setFocusPainted(false);
//        button.setBorderPainted(false);
//        button.setBackground(Color.decode("#00008B"));
//        button.setForeground(Color.white);
//        button.setFont(new Font("Objektiv Mk1", Font.BOLD, 15));
//        return button;
//    }
//
//    private List<Operation> getOperations() {
//        List<Operation> ops = new ArrayList<>();
//        String query = "SELECT operation.id, d_id, p_id, description, billing, d_name, surname\n"
//                + "FROM operation INNER JOIN doctor ON doctor.id = operation.d_id\n"
//                + "WHERE p_id = ?";
//
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement ops_Stat = connection.prepareStatement(query)) {
//
//            ops_Stat.setInt(1, this.patientID);
//            ResultSet ops_Set = ops_Stat.executeQuery();
//
//            while (ops_Set.next()) {
//                int id = ops_Set.getInt("id");
//                int patientID = ops_Set.getInt("p_id");
//                int doctorID = ops_Set.getInt("d_id");
//                String description = ops_Set.getString("description");
//                double billing = ops_Set.getDouble("billing");
//                String doctorName = ops_Set.getString("d_name");
//                String doctorSName = ops_Set.getString("surname");
//
//                ops.add(new Operation(id, patientID, doctorID, description, billing, doctorName, doctorSName));
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return ops;
//    }
//
//    private Map<String, Float> billingToDoctor() {
//        Map<String, Float> bill_map = new HashMap<>();
//        String query = "SELECT d_name, surname, AVG(billing) as av_billing\n"
//                + "FROM operation INNER JOIN doctor ON doctor.id = operation.d_id\n"
//                + "WHERE p_id = ? GROUP BY d_id HAVING AVG(billing) > 0;";
//
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement bill_Stat = connection.prepareStatement(query)) {
//
//            bill_Stat.setInt(1, this.patientID);
//            ResultSet resultSet = bill_Stat.executeQuery();
//
//            while (resultSet.next()) {
//                String doc_id = resultSet.getString("d_name") + " " + resultSet.getString("surname");
//                float averageBilling = resultSet.getFloat("av_billing");
//                bill_map.put(doc_id, averageBilling);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return bill_map;
//    }
//
//    private float getBill() {
//        float totalBilling = 0;
//        String totalBillingQuery = "SELECT SUM(billing) as total_billing FROM operation WHERE p_id = ?;";
//
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement totalBillingStatement = connection.prepareStatement(totalBillingQuery)) {
//
//            totalBillingStatement.setInt(1, this.patientID);
//            ResultSet resultSet = totalBillingStatement.executeQuery();
//            if (resultSet.next()) {
//                totalBilling = resultSet.getFloat("total_billing");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return totalBilling;
//    }
//
//    public void goToAppointment() {
//        new MyAppointments(patient).setVisible(true);
//        this.dispose();
//    }
//
//    public void goToTest() {
//        new TestResults(patient).setVisible(true);
//        this.dispose();
//    }
//
//    public void goToOperations() {
//        new PastOperations(patient).setVisible(true);
//        this.dispose();
//    }
//}
package sql;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import objects.DatabaseConnection;
import objects.JMenuClass;
import objects.Operation;
import objects.OperationTableModel;
import objects.Patient;

public class PastOperations extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable resultsTable;
    private int patientID;
    private Patient patient;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PastOperations frame = new PastOperations(new Patient(1, "male", "Azad", 22, "ASLANLI", "dummy", "aaslanli21"));
                    //frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public PastOperations(Patient p) {
        this.patient = p;
        this.patientID = p.getId();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.white);
        contentPane.add(headerPanel, BorderLayout.NORTH);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JPanel headerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerButtonPanel.setBackground(Color.decode("#00008B"));

        JButton appointmentsButton = createHeaderButton("Appointments");
        appointmentsButton.addActionListener(e -> goToAppointment());
        headerButtonPanel.add(appointmentsButton);

        JButton operationsButton = createHeaderButton("Operations");
        operationsButton.setBackground(Color.decode("#00008B"));

        operationsButton.setForeground(Color.decode("#ADD8E6")); // Light blue color for the current page
        headerButtonPanel.add(operationsButton);

        JButton testResultsButton = createHeaderButton("Test Results");
        testResultsButton.addActionListener(e -> goToTest());
        headerButtonPanel.add(testResultsButton);

        headerPanel.add(headerButtonPanel);

        headerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Adding whitespace between buttons and Patient name

        JPanel patientNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        patientNamePanel.setBackground(Color.decode("#00008B"));
        JLabel patientNameLabel = new JLabel(p.getName() + " " + p.getSurname());
        patientNameLabel.setFont(new Font("Objektiv Mk1", Font.BOLD, 15));
        patientNameLabel.setForeground(Color.white);
        patientNamePanel.add(patientNameLabel);
        headerPanel.add(patientNamePanel);

        JPanel infoPanel = new JPanel();
        contentPane.add(infoPanel, BorderLayout.WEST);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JScrollPane resultsPane = new JScrollPane();
        contentPane.add(resultsPane, BorderLayout.CENTER);

        List<Operation> op_List = getOperations();
        OperationTableModel model = new OperationTableModel(op_List);
        resultsTable = new JTable(model);
        resultsPane.setViewportView(resultsTable);

        JPanel billingPanel = new JPanel();
        billingPanel.setBackground(Color.white);
        contentPane.add(billingPanel, BorderLayout.EAST);
        billingPanel.setLayout(new BoxLayout(billingPanel, BoxLayout.Y_AXIS));
        billingPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Billing Information"));

        JPanel avgBillingPanel = new JPanel();
        avgBillingPanel.setBackground(Color.white);
        avgBillingPanel.setLayout(new BoxLayout(avgBillingPanel, BoxLayout.Y_AXIS));
        avgBillingPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Average Billings"));
        billingPanel.add(avgBillingPanel);

        Map<String, Float> avgBillingMap = billingToDoctor();
        for (Map.Entry<String, Float> entry : avgBillingMap.entrySet()) {
            JLabel label = new JLabel("   " + entry.getKey() + ": " + entry.getValue() + "   ");
            avgBillingPanel.add(label);
        }

        billingPanel.setPreferredSize(new Dimension(150, 200));

        billingPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel totalBillingPanel = new JPanel();
        totalBillingPanel.setBackground(Color.white);
        totalBillingPanel.setLayout(new BoxLayout(totalBillingPanel, BoxLayout.Y_AXIS));
        totalBillingPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Total Billings"));
        billingPanel.add(totalBillingPanel);

        float totalBilling = getBill();
        JLabel totalBillingLabel = new JLabel("   Total: " + totalBilling + "   ");
        totalBillingPanel.add(totalBillingLabel);

        JMenuClass menuItem = new JMenuClass(this, p);

        JMenuItem appointment = new JMenuItem("Appointments");
        appointment.addActionListener(e -> goToAppointment());
        menuItem.add(appointment);

        JMenuItem test = new JMenuItem("Test Results");
        test.addActionListener(e -> goToTest());
        menuItem.add(test);

        setJMenuBar(menuItem.getMenuBar());
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

    private List<Operation> getOperations() {
        List<Operation> ops = new ArrayList<>();
        String query = "SELECT operation.id, d_id, p_id, description, billing, d_name, surname\n"
                + "FROM operation INNER JOIN doctor ON doctor.id = operation.d_id\n"
                + "WHERE p_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ops_Stat = connection.prepareStatement(query)) {

            ops_Stat.setInt(1, this.patientID);
            ResultSet ops_Set = ops_Stat.executeQuery();

            while (ops_Set.next()) {
                int id = ops_Set.getInt("id");
                int patientID = ops_Set.getInt("p_id");
                int doctorID = ops_Set.getInt("d_id");
                String description = ops_Set.getString("description");
                double billing = ops_Set.getDouble("billing");
                String doctorName = ops_Set.getString("d_name");
                String doctorSName = ops_Set.getString("surname");

                ops.add(new Operation(id, patientID, doctorID, description, billing, doctorName, doctorSName));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ops;
    }

    private Map<String, Float> billingToDoctor() {
        Map<String, Float> bill_map = new HashMap<>();
        String query = "SELECT d_name, surname, AVG(billing) as av_billing\n"
                + "FROM operation INNER JOIN doctor ON doctor.id = operation.d_id\n"
                + "WHERE p_id = ? GROUP BY d_id HAVING AVG(billing) > 0;";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement bill_Stat = connection.prepareStatement(query)) {

            bill_Stat.setInt(1, this.patientID);
            ResultSet resultSet = bill_Stat.executeQuery();

            while (resultSet.next()) {
                String doc_id = resultSet.getString("d_name") + " " + resultSet.getString("surname");
                float averageBilling = resultSet.getFloat("av_billing");
                bill_map.put(doc_id, averageBilling);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bill_map;
    }

    private float getBill() {
        float totalBilling = 0;
        String totalBillingQuery = "SELECT SUM(billing) as total_billing FROM operation WHERE p_id = ?;";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement totalBillingStatement = connection.prepareStatement(totalBillingQuery)) {

            totalBillingStatement.setInt(1, this.patientID);
            ResultSet resultSet = totalBillingStatement.executeQuery();
            if (resultSet.next()) {
                totalBilling = resultSet.getFloat("total_billing");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalBilling;
    }

    public void goToAppointment() {
        new MyAppointments(patient).setVisible(true);
        this.dispose();
    }

    public void goToTest() {
        new TestResults(patient).setVisible(true);
        this.dispose();
    }

    public void goToOperations() {
        // This method is not needed here since we are already on the PastOperations page
    }
}

