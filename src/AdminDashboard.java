import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Objects;

public class AdminDashboard {
    AdminDashboard() {
        JFrame homeFrame = new JFrame("Admin Dashboard");
        homeFrame.setLayout(null);
        homeFrame.setSize(1000, 800);
        homeFrame.setLocationRelativeTo(null);
        homeFrame.getContentPane().setBackground(Color.WHITE);
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color primaryColor = new Color(57, 108, 103);
        Font textTheme = new Font("Verdana", Font.PLAIN, 16);

        // Create a dropdown list of ingredients
        JComboBox<String> ingredientList = new JComboBox<>();
        ingredientList.setBounds(350, 50, 300, 30);
        ingredientList.setForeground(Color.GRAY);
        ingredientList.setFont(textTheme);
        homeFrame.add(ingredientList);

        // Create a text field for the quantity
        JTextField quantityField = new HintTextField("Quantity");
        quantityField.setBounds(350, 100, 300, 30);
        quantityField.setForeground(Color.GRAY);
        quantityField.setFont(textTheme);
        quantityField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        homeFrame.add(quantityField);

        // Create a button to update the quantity
        JButton updateButton = new JButton("Update");
        updateButton.setBounds(350, 150, 300, 30);
        updateButton.setForeground(Color.WHITE);
        updateButton.setBackground(primaryColor);
        updateButton.setFont(textTheme);
        updateButton.setOpaque(true);
        updateButton.setBorderPainted(false);
        homeFrame.add(updateButton);

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Username", "Product Names", "Total Price", "Payment Method", "Status"}, 0);

        // Create the table
        JTable table = new JTable(model);
        table.getTableHeader().setFont(textTheme);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setBackground(primaryColor);
        table.getTableHeader().setOpaque(false);
        table.setRowHeight(30);
        table.setFont(textTheme);
        table.setForeground(Color.GRAY);
        table.setBackground(Color.WHITE);
        table.setShowGrid(false);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setGridColor(Color.BLACK);
        table.setFillsViewportHeight(true);


        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(850, 10, 100, 30);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(primaryColor);
        logoutButton.setFont(textTheme);
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        homeFrame.add(logoutButton);

        logoutButton.addActionListener(e -> {
            homeFrame.dispose();
            new Login();
        });


        try {
            Class.forName(Constants.jdbcClass);
            Connection con = DriverManager.getConnection(Constants.connectionAddress, Constants.databaseUser, Constants.databasePassword);
            // Create a statement to execute the SQL query
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT o.id, u.username, GROUP_CONCAT(p.name) AS product_names, SUM(oi.quantity * p.price) AS total_price, o.payment_method, o.status " +
                            "FROM orders o " +
                            "INNER JOIN users u ON o.user_id = u.id " +
                            "INNER JOIN order_items oi ON o.id = oi.order_id " +
                            "INNER JOIN products p ON oi.product_id = p.id " +
                            "GROUP BY o.id, u.username, o.payment_method, o.status");

            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();

            // Add the data from the query to the table model
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String productNames = rs.getString("product_names");
                double totalPrice = rs.getDouble("total_price");
                String paymentMethod = rs.getString("payment_method");
                String status = rs.getString("status");
                model.addRow(new Object[]{id, username, productNames, totalPrice, paymentMethod, status});
            }
            // Add the table to a scroll pane
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(50, 200, 900, 300);
            homeFrame.add(scrollPane);

        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to the database.");
        }

        try {
            Class.forName(Constants.jdbcClass);
            Connection con = DriverManager.getConnection(Constants.connectionAddress, Constants.databaseUser, Constants.databasePassword);
            // Create a statement to execute the SQL query
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT name FROM ingredients");

            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();

            // Loop through the result set and add the ingredients to the dropdown list
            while (rs.next()) {
                ingredientList.addItem(rs.getString("name"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to the database.");
        }

        updateButton.addActionListener(e -> {
            try {
                Class.forName(Constants.jdbcClass);
                Connection con = DriverManager.getConnection(Constants.connectionAddress, Constants.databaseUser, Constants.databasePassword);
                // Create a statement to execute the SQL query
                PreparedStatement stmt = con.prepareStatement(
                        "UPDATE ingredients SET quantity = ? WHERE name = ?");

                // Execute the query and get the result set
                stmt.setInt(1, Integer.parseInt(quantityField.getText()));
                stmt.setString(2, Objects.requireNonNull(ingredientList.getSelectedItem()).toString());
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Ingredient updated successfully.");
            } catch (SQLException | ClassNotFoundException e1) {
                JOptionPane.showMessageDialog(null, "Error connecting to the database.");
            }
        });

        homeFrame.setVisible(true);
    }
}
