import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;


class HostelRegistration extends JFrame {
    private JTextField nameField;
    private JTextField rollNumberField;
    private JTextField towerNumberField;
    private JTextField roomNumberField;

    private Connection connection;
    private PreparedStatement insertStatement;
    private PreparedStatement displayStatement;

    public HostelRegistration() {
        InitializeDatabase initializeDatabase = new InitializeDatabase();
        initializeDatabase.getDatabase();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Hostel Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel nameLabel = new JLabel("Name:");
        JLabel rollNumberLabel = new JLabel("Roll Number:");
        JLabel towerNumberLabel = new JLabel("Tower Number:");
        JLabel roomNumberLabel = new JLabel("Room Number:");
        nameField = new JTextField(20);
        rollNumberField = new JTextField(20);
        towerNumberField = new JTextField(20);
        roomNumberField = new JTextField(20);
        JButton registerButton = new JButton("Register");
        JButton displayButton = new JButton("Display Hostel Details");

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayHostelDetails();
            }
        });

        add(nameLabel);
        add(nameField);
        add(rollNumberLabel);
        add(rollNumberField);
        add(towerNumberLabel);
        add(towerNumberField);
        add(roomNumberLabel);
        add(roomNumberField);
        add(registerButton);
        add(displayButton);

        pack();
        setVisible(true);
    }

    private void register() {
        String name = nameField.getText();
        String rollNumber = rollNumberField.getText();
        String towerNumber = towerNumberField.getText();
        String roomNumber = roomNumberField.getText();

        try {
            insertStatement.setString(1, name);
            insertStatement.setString(2, rollNumber);
            insertStatement.setString(3, towerNumber);
            insertStatement.setString(4, roomNumber);
            int rowsAffected = insertStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayHostelDetails() {
        try {
            ResultSet resultSet = displayStatement.executeQuery();

            StringBuilder details = new StringBuilder();
            details.append("Hostel Details:\n");

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String rollNumber = resultSet.getString("roll_number");
                String towerNumber = resultSet.getString("tower_number");
                String roomNumber = resultSet.getString("room_number");

                details.append("Name: ").append(name).append(", ");
                details.append("Roll Number: ").append(rollNumber).append(", ");
                details.append("Tower Number: ").append(towerNumber).append(", ");
                details.append("Room Number: ").append(roomNumber).append("\n");
            }

            JOptionPane.showMessageDialog(this, details.toString());
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.setText("");
        rollNumberField.setText("");
        towerNumberField.setText("");
        roomNumberField.setText("");
    }
}


class HostelStudentTable extends JFrame {

    public HostelStudentTable() {
        setTitle("Hostel Student Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Create table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Roll Number");
        model.addColumn("Tower Number");
        model.addColumn("Room Number");

        // Connect to the database and fetch data
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel", "username", "password");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name, rollnumber, towernumber, roomnumber FROM students");

            // Populate the table model with data from the database
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int rollNumber = resultSet.getInt("rollnumber");
                int towerNumber = resultSet.getInt("towernumber");
                int roomNumber = resultSet.getInt("roomnumber");

                model.addRow(new Object[]{name, rollNumber, towerNumber, roomNumber});
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create and configure the table
        JTable table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(500, 300));
        table.setFillsViewportHeight(true);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }


}