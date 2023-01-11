import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserDashboard implements ActionListener {
    ArrayList<JButton> productButtons = new ArrayList<>();
    ArrayList<String> productNames = new ArrayList<>();
    ArrayList<Double> prices = new ArrayList<>();
    ArrayList<Integer> quantities = new ArrayList<>();
    ArrayList<Integer> productIds = new ArrayList<>();
    JLabel totalPriceLabel;
    double totalPrice = 0.0;
    JFrame frame = new JFrame("User Dashboard");
    String[] column = {"PRODUCT", "PRICE", "QUANTITY", "TOTAL"};
    DefaultTableModel tableModel = new DefaultTableModel(column, 0);
    JTable orderTable;
    JScrollPane scrollPane;
    JComboBox<String> paymentMethodComboBox;
    JButton placeOrderBtn;
    int offset;
    final int width = 230;
    final int height = 250;

    UserDashboard(int userId) {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenSizeWidth = (int) screenSize.getWidth();
        int screenSizeHeight = (int) screenSize.getHeight();

        Color primaryColor = new Color(57, 108, 103);
        Font textTheme = new Font("Verdana", Font.PLAIN, 16);

        frame.setLayout(null);
        frame.setSize(screenSizeWidth, screenSizeHeight);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            Class.forName(Constants.jdbcClass);
            Connection con = DriverManager.getConnection(Constants.connectionAddress, Constants.databaseUser, Constants.databasePassword);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM products");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int id = rs.getInt("id");
                productNames.add(name);
                prices.add(price);
                productIds.add(id);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        int numButtons = productNames.size();
        int numColumns = 3;
        int x = 0;
        int y = 0;

        for (int i = 0; i < numButtons; i++) {
            ImageIcon imageIcon = new ImageIcon("Images/" + productNames.get(i) + ".png");
            JButton productButton = new JButton();
            productButton.setActionCommand(Integer.toString(i));
            productButton.setBounds(x, y, width, height);
            offset = productButton.getInsets().left;
            productButton.setIcon(resizeIcon(imageIcon, productButton.getWidth() - offset, productButton.getHeight() - offset));
            productButton.addActionListener(this);
            frame.add(productButton);
            productButtons.add(productButton);
            quantities.add(0);
            x += width;
            if (i % numColumns == numColumns - 1) {
                x = 0;
                y += height;
            }
        }

        orderTable = new JTable(tableModel);
        orderTable.getTableHeader().setFont(textTheme);
        orderTable.getTableHeader().setForeground(Color.WHITE);
        orderTable.getTableHeader().setBackground(primaryColor);
        orderTable.getTableHeader().setOpaque(false);
        orderTable.setRowHeight(30);
        orderTable.setFont(textTheme);
        orderTable.setForeground(Color.GRAY);
        orderTable.setBackground(Color.WHITE);
        orderTable.setShowGrid(false);
        orderTable.setShowVerticalLines(true);
        orderTable.setShowHorizontalLines(true);
        orderTable.setGridColor(Color.BLACK);
        orderTable.setFillsViewportHeight(true);
        orderTable.setRowSelectionAllowed(false);
        orderTable.setCellSelectionEnabled(false);
        orderTable.setDragEnabled(false);

        scrollPane = new JScrollPane(orderTable);
        scrollPane.setPreferredSize(new Dimension(500, 200));
        scrollPane.setBounds(width * 3 + 100, 50, width * 2, height);
        frame.add(scrollPane);

        String[] paymentMethods = {"Cash", "Card"};
        paymentMethodComboBox = new JComboBox<>(paymentMethods);
        paymentMethodComboBox.setBounds(width * 4 + 100, height + 70, width, 30);
        paymentMethodComboBox.setFont(textTheme);
        paymentMethodComboBox.setForeground(Color.GRAY);
        paymentMethodComboBox.setBackground(Color.WHITE);
        frame.add(paymentMethodComboBox);

        totalPriceLabel = new JLabel("Total Price: $0.0");
        totalPriceLabel.setBounds(width * 3 + 100, height + 70, width, 30);
        totalPriceLabel.setFont(textTheme);
        frame.add(totalPriceLabel);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(screenSizeWidth - 350, screenSizeHeight - 200, 300, 30);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(primaryColor);
        logoutButton.setFont(textTheme);
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        frame.add(logoutButton);

        logoutButton.addActionListener(e -> {
            frame.dispose();
            new Login();
        });

        placeOrderBtn = new JButton("Place Order");
        placeOrderBtn.setBackground(primaryColor);
        placeOrderBtn.setForeground(Color.WHITE);
        placeOrderBtn.setFont(textTheme);
        placeOrderBtn.setOpaque(true);
        placeOrderBtn.setBorderPainted(false);
        placeOrderBtn.setBounds(width * 4 + 100, height + 150, width, 30);
        placeOrderBtn.addActionListener(this);
        frame.add(placeOrderBtn);

        placeOrderBtn.addActionListener(e -> {
            // selectare modalitate de plata
            String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();

            // inserare comanda in tabel
            try {
                Class.forName(Constants.jdbcClass);
                Connection con = DriverManager.getConnection(Constants.connectionAddress, Constants.databaseUser, Constants.databasePassword);
                PreparedStatement stmt = con.prepareStatement("INSERT INTO orders (user_id, payment_method, status) VALUES (?, ?, ?)");
                stmt.setString(1, Integer.toString(userId));
                stmt.setString(2, paymentMethod);
                stmt.setString(3, "finalized");
                stmt.executeUpdate();
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Error inserting order into the database.");
            }

            // id comenzii
            int orderId = 0;
            try {
                Class.forName(Constants.jdbcClass);
                Connection con = DriverManager.getConnection(Constants.connectionAddress, Constants.databaseUser, Constants.databasePassword);
                PreparedStatement stmt = con.prepareStatement("SELECT id FROM orders ORDER BY id DESC LIMIT 1");
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    orderId = rs.getInt("id");
                }
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Error getting order id from the database.");
            }

            // inserare comanda in tabela order_items
            for (int i = 0; i < quantities.size(); i++) {
                if (quantities.get(i) > 0) {
                    try {
                        Class.forName(Constants.jdbcClass);
                        Connection con = DriverManager.getConnection(Constants.connectionAddress, Constants.databaseUser, Constants.databasePassword);
                        PreparedStatement stmt = con.prepareStatement("INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)");
                        stmt.setInt(1, orderId);
                        stmt.setInt(2, productIds.get(i));
                        stmt.setInt(3, quantities.get(i));
                        stmt.executeUpdate();
                    } catch (SQLException | ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, "Error inserting order item into the database.");
                    }
                }
            }

            // modificare cantitate ingredients folosite in comanda
            try {
                Class.forName(Constants.jdbcClass);
                Connection con = DriverManager.getConnection(Constants.connectionAddress, Constants.databaseUser,
                        Constants.databasePassword);
                for (int i = 0; i < quantities.size(); i++) {
                    if (quantities.get(i) > 0) {
                        // folosim reteta asociata produsului
                        PreparedStatement stmt = con
                                .prepareStatement("SELECT recipe_id FROM products WHERE id = ?");
                        stmt.setInt(1, productIds.get(i));
                        ResultSet rs = stmt.executeQuery();
                        int recipeId = -1;
                        if (rs.next()) {
                            recipeId = rs.getInt("recipe_id");
                        }

                        // ingredientele folosite in reteta
                        stmt = con.prepareStatement(
                                "SELECT ingredient_id, quantity FROM recipe_ingredients WHERE recipe_id = ?");
                        stmt.setInt(1, recipeId);
                        rs = stmt.executeQuery();
                        while (rs.next()) {
                            int ingredientId = rs.getInt("ingredient_id");
                            int ingredientQuantity = rs.getInt("quantity");

                            // scadere cantitate ingredient in stoc
                            stmt = con.prepareStatement(
                                    "UPDATE ingredients SET quantity = quantity - ? WHERE id = ?");
                            stmt.setInt(1, ingredientQuantity * quantities.get(i));
                            stmt.setInt(2, ingredientId);
                            stmt.executeUpdate();
                        }
                    }
                }
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }

            // resetare tabela comanda
            tableModel.setRowCount(0);
            totalPrice = 0.0;
            totalPriceLabel.setText("Total Price: $0.0");
            quantities = new ArrayList<>();
            for (int i = 0; i < productNames.size(); i++) {
                quantities.add(0);
            }
            JOptionPane.showMessageDialog(null, "Order placed successfully!");
        });

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int index = Integer.parseInt(e.getActionCommand());
            // incrementare cantitate produs
            quantities.set(index, quantities.get(index) + 1);
            Object[] data = {productNames.get(index), "$" + prices.get(index), quantities.get(index), "$" + (prices.get(index) * quantities.get(index))};
            tableModel.addRow(data);
            // schimbare pret total
            totalPrice += prices.get(index);
            totalPriceLabel.setText("Total Price: $" + totalPrice);
        } catch (NumberFormatException ex) {
            if (e.getActionCommand().equals("Place Order")) {

                ex.printStackTrace();

            } else {
                JOptionPane.showMessageDialog(frame, "Error: Unrecognized action command: " + e.getActionCommand());
            }
        }
    }


    public ImageIcon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    public static void main(String[] args) {
        new UserDashboard(1);
    }
}
