import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SignUp implements ActionListener {
    JLabel appIcon;
    HintTextField usernameTxt, firstnameTxt, lastnameTxt;
    HintPasswordField passwordTxt;
    JButton loginBtn, signupBtn;
    Color primaryColor;
    Font textTheme;
    private final JFrame signupFrame;

    SignUp() {
        signupFrame = new JFrame("Sign Up");
        signupFrame.setLayout(null);
        signupFrame.setSize(600, 600);
        signupFrame.setLocationRelativeTo(null);
        signupFrame.getContentPane().setBackground(Color.WHITE);
        signupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        primaryColor = new Color(57, 108, 103);
        textTheme = new Font("Verdana", Font.PLAIN, 16);

        appIcon = new JLabel(new ImageIcon("Images/cup.png"));
        appIcon.setBounds(250, 20, 128, 128);
        signupFrame.add(appIcon);

        firstnameTxt = new HintTextField("First Name");
        firstnameTxt.setBounds(150, 160, 300, 30);
        firstnameTxt.setForeground(Color.GRAY);
        firstnameTxt.setFont(textTheme);
        firstnameTxt.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        signupFrame.add(firstnameTxt);

        lastnameTxt = new HintTextField("Last Name");
        lastnameTxt.setBounds(150, 220, 300, 30);
        lastnameTxt.setForeground(Color.GRAY);
        lastnameTxt.setFont(textTheme);
        lastnameTxt.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        signupFrame.add(lastnameTxt);

        usernameTxt = new HintTextField("Username");
        usernameTxt.setBounds(150, 280, 300, 30);
        usernameTxt.setForeground(Color.GRAY);
        usernameTxt.setFont(textTheme);
        usernameTxt.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        signupFrame.add(usernameTxt);

        passwordTxt = new HintPasswordField("Password");
        passwordTxt.setBounds(150, 340, 300, 30);
        passwordTxt.setForeground(Color.GRAY);
        passwordTxt.setFont(textTheme);
        passwordTxt.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        signupFrame.add(passwordTxt);

        signupBtn = new JButton("Sign Up");
        signupBtn.setBounds(150, 400, 300, 30);
        signupBtn.setBackground(primaryColor);
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFont(textTheme);
        signupBtn.setOpaque(true);
        signupBtn.setBorderPainted(false);
        signupBtn.addActionListener(this);
        signupFrame.add(signupBtn);

        loginBtn = new JButton("Already have an account? Login");
        loginBtn.setBounds(135, 440, 330, 30);
        loginBtn.setForeground(primaryColor);
        loginBtn.setBackground(Color.WHITE);
        loginBtn.setFont(textTheme);
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false);
        loginBtn.addActionListener(this);
        signupFrame.add(loginBtn);

        signupFrame.setVisible(true);

    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == signupBtn) {

            String username = usernameTxt.getText();
            String password = String.valueOf(passwordTxt.getPassword());
            String firstname = firstnameTxt.getText();
            String lastname = lastnameTxt.getText();

            if (username.isEmpty() || password.isEmpty() || firstname.isEmpty() || lastname.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter all fields", "Try Again", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Class.forName(Constants.jdbcClass);
                Connection con = DriverManager.getConnection(Constants.connectionAddress, Constants.databaseUser, Constants.databasePassword);

                String signUpQuery = "INSERT INTO users (username, password, first_name, last_name, role) VALUES (?, ?, ?, ?, ?)";

                PreparedStatement signUpStatement = con.prepareStatement(signUpQuery);
                signUpStatement.setString(1, username);
                signUpStatement.setString(2, password);
                signUpStatement.setString(3, firstname);
                signUpStatement.setString(4, lastname);
                signUpStatement.setString(5, "waiter");

                signUpStatement.execute();

                JOptionPane.showMessageDialog(null, "Successful", "Sign Up", JOptionPane.INFORMATION_MESSAGE);
                signupFrame.dispose();
            } catch (SQLException e) {
                if (e instanceof SQLIntegrityConstraintViolationException) {
                    JOptionPane.showMessageDialog(null, "Username already exists!", "Sign Up", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed", "Sign Up", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            signupFrame.dispose();
            new Login();
        }
    }
}