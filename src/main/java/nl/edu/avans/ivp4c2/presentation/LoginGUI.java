package nl.edu.avans.ivp4c2.presentation;

import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.org.apache.xpath.internal.SourceTreeManager;
import nl.edu.avans.ivp4c2.domain.Employee;
import nl.edu.avans.ivp4c2.manager.LoginManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Bram on 22-5-2015.
 */
public class LoginGUI extends JPanel {
    private JTextField logInOutField;
    private JButton loginButton;
    private JButton logoutButton;

    private LoginManager loginmanager;

    public LoginGUI() {
        loginmanager = new LoginManager();
        JPanel login = new JPanel();

        logInOutField = new JTextField("Medewerkerscode",20);
        loginButton = new JButton("Aanmelden");
        loginButton.addActionListener(new LogInOutHandler() );
        logoutButton = new JButton("Afmelden");
        logoutButton.addActionListener(new LogOutHandler() );

        login.add(logInOutField);
        login.add(loginButton);
        login.add(logoutButton);

        // This will add the items to the panel and show them.
        JOptionPane.showMessageDialog(this, login, "Inlogscherm", JOptionPane.CANCEL_OPTION);
    }


    // Inner class
    class LogInOutHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Dit werkt
            int employeeCode =   Integer.parseInt(logInOutField.getText());

            System.out.println(employeeCode);
            System.out.println(loginmanager.findEmployee(employeeCode));

        }
    }

    class LogOutHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println(loginmanager.getEmployees());
        }
    }
}