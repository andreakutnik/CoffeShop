import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login implements ActionListener {
    JLabel appIcon;
    HintTextField usernameTxt;
    HintPasswordField passwordTxt;
    JButton loginBtn, signupBtn;
    Color primaryColor;
    Font textTheme;
    private final JFrame loginFrame;

    Login() {
        loginFrame = new JFrame("Login");
        loginFrame.setLayout(null);
        loginFrame.setSize(600, 600);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.getContentPane().setBackground(Color.WHITE);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        primaryColor = new Color(57, 108, 103);
        textTheme = new Font("Verdana", Font.PLAIN, 16);

        appIcon = new JLabel(new ImageIcon("Images/cup.png"));
        appIcon.setBounds(250, 20, 128, 128);
        loginFrame.add(appIcon);

        usernameTxt = new HintTextField("Username");
        usernameTxt.setBounds(150, 200, 300, 30);
        usernameTxt.setForeground(Color.GRAY);
        usernameTxt.setFont(textTheme);
        usernameTxt.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        loginFrame.add(usernameTxt);

        passwordTxt = new HintPasswordField("Password");
        passwordTxt.setBounds(150, 260, 300, 30);
        passwordTxt.setForeground(Color.GRAY);
        passwordTxt.setFont(textTheme);
        passwordTxt.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        loginFrame.add(passwordTxt);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 320, 300, 30);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBackground(primaryColor);
        loginBtn.setFont(textTheme);
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false);
        loginBtn.addActionListener(this);
        loginFrame.add(loginBtn);

        signupBtn = new JButton("Don't have an Account yet? SignUp");
        signupBtn.setBounds(135, 380, 330, 30);
        signupBtn.setBackground(Color.WHITE);
        signupBtn.setForeground(primaryColor);
        signupBtn.setFont(textTheme);
        signupBtn.setOpaque(true);
        signupBtn.setBorderPainted(false);
        signupBtn.addActionListener(this);
        loginFrame.add(signupBtn);

        loginFrame.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == loginBtn) {

            String username = usernameTxt.getText();
            String password = String.valueOf(passwordTxt.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter all fields", "Try Again", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Class.forName(Constants.jdbcClass);
                Connection con = DriverManager.getConnection(Constants.connectionAddress, Constants.databaseUser, Constants.databasePassword);
                Statement stmt = con.createStatement();

                String sql = "select * from users where username='" + username + "' and password='" + password
                        + "'";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    // get user id
                    if (rs.getString("role").equals("admin")) {
                        new AdminDashboard();
                        loginFrame.dispose();
                    } else {
                        int userId = rs.getInt("id");
                        new UserDashboard(userId);
                        loginFrame.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Try Again", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == signupBtn) {
            loginFrame.dispose();
            new SignUp();
        }
    }
}