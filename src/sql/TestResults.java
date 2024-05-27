package sql;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import objects.Patient;
import objects.DatabaseConnection;
import objects.Test;
import objects.TestTableModel;

import javax.swing.BoxLayout;
import java.awt.FlowLayout;
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

public class TestResults extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable resultsTable;
	private int patientID;

	

	public TestResults(Patient patient) {
        this.patientID = patient.getId();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel headerPanel = new JPanel();
        contentPane.add(headerPanel, BorderLayout.NORTH);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JPanel headerLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("Test Results");
        headerLabelPanel.add(headerLabel);
        headerPanel.add(headerLabelPanel);

        JPanel patientNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel patientNameLabel = new JLabel("Patient: " + patient.getName() + " " + patient.getSurname());
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

        JPanel avgBillingPanel = new JPanel();
        contentPane.add(avgBillingPanel, BorderLayout.EAST);
        avgBillingPanel.setLayout(new BoxLayout(avgBillingPanel, BoxLayout.Y_AXIS));

        JLabel avgBillingHeader = new JLabel("Average Billings:");
        avgBillingPanel.add(avgBillingHeader);

        Map<String, Float> avgBillingMap = fetchAvgBillings();
        for (Map.Entry<String, Float> entry : avgBillingMap.entrySet()) {
            JLabel label = new JLabel(entry.getKey() + ": " + entry.getValue());
            avgBillingPanel.add(label);
        }

        JPanel actionsPanel = new JPanel();
        contentPane.add(actionsPanel, BorderLayout.SOUTH);
        actionsPanel.setLayout(new BorderLayout(0, 0));

        JButton closeButton = new JButton("Done");
        closeButton.addActionListener(e -> dispose());
        actionsPanel.add(closeButton, BorderLayout.EAST);
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
	
	
		

				
	

}
