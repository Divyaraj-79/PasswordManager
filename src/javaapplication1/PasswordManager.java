/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication1;

/**
 *
 * @author anonymous
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PasswordManager extends JFrame implements ActionListener {
    
    private JPasswordField passwordField;
    private JButton addButton, listButton;
    private JTextArea passwordList;
    private String password = "";
    private Connection conn;
    
    public PasswordManager() {
        super("Password Manager");
        setLayout(new BorderLayout());
        
        passwordField = new JPasswordField(20);
        addButton = new JButton("Add Password");
        listButton = new JButton("List All Passwords");
        passwordList = new JTextArea(10, 20);
        passwordList.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(passwordList);
        
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(passwordField);
        panel.add(addButton);
        panel.add(listButton);
        
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        addButton.addActionListener(this);
        listButton.addActionListener(this);
        passwordField.addActionListener(this);
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/passwords", "root", "root");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton || e.getSource() == passwordField) {
            char[] input = passwordField.getPassword();
            if (input.length > 0) {
                String password = new String(input);
                try {
                    Statement stmt = conn.createStatement();
                    String query = "INSERT INTO passwords (password) VALUES ('" + password + "')";
                    stmt.executeUpdate(query);
                    passwordList.append(password + "\n");
                    passwordField.setText("");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == listButton) {
            passwordList.setText("");
            try {
                Statement stmt = conn.createStatement();
                String query = "SELECT * FROM passwords";
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    passwordList.append(rs.getString("password") + "\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        new PasswordManager();
    }
}