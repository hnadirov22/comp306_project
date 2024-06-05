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
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.Color;
import java.awt.Dimension;



public class TestResults extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable resultsTable;
    private int patientID;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TestResults frame = new TestResults(null);
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

        JPanel headerLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerLabelPanel.setBackground(Color.decode("#00008B"));
        JLabel headerLabel = new JLabel("Test Results");
        headerLabel.setFont(new Font("Objektiv Mk1", Font.BOLD, 20));
        headerLabel.setForeground(Color.white);
        headerLabelPanel.add(headerLabel);
        headerPanel.add(headerLabelPanel);

        headerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Adding whitespace between Test Results and Patient name

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

//        JPanel actionsPanel = new JPanel();
//        contentPane.add(actionsPanel, BorderLayout.SOUTH);
//        actionsPanel.setLayout(new BorderLayout(0, 0));
//
//        JButton closeButton = new JButton("Done");
//        closeButton.addActionListener(e -> dispose());
//        actionsPanel.add(closeButton, BorderLayout.EAST);
        JMenuClass menuItem = new JMenuClass(this,patient);
        setJMenuBar(menuItem.getMenuBar());
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
}
