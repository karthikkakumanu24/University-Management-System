import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

class LibraryClientSide extends JFrame {
    private final JTextField searchField;
    private final DefaultTableModel tableModel;

    private Statement statement;

    public LibraryClientSide() {
        super("Library Management System");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton show_allButton=new JButton("Show all");
        tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);

        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Search by Title:"));
        inputPanel.add(searchField);
        inputPanel.add(searchButton);
        inputPanel.add(show_allButton);
        inputPanel.add(new JScrollPane(table));

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);


        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchBooks();
            }
        });
        show_allButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showBooks();
            }
        });



        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1900, 800);
        setVisible(true);


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "root", "toor");
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tableModel.setColumnIdentifiers(new Object[]{"Book_Name", "Author"});
    }
    private void showBooks(){
        try{
            String query="SELECT * from books";
            ResultSet resultSet=statement.executeQuery(query);
            tableModel.setRowCount(0);
            while (resultSet.next()) {
                String title = resultSet.getString("Book_Name");
                String author = resultSet.getString("Author");
                tableModel.addRow(new Object[]{title, author});
            }
        }catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching books!");
        }
    }
    private void searchBooks() {
        String keyword = searchField.getText();

        try {
            String query = "SELECT * FROM books WHERE Book_Name LIKE '%" + keyword + "%'";
            ResultSet resultSet = statement.executeQuery(query);
            tableModel.setRowCount(0);
            while (resultSet.next()) {
                String title = resultSet.getString("Book_Name");
                String author = resultSet.getString("Author");
                tableModel.addRow(new Object[]{title, author});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching books!");
        }
    }

    public static void main(String[] args) {
            try {

                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new LibraryClientSide();
                    }
                });
            } catch (ClassNotFoundException | InstantiationException |
                     IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        }
}