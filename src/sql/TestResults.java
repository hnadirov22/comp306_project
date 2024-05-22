package sql;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

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
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class TestResults extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable resultsTable;
	private int patientID;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					TestResults frame = new TestResults();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public TestResults(int patientID) {
		this.patientID = patientID;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel headerPanel = new JPanel();
		contentPane.add(headerPanel, BorderLayout.NORTH);
		headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel HeaderLabel = new JLabel("Test Results");
		headerPanel.add(HeaderLabel);
		
		JPanel infoPanel = new JPanel();
		contentPane.add(infoPanel, BorderLayout.WEST);
		infoPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane resultsPane = new JScrollPane();
		contentPane.add(resultsPane, BorderLayout.CENTER);
		
		List<Test> testList = fetchResults();
        TestTableModel model = new TestTableModel(testList);
        resultsTable = new JTable(model);
        resultsPane.setViewportView(resultsTable);
		
		resultsTable = new JTable();
		contentPane.add(resultsTable, BorderLayout.SOUTH);
		
		JPanel actionsPanel = new JPanel();
		contentPane.add(actionsPanel, BorderLayout.SOUTH);
		actionsPanel.setLayout(new BorderLayout(0, 0));
		
		JButton closeButton = new JButton("Done");
		actionsPanel.add(closeButton, BorderLayout.EAST);
	}
	
	private List<Test> fetchResults() {
		List<Test> tests = new ArrayList<>();
		String sqlQuery = "";
		try (Connection connection = DatabaseConnection.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
	             ResultSet resultSet = preparedStatement.executeQuery()) {

	            while (resultSet.next()) {
	            	int id = resultSet.getInt("id");
                    int patientID = resultSet.getInt("patientID");
                    int appointmentID = resultSet.getInt("appointmentID");
                    String description = resultSet.getString("description");
                    double billing = resultSet.getDouble("billing");
                    tests.add(new Test(id, patientID, appointmentID, description, billing));
	            }
	    
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return tests;
	}

}
