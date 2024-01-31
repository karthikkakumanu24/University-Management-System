import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

public class ComplaintsAdminSide extends JFrame {
    private JTextField subjectField, detailsField, locationField;
    private DefaultTableModel tableModel;
    private JTable table;
    private Connection connection;
    private Statement statement;

    public ComplaintsAdminSide() {
        super("Complaint Administration");
        subjectField = new JTextField(20);
        detailsField = new JTextField(20);
        locationField = new JTextField(20);

        JButton addButton = new JButton("Register Complaint");
        JButton displayButton = new JButton("Display Complaints");
        JButton completeButton = new JButton("Set as Completed");
        JButton editButton = new JButton("Edit Complaint");

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Complaint Subject"));
        inputPanel.add(subjectField);
        inputPanel.add(new JLabel("Complaint Description"));
        inputPanel.add(detailsField);
        inputPanel.add(new JLabel("Location"));
        inputPanel.add(locationField);
        inputPanel.add(addButton);
        inputPanel.add(displayButton);
        inputPanel.add(completeButton);
        inputPanel.add(editButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addComplaint();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayComplaints();
            }
        });

        completeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setComplaintCompleted();
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editComplaint();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "root", "toor");
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableModel.setColumnIdentifiers(new Object[] { "Subject", "Description", "Location", "Completed" });
    }

    private void addComplaint() {
        String subject = subjectField.getText();
        String description = detailsField.getText();
        String location = locationField.getText();
        try {
            String query = "INSERT INTO FAQ (Subject, Details, Location, Completion) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, subject);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, location);
            preparedStatement.setBoolean(4, false);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Complaint registered successfully");
            subjectField.setText("");
            detailsField.setText("");
            locationField.setText("");
            displayComplaints();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error registering complaint");
        }
    }

    private void displayComplaints() {
        try {
            String query = "SELECT * FROM FAQ";
            ResultSet resultSet = statement.executeQuery(query);
            tableModel.setRowCount(0);
            while (resultSet.next()) {
                String subject = resultSet.getString("Subject");
                String description = resultSet.getString("Details");
                String location = resultSet.getString("Location");
                boolean completed =resultSet.getBoolean("Completion");
                tableModel.addRow(new Object[]{subject, description, location, completed});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error displaying complaints");
        }
    }

    private void setComplaintCompleted() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "No complaint selected");
            return;
        }

        String subject = (String) table.getValueAt(selectedRow, 0);
        try {
            String query = "UPDATE FAQ SET Completion = ? WHERE Subject = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setString(2, subject);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Complaint marked as completed");
            displayComplaints();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error marking complaint as completed");
        }
    }

    private void editComplaint() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "No complaint selected");
            return;
        }

        String subject = (String) table.getValueAt(selectedRow, 0);
        String description = (String) table.getValueAt(selectedRow, 1);
        String location = (String) table.getValueAt(selectedRow, 2);

        String newSubject = JOptionPane.showInputDialog(this, "Enter new subject:", subject);
        String newDescription = JOptionPane.showInputDialog(this, "Enter new description:", description);
        String newLocation = JOptionPane.showInputDialog(this, "Enter new location:", location);

        try {
            String query = "UPDATE FAQ SET Subject = ?, Details = ?, Location = ? WHERE Subject = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newSubject);
            preparedStatement.setString(2, newDescription);
            preparedStatement.setString(3, newLocation);
            preparedStatement.setString(4, subject);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Complaint updated successfully");
            displayComplaints();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating complaint");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HostelRegistration hostelRegistration = new HostelRegistration();
            hostelRegistration.setVisible(true);
            HostelStudentTable table = new HostelStudentTable();
            table.setVisible(true);
        });
    }
}