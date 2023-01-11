import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Objects;

public class AdminDashboard {
    AdminDashboard() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenSizeWidth = (int) screenSize.getWidth();
        int screenSizeHeight = (int) screenSize.getHeight();

        JFrame homeFrame = new JFrame("Admin Dashboard");
        homeFrame.setLayout(null);
        homeFrame.setSize(screenSizeWidth, screenSizeHeight);
        homeFrame.setLocationRelativeTo(null);
        homeFrame.getContentPane().setBackground(Color.WHITE);
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color primaryColor = new Color(57, 108, 103);
        Font textTheme = new Font("Verdana", Font.PLAIN, 16);

        // Create a table to display ingredient quantities
        DefaultTableModel ingredientModel = new DefaultTableModel(new String[]{"Ingredient", "Quantity"}, 0);
        JTable ingredientTable = new JTable(ingredientModel);
        ingredientTable.getTableHeader().setFont(textTheme);
        ingredientTable.getTableHeader().setForeground(Color.WHITE);
        ingredientTable.getTableHeader().setBackground(primaryColor);
        ingredientTable.getTableHeader().setOpaque(false);
        ingredientTable.setRowHeight(30);
        ingredientTable.setFont(textTheme);
        ingredientTable.setForeground(Color.GRAY);
        ingredientTable.setBackground(Color.WHITE);
        ingredientTable.setShowGrid(false);
        ingredientTable.setShowVerticalLines(true);
        ingredientTable.setShowHorizontalLines(true);
        ingredientTable.setGridColor(Color.BLACK);
        ingredientTable.setFillsViewportHeight(true);
        JScrollPane ingredientPane = new JScrollPane(ingredientTable);
        ingredientPane.setBounds(50, 50, 300, 300);
        homeFrame.add(ingredientPane);

        // Create a button to refresh the ingredient table
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(primaryColor);
        refreshButton.setFont(textTheme);
        refreshButton.setOpaque(true);
        refreshButton.setBorderPainted(false);
        refreshButton.setBounds(50, 360, 300, 30);
        homeFrame.add(refreshButton);

        refreshButton.addActionListener(e -> {
            try {
                Class.forName(Constants.jdbcClass);
                Connection con = DriverManager.getConnection(Constants.connectionAddress, Constants.databaseUser, Constants.databasePassword);
                // Create a statement to execute the SQL query
                PreparedStatement stmt = con.prepareStatement("SELECT name, quantity FROM ingredients");
                ResultSet ingredientResults = stmt.executeQuery();
                ingredientModel.setRowCount(0);
                while(ingredientResults.next()) {
                    String ingredient = ingredientResults.getString("name");
                    int quantity = ingredientResults.getInt("quantity");
                    ingredientModel.addRow(new Object[] {ingredient, quantity});
                }
                stmt.close();
                con.close();
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(homeFrame, "An error occurred while refreshing the ingredient table.");
            }
        });

        try {
            Class.forName(Constants.jdbcClass);
            Connection con = DriverManager.getConnection(Constants.connectionAddress, Constants.databaseUser, Constants.databasePassword);
            // Create a statement to execute the SQL query
            PreparedStatement stmt = con.prepareStatement("SELECT name, quantity FROM ingredients");
            ResultSet ingredientResults = stmt.executeQuery();
            ingredientModel.setRowCount(0);
            while (ingredientResults.next()) {
                String ingredient = ingredientResults.getString("name");
                int quantity = ingredientResults.getInt("quantity");
                ingredientModel.addRow(new Object[]{ingredient, quantity});
            }
            stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(homeFrame, "An error occurred while refreshing the ingredient table.");
        }

        // Create a dropdown list of ingredients
        JComboBox<String> ingredientList = new JComboBox<>();
        ingredientList.setFont(textTheme);
        ingredientList.setForeground(Color.GRAY);
        ingredientList.setFont(textTheme);
        ingredientList.setBackground(Color.WHITE);
        ingredientList.setBounds(50, 450, 300, 30);

        homeFrame.add(ingredientList);

        // Create a text field for the quantity
        JTextField quantityField = new HintTextField("Quantity");
        quantityField.setFont(textTheme);
        quantityField.setForeground(Color.GRAY);
        quantityField.setFont(textTheme);
        quantityField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        quantityField.setBackground(Color.WHITE);
        quantityField.setBounds(50, 500, 300, 30);
        homeFrame.add(quantityField);

        // Create a button to update the quantity
        JButton updateButton = new JButton("Update");
        updateButton.setFont(textTheme);
        updateButton.setForeground(Color.WHITE);
        updateButton.setBackground(primaryColor);
        updateButton.setFont(textTheme);
        updateButton.setOpaque(true);
        updateButton.setBorderPainted(false);
        updateButton.setBounds(50, 550, 300, 30);
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
        logoutButton.setFont(textTheme);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(primaryColor);
        logoutButton.setFont(textTheme);
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.setBounds(screenSizeWidth - 450, screenSizeHeight - 200, 300, 30);
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
            scrollPane.setBounds(400, 50, 800, 600);
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

            if (Objects.equals(quantityField.getText(), "")) {
                JOptionPane.showMessageDialog(null, "Please enter a quantity.");
            } else {
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
            }
        });

        homeFrame.setVisible(true);
    }
}