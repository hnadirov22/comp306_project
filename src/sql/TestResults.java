
package sql;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import objects.DatabaseConnection;
import objects.JMenuClass;
import objects.Patient;
import objects.Test;
import objects.TestTableModel;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import java.awt.Color;

public class TestResults extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable resultsTable;
    private int patientID;
    private Patient patient;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TestResults frame = new TestResults(new Patient(1, "male", "Azad", 22, "ASLANLI", "dummy", "aaslanli21"));
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public TestResults(Patient patient) {
        this.patient = patient;
        this.patientID = patient.getId();
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
        operationsButton.addActionListener(e -> goToOperations());
        headerButtonPanel.add(operationsButton);

        JButton testResultsButton = createHeaderButton("Test Results");
        testResultsButton.setBackground(Color.decode("#00008B"));

        testResultsButton.setForeground(Color.decode("#ADD8E6")); // Light blue color for the current page
        headerButtonPanel.add(testResultsButton);

        headerPanel.add(headerButtonPanel);

        headerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Adding whitespace between buttons and Patient name

        JPanel patientNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        patientNamePanel.setBackground(Color.decode("#00008B"));
        JLabel patientNameLabel = new JLabel(patient.getName() + " " + patient.getSurname());
        patientNameLabel.setFont(new Font("Objektiv Mk1", Font.BOLD, 15));
        patientNameLabel.setForeground(Color.white);
        patientNamePanel.add(patientNameLabel);
        headerPanel.add(patientNamePanel);

        JPanel infoPanel = new JPanel();
        contentPane.add(infoPanel, BorderLayout.WEST);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JScrollPane resultsPane = new JScrollPane();
        contentPane.add(resultsPane, BorderLayout.CENTER);

        List<Test> testList = fetchResults();
        TestTableModel model = new TestTableModel(testList);
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

        Map<String, Float> avgBillingMap = fetchAvgBillings();
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

        float totalBilling = fetchTotalBillings();
        JLabel totalBillingLabel = new JLabel("   Total: " + totalBilling + "   ");
        totalBillingPanel.add(totalBillingLabel);

        JMenuClass menuItem = new JMenuClass(this, patient);

        JMenuItem operation = new JMenuItem("Operations");
        operation.addActionListener(e -> goToOperations());
        menuItem.add(operation);

        JMenuItem appointment = new JMenuItem("Appointments");
        appointment.addActionListener(e -> goToAppointment());
        menuItem.add(appointment);

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

    private List<Test> fetchResults() {
        List<Test> tests = new ArrayList<>();
        String resultsQuery = "SELECT *\n"
                + "FROM test\n"
                + "WHERE p_id = ?;";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement resultsStatement = connection.prepareStatement(resultsQuery)) {

            resultsStatement.setInt(1, this.patientID);
            ResultSet resultSet = resultsStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int patientID = resultSet.getInt("p_id");
                int appointmentID = resultSet.getInt("app_id");
                String description = resultSet.getString("description");
                double billing = resultSet.getDouble("billing");
                tests.add(new Test(id, patientID, appointmentID, description, billing));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tests;
    }

    private Map<String, Float> fetchAvgBillings() {
        Map<String, Float> avgBillingMap = new HashMap<>();
        String avgBillingQuery = "SELECT description, AVG(billing) as average_billing FROM test WHERE p_id = ? GROUP BY description HAVING AVG(billing) > 0;";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement avgBillingStatement = connection.prepareStatement(avgBillingQuery)) {

            avgBillingStatement.setInt(1, this.patientID);
            ResultSet resultSet = avgBillingStatement.executeQuery();

            while (resultSet.next()) {
                String testType = resultSet.getString("description");
                float averageBilling = resultSet.getFloat("average_billing");
                avgBillingMap.put(testType, averageBilling);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avgBillingMap;
    }

    private float fetchTotalBillings() {
        float totalBilling = 0;
        String totalBillingQuery = "SELECT SUM(billing) as total_billing FROM test WHERE p_id = ?;";

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

    public void goToOperations() {
        new PastOperations(patient).setVisible(true);
        this.dispose();
    }

    public void goToAppointment() {
        new MyAppointments(patient).setVisible(true);
        this.dispose();
    }
}
