package sql;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import objects.DatabaseConnection;
import objects.Patient;

public class Vaccine extends JFrame implements ActionListener {
    JPanel topPanel;
    JPanel mainPanel;
    JPanel formPanel;
    JPanel buttonPanel;
    JPanel tablePanel;

    JLabel topText;

    JLabel descriptionLabel, vacNameLabel, doseLabel, statLabel, billingLabel;
    JTextField descriptionField, doseField, billingField, statField;
    JComboBox<String> vacNameField;
    JButton insertButton, updateButton, deleteButton, viewButton, confirmInsertButton, vaccineBillingsButton;
    JTextArea resultArea;

    JTable vaccineTable;
    DefaultTableModel tableModel;

    Font font;
    Patient patient;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                // Create a sample Patient object for testing
                Patient patient = null;
                Vaccine frame = new Vaccine(patient);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Vaccine(Patient patient) {
        this.patient = patient;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Add settings menu with patient name and surname
        JMenuBar menuBar = new JMenuBar();
        JMenu settingsMenu = new JMenu(patient.getName() + " " + patient.getSurname());
        JMenuItem homeMenuItem = new JMenuItem("Home Page");
        JMenuItem profileMenuItem = new JMenuItem("Profile Page");
        JMenuItem logoutMenuItem = new JMenuItem("Log out");

        homeMenuItem.addActionListener(e -> goToHomePage());
        profileMenuItem.addActionListener(e -> goToProfilePage());
        logoutMenuItem.addActionListener(e -> logout());

        settingsMenu.add(homeMenuItem);
        settingsMenu.add(profileMenuItem);
        settingsMenu.add(logoutMenuItem);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(settingsMenu);

        setJMenuBar(menuBar);

        topPanel = new JPanel();
        mainPanel = new JPanel();
        formPanel = new JPanel();
        buttonPanel = new JPanel();
        tablePanel = new JPanel();

        topPanel.setPreferredSize(new Dimension(800, 100));
        topPanel.setBackground(Color.decode("#00008B"));
        topPanel.setLayout(new GridLayout(2, 1, 0, 0));

        topText = new JLabel("Vaccine Management", SwingConstants.CENTER);
        font = new Font("Objektiv Mk1", Font.BOLD, 40);
        topText.setFont(font);
        topText.setForeground(Color.decode("#A9A9A9"));
        topText.setBorder(new EmptyBorder(20, 0, 0, 0));
        topPanel.add(topText);

        formPanel.setLayout(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(Color.white);

        descriptionLabel = new JLabel("Description:");
        vacNameLabel = new JLabel("Vaccine Name:");
        doseLabel = new JLabel("Dose:");
        statLabel = new JLabel("Status:");
        billingLabel = new JLabel("Billing:");

        descriptionField = new JTextField();
        doseField = new JTextField();
        billingField = new JTextField();
        statField = new JTextField("pending");
        statField.setEditable(false);
        vacNameField = new JComboBox<>();

        formPanel.add(descriptionLabel);
        formPanel.add(descriptionField);
        formPanel.add(vacNameLabel);
        formPanel.add(vacNameField);
        formPanel.add(doseLabel);
        formPanel.add(doseField);
        formPanel.add(statLabel);
        formPanel.add(statField);
        formPanel.add(billingLabel);
        formPanel.add(billingField);

        buttonPanel.setLayout(new GridLayout(1, 5, 10, 10));
        buttonPanel.setBackground(Color.white);

        insertButton = new JButton("Insert");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        viewButton = new JButton("View");
        vaccineBillingsButton = new JButton("Vaccine Billings");
        confirmInsertButton = new JButton("Confirm Insert");

        insertButton.setFont(new Font("Objektiv Mk1", Font.BOLD, 20));
        insertButton.setBackground(Color.white);
        insertButton.setForeground(Color.decode("#00008B"));
        insertButton.setPreferredSize(new Dimension(150, 50));
        insertButton.setFocusPainted(false);

        updateButton.setFont(new Font("Objektiv Mk1", Font.BOLD, 20));
        updateButton.setBackground(Color.white);
        updateButton.setForeground(Color.decode("#00008B"));
        updateButton.setPreferredSize(new Dimension(150, 50));
        updateButton.setFocusPainted(false);
        updateButton.setEnabled(false); // Initially disabled

        deleteButton.setFont(new Font("Objektiv Mk1", Font.BOLD, 20));
        deleteButton.setBackground(Color.white);
        deleteButton.setForeground(Color.decode("#00008B"));
        deleteButton.setPreferredSize(new Dimension(150, 50));
        deleteButton.setFocusPainted(false);

        viewButton.setFont(new Font("Objektiv Mk1", Font.BOLD, 20));
        viewButton.setBackground(Color.white);
        viewButton.setForeground(Color.decode("#00008B"));
        viewButton.setPreferredSize(new Dimension(150, 50));
        viewButton.setFocusPainted(false);
        viewButton.setEnabled(false);  // Initially disabled

        vaccineBillingsButton.setFont(new Font("Objektiv Mk1", Font.BOLD, 20));
        vaccineBillingsButton.setBackground(Color.white);
        vaccineBillingsButton.setForeground(Color.decode("#00008B"));
        vaccineBillingsButton.setPreferredSize(new Dimension(150, 50));
        vaccineBillingsButton.setFocusPainted(false);

        confirmInsertButton.setFont(new Font("Objektiv Mk1", Font.BOLD, 20));
        confirmInsertButton.setBackground(Color.white);
        confirmInsertButton.setForeground(Color.decode("#00008B"));
        confirmInsertButton.setPreferredSize(new Dimension(150, 50));
        confirmInsertButton.setFocusPainted(false);

        insertButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        viewButton.addActionListener(this);
        vaccineBillingsButton.addActionListener(this);
        confirmInsertButton.addActionListener(this);

        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(vaccineBillingsButton);
        buttonPanel.add(viewButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Objektiv Mk1", Font.PLAIN, 16));
        resultArea.setBackground(Color.white);
        resultArea.setForeground(Color.decode("#00008B"));

        tableModel = new DefaultTableModel();
        vaccineTable = new JTable(tableModel);
        vaccineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(vaccineTable);

        tableModel.addColumn("Vaccine Name");
        tableModel.addColumn("Dose");
        tableModel.addColumn("Status");

        // Add a listener to enable the View and Update buttons only when a row is selected
        vaccineTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int selectedRow = vaccineTable.getSelectedRow();
                viewButton.setEnabled(selectedRow != -1);
                String status = tableModel.getValueAt(selectedRow, 2).toString();
                updateButton.setEnabled(selectedRow != -1 && !status.equalsIgnoreCase("administered"));
            }
        });

        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        loadVaccineData();
        loadComboBoxData();
    }

	@Override
    public void actionPerformed(ActionEvent e) {
        Connection connection=null;
		try {
			connection = DatabaseConnection.getConnection();
			if (e.getSource() == insertButton) {
	            resetFormFields();
	            formPanel.setVisible(true);
	            setFieldsEditable(true);
	            mainPanel.add(formPanel, BorderLayout.SOUTH);
	            mainPanel.revalidate();
	            mainPanel.repaint();
	        } else if (e.getSource() == confirmInsertButton) {
	            insertRecord(connection);
	            formPanel.setVisible(false);
	            mainPanel.remove(formPanel);
	            mainPanel.revalidate();
	            mainPanel.repaint();
	        } else if (e.getSource() == updateButton) {
	            showDetailedVaccineInfo(connection);
	            setFieldsEditable(true);
	            mainPanel.add(formPanel, BorderLayout.SOUTH);
	            formPanel.setVisible(true);
	            mainPanel.revalidate();
	            mainPanel.repaint();
	        } else if (e.getSource() == deleteButton) {
	            deleteRecord(connection);
	        } else if (e.getSource() == viewButton) {
	            showDetailedVaccineInfo(connection);
	            setFieldsEditable(false);
	            mainPanel.add(formPanel, BorderLayout.SOUTH);
	            formPanel.setVisible(true);
	            mainPanel.revalidate();
	            mainPanel.repaint();
	        } else if (e.getSource() == vaccineBillingsButton) {
	            showVaccineBillings(connection);
	        }
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        if (connection == null) {
            resultArea.setText("Database connection failed!");
            return;
        }

       
    }

//    public Connection getConnection() {
//        try {
//            return DriverManager.getConnection(url, "root", "school!278A");
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    private void insertRecord(Connection connection) {
        String sql = "INSERT INTO vaccine (p_id, description, vac_name, dose, stat, billing) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patient.getId());
            ps.setString(2, descriptionField.getText());
            ps.setString(3, vacNameField.getSelectedItem().toString());
            ps.setInt(4, Integer.parseInt(doseField.getText()));
            ps.setString(5, "Pending");
            ps.setFloat(6, Float.parseFloat(billingField.getText()));
            int rowsInserted = ps.executeUpdate();
            resultArea.setText(rowsInserted + " record(s) inserted.");
            loadVaccineData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            resultArea.setText("Error inserting record: " + ex.getMessage());
        }
    }

    private void updateRecord(Connection connection) {
        int selectedRow = vaccineTable.getSelectedRow();
        if (selectedRow != -1) {
            String oldVaccineName = tableModel.getValueAt(selectedRow, 0).toString();
            int oldDose = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());

            String sql = "UPDATE vaccine SET description = ?, vac_name = ?, dose = ?, stat = ?, billing = ? WHERE p_id = ? AND vac_name = ? AND dose = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, descriptionField.getText());
                ps.setString(2, vacNameField.getSelectedItem().toString());
                ps.setInt(3, Integer.parseInt(doseField.getText()));
                ps.setString(4, statField.getText());
                ps.setFloat(5, Float.parseFloat(billingField.getText()));
                ps.setInt(6, patient.getId());
                ps.setString(7, oldVaccineName);
                ps.setInt(8, oldDose);
                int rowsUpdated = ps.executeUpdate();
                resultArea.setText(rowsUpdated + " record(s) updated.");
                loadVaccineData();
            } catch (SQLException ex) {
                ex.printStackTrace();
                resultArea.setText("Error updating record: " + ex.getMessage());
            }
        }
    }

    private void deleteRecord(Connection connection) {
        int selectedRow = vaccineTable.getSelectedRow();
        if (selectedRow != -1) {
            String vaccineName = tableModel.getValueAt(selectedRow, 0).toString();
            int dose = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());

            String sql = "DELETE FROM vaccine WHERE p_id = ? AND vac_name = ? AND dose = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, patient.getId());
                ps.setString(2, vaccineName);
                ps.setInt(3, dose);
                int rowsDeleted = ps.executeUpdate();
                resultArea.setText(rowsDeleted + " record(s) deleted.");
                loadVaccineData();
            } catch (SQLException ex) {
                ex.printStackTrace();
                resultArea.setText("Error deleting record: " + ex.getMessage());
            }
        }
    }

    private void loadVaccineData() {
        tableModel.setRowCount(0);
        String sql = "SELECT vac_name, dose, stat " +
                "FROM vaccine " +
                "WHERE p_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patient.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getString("vac_name"),
                            rs.getInt("dose"),
                            rs.getString("stat")
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            resultArea.setText("Error retrieving records: " + ex.getMessage());
        }
    }

    private void showDetailedVaccineInfo(Connection connection) {
        int selectedRow = vaccineTable.getSelectedRow();
        if (selectedRow != -1) {
            String vaccineName = tableModel.getValueAt(selectedRow, 0).toString();
            int dose = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());

            String sql = "SELECT description, stat, billing FROM vaccine WHERE p_id = ? AND vac_name = ? AND dose = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, patient.getId());
                ps.setString(2, vaccineName);
                ps.setInt(3, dose);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        descriptionField.setText(rs.getString("description"));
                        vacNameField.setSelectedItem(vaccineName);
                        doseField.setText(String.valueOf(dose));
                        statField.setText(rs.getString("stat"));
                        billingField.setText(String.valueOf(rs.getFloat("billing")));

                        formPanel.setVisible(true);
                        mainPanel.add(formPanel, BorderLayout.SOUTH);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                resultArea.setText("Error retrieving detailed information: " + ex.getMessage());
            }
        }
    }

    private void showVaccineBillings(Connection connection) {
        String sql = "SELECT vac_name, SUM(billing) as total_billing " +
                "FROM vaccine " +
                "WHERE p_id = ? " +
                "GROUP BY vac_name";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patient.getId());
            try (ResultSet rs = ps.executeQuery()) {
                JFrame billingFrame = new JFrame("Vaccine Billings");
                billingFrame.setSize(400, 300);
                billingFrame.setLayout(new BorderLayout());

                DefaultTableModel billingTableModel = new DefaultTableModel();
                JTable billingTable = new JTable(billingTableModel);
                JScrollPane billingScrollPane = new JScrollPane(billingTable);

                billingTableModel.addColumn("Vaccine Name");
                billingTableModel.addColumn("Total Billing");

                while (rs.next()) {
                    billingTableModel.addRow(new Object[]{
                            rs.getString("vac_name"),
                            rs.getFloat("total_billing")
                    });
                }

                billingFrame.add(billingScrollPane, BorderLayout.CENTER);
                billingFrame.setVisible(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            resultArea.setText("Error retrieving vaccine billings: " + ex.getMessage());
        }
    }

    private void resetFormFields() {
        descriptionField.setText("");
        doseField.setText("");
        billingField.setText("");
        vacNameField.setSelectedIndex(-1);
        statField.setText("Pending");
    }

    private void setFieldsEditable(boolean editable) {
        descriptionField.setEditable(editable);
        vacNameField.setEnabled(editable);
        doseField.setEditable(editable);
        statField.setEditable(false); // Always uneditable, set to "Pending"
        billingField.setEditable(editable);
    }

    private void loadComboBoxData() {
        vacNameField.removeAllItems();

        String vacSql = "SELECT DISTINCT vac_name FROM vaccine";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement vacPs = connection.prepareStatement(vacSql);
             ResultSet vacRs = vacPs.executeQuery()) {

            while (vacRs.next()) {
                vacNameField.addItem(vacRs.getString("vac_name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            resultArea.setText("Error loading combobox data: " + ex.getMessage());
        }
    }

    private void goToHomePage() {
        // Code to navigate to the HomePage class
        new HomePage(patient).setVisible(true);
        this.dispose();
    }


    private void logout() {
        // Code to navigate to the Login class
        new LogIn().setVisible(true);
        this.dispose();
    }
    
    private void goToProfilePage() {
		// TODO Auto-generated method stub
    	new PatientProfile(patient).setVisible(true);
        this.dispose();		
	}
}
