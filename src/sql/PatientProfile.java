package sql;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import objects.DatabaseConnection;
import objects.Patient;

import java.sql.*;
import java.util.Vector;
import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

import javax.swing.*;
import java.sql.*;
import java.awt.*;

import javax.swing.*;
import java.sql.*;
import java.awt.*;

import javax.swing.*;
import java.sql.*;
import java.awt.*;

public class PatientProfile extends JFrame {
    private Patient patient;

    private JTextField nameField, surnameField, genderField, ageField;
    private JButton editButton, saveButton;

    public PatientProfile(Patient patient) {
        super("Patient Profile");
        this.patient = patient;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        try {
            initializeUI();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error initializing UI: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeUI() throws ClassNotFoundException, SQLException {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.BLUE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        editButton = new JButton("Edit Profile");
        saveButton = new JButton("Save Changes");
        saveButton.setVisible(false);  // Initially hide the save button

        editButton.addActionListener(this::enableEditing);
        saveButton.addActionListener(this::saveProfile);

        buttonPanel.add(editButton);
        buttonPanel.add(saveButton);

        try (Connection con = DatabaseConnection.getConnection()) {
            PreparedStatement pst = con.prepareStatement("SELECT p_name, surname, gender, age FROM patient WHERE id = ?");
            pst.setInt(1, patient.getId());
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                nameField = new JTextField(rs.getString("p_name"), 15);
                surnameField = new JTextField(rs.getString("surname"), 15);
                genderField = new JTextField(rs.getString("gender"), 15);
                ageField = new JTextField(String.valueOf(rs.getInt("age")), 15);

                nameField.setEditable(false);
                surnameField.setEditable(false);
                genderField.setEditable(false);
                ageField.setEditable(false);

                infoPanel.add(createLabelPanel("Name:", nameField));
                infoPanel.add(createLabelPanel("Surname:", surnameField));
                infoPanel.add(createLabelPanel("Gender:", genderField));
                infoPanel.add(createLabelPanel("Age:", ageField));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void enableEditing(ActionEvent e) {
        nameField.setEditable(true);
        surnameField.setEditable(true);
        genderField.setEditable(true);
        ageField.setEditable(true);
        saveButton.setVisible(true);  // Show the save button
        editButton.setVisible(false);  // Hide the edit button
    }

    private void saveProfile(ActionEvent e) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String updateSql = "UPDATE patient SET p_name = ?, surname = ?, gender = ?, age = ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(updateSql);
            pst.setString(1, nameField.getText());
            pst.setString(2, surnameField.getText());
            pst.setString(3, genderField.getText());
            pst.setInt(4, Integer.parseInt(ageField.getText()));
            pst.setInt(5, patient.getId());

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update profile.", "Update Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } 
        disableEditing();
    }

    private void disableEditing() {
        nameField.setEditable(false);
        surnameField.setEditable(false);
        genderField.setEditable(false);
        ageField.setEditable(false);
        saveButton.setVisible(false);  // Hide the save button
        editButton.setVisible(true);  // Show the edit button
    }

    private JPanel createLabelPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setForeground(new Color(0, 0, 139)); // Dark blue
        label.setFont(new Font("Arial", Font.BOLD, 12));

        textField.setForeground(Color.DARK_GRAY);
        textField.setFont(new Font("Arial", Font.PLAIN, 12));

        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PatientProfile(null).setVisible(true));
    }
}
