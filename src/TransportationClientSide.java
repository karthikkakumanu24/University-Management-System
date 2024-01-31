
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

class TransportationClientSide extends JFrame {
    private JTextField nameField;
    private JTextField rollNumberField;
    private JTextField routeField;
    private JTextField vehicleNumberField;
    private JButton registerButton;
    private JButton displayButton;

    private Connection connection;
    private PreparedStatement insertStatement;
    private PreparedStatement displayStatement;

    public TransportationClientSide() {
        InitializeDatabase initializeDatabase = new InitializeDatabase();
        initializeDatabase.getDatabase();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Transport Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel nameLabel = new JLabel("Name:");
        JLabel rollNumberLabel = new JLabel("Roll Number:");
        JLabel routeLabel = new JLabel("Route:");
        JLabel vehicleNumberLabel = new JLabel("Vehicle Number:");
        nameField = new JTextField(20);
        rollNumberField = new JTextField(20);
        routeField = new JTextField(20);
        vehicleNumberField = new JTextField(20);
        registerButton = new JButton("Register");
        displayButton = new JButton("Display Transport Details");

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayTransportDetails();
            }
        });

        add(nameLabel);
        add(nameField);
        add(rollNumberLabel);
        add(rollNumberField);
        add(routeLabel);
        add(routeField);
        add(vehicleNumberLabel);
        add(vehicleNumberField);
        add(registerButton);
        add(displayButton);

        pack();
        setVisible(true);
    }

    private void register() {
        String name = nameField.getText();
        String rollNumber = rollNumberField.getText();
        String route = routeField.getText();
        String vehicleNumber = vehicleNumberField.getText();

        try {
            insertStatement.setString(1, name);
            insertStatement.setString(2, rollNumber);
            insertStatement.setString(3, route);
            insertStatement.setString(4, vehicleNumber);
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

    private void displayTransportDetails() {
        try {
            ResultSet resultSet = displayStatement.executeQuery();

            StringBuilder details = new StringBuilder();
            details.append("Transport Details:\n");

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String rollNumber = resultSet.getString("roll_number");
                String route = resultSet.getString("route");
                String vehicleNumber = resultSet.getString("vehicle_number");

                details.append("Name: ").append(name).append(", ");
                details.append("Roll Number: ").append(rollNumber).append(", ");
                details.append("Route: ").append(route).append(", ");
                details.append("Vehicle Number: ").append(vehicleNumber).append("\n");
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
        routeField.setText("");
        vehicleNumberField.setText("");
    }
}

class TransportStudentTable extends JFrame {
    private JTable table;

    public TransportStudentTable() {
        setTitle("Transport Student Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Create table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Roll Number");
        model.addColumn("Route");
        model.addColumn("Vehicle Number");

        // Connect to the database and fetch data
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/transport", "username", "password");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name, rollnumber, route, vehiclenumber FROM students");

            // Populate the table model with data from the database
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int rollNumber = resultSet.getInt("rollnumber");
                String route = resultSet.getString("route");
                String vehicleNumber = resultSet.getString("vehiclenumber");

                model.addRow(new Object[]{name, rollNumber, route, vehicleNumber});
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create and configure the table
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(500, 300));
        table.setFillsViewportHeight(true);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
}
