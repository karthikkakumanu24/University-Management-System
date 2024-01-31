import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

public class ComplaintsClientSide extends JFrame{
    private final JTextField SubjectField;
    private final JTextField DetailsField;
    private final JTextField LocationField;
    private final DefaultTableModel tableModel;
    private Connection connection;
    private Statement statement;
    public ComplaintsClientSide(){
        super("Complaint Registration");
        SubjectField=new JTextField(20);
        DetailsField=new JTextField(20);
        LocationField=new JTextField(20);

        JButton addButton=new JButton("Register Complaint");
        JButton displayButton=new JButton("Display Complaints");
        tableModel=new DefaultTableModel();
        JTable table = new JTable(tableModel);
        setLayout(new BorderLayout());

        JPanel inputPanel=new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Complaint Subject"));
        inputPanel.add(SubjectField);
        inputPanel.add(new JLabel("Complaint Description"));
        inputPanel.add(DetailsField);
        inputPanel.add(new JLabel("Location"));
        inputPanel.add(LocationField);
        inputPanel.add(addButton);
        inputPanel.add(displayButton);

        add(inputPanel,BorderLayout.NORTH);
        add(new JScrollPane(table),BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addComplaint();
            }
        });
        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayComplaint();
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1900,1000);
        setVisible(true);

        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc","root","toor");
            statement=connection.createStatement();
        }catch(Exception e){
            e.printStackTrace();
        }
        tableModel.setColumnIdentifiers(new Object[]{"Subject","Description","Location","Completed"});
    }
    private void addComplaint() {
        String subject = SubjectField.getText();
        String description = DetailsField.getText();
        String location = LocationField.getText();
        try {
            String query = "INSERT INTO FAQ (Subject, Details, Location, Completion) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, subject);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, location);
            preparedStatement.setBoolean(4, false);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Complaint registered successfully");
            SubjectField.setText("");
            DetailsField.setText("");
            LocationField.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error registering Complaint");
        }
    }


    private void displayComplaint() {
        try {
            String query = "SELECT * FROM FAQ";
            // Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            tableModel.setRowCount(0);
            while (resultSet.next()) {
                String subject = resultSet.getString("Subject");
                String description = resultSet.getString("Details");
                String location = resultSet.getString("Location");
                tableModel.addRow(new Object[]{subject, description, location});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error displaying Complaints!");
        }
    }


    public static void main(String[] args) {
        try {

            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new ComplaintsClientSide();
                }
            });
        } catch (ClassNotFoundException | InstantiationException |
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

}