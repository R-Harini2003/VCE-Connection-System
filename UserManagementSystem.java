import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserManagementSystem extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField txtUserID, txtFirstName, txtLastName, txtEmail, txtPhoneNumber, txtRole, txtUsername, txtPassword, txtDepartmentID;
    private JTable tblUsers;
    private JButton btnAdd, btnModify, btnDelete, btnDisplay;

    private Connection connection;

    public UserManagementSystem() {
        initializeUI();
        connectToDatabase();
        displayUsers();
    }

    private void initializeUI() {
        txtUserID = new JTextField();
        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtEmail = new JTextField();
        txtPhoneNumber = new JTextField();
        txtRole = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JTextField();
        txtDepartmentID = new JTextField();

        tblUsers = new JTable();
        tblUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblUsers.getSelectionModel().addListSelectionListener(e -> selectUser());

        JScrollPane scrollPane = new JScrollPane(tblUsers);

        btnAdd = new JButton("Add");
        btnModify = new JButton("Modify");
        btnDelete = new JButton("Delete");
        btnDisplay = new JButton("Display");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        panel.add(new JLabel("User ID:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("First Name:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Department ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        panel.add(txtUserID, gbc);
        gbc.gridy++;
        panel.add(txtFirstName, gbc);
        gbc.gridy++;
        panel.add(txtLastName, gbc);
        gbc.gridy++;
        panel.add(txtEmail, gbc);
        gbc.gridy++;
        panel.add(txtPhoneNumber, gbc);
        gbc.gridy++;
        panel.add(txtRole, gbc);
        gbc.gridy++;
        panel.add(txtUsername, gbc);
        gbc.gridy++;
        panel.add(txtPassword, gbc);
        gbc.gridy++;
        panel.add(txtDepartmentID, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;

        panel.add(btnAdd, gbc);
        gbc.gridy++;
        panel.add(btnModify, gbc);
        gbc.gridy++;
        panel.add(btnDelete, gbc);
        gbc.gridy++;
        panel.add(btnDisplay, gbc);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> insertUser());

        btnModify.addActionListener(e -> modifyUser());

        btnDelete.addActionListener(e -> deleteUser());

        btnDisplay.addActionListener(e -> displayUsers());

        setTitle("User Management System");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void connectToDatabase() {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "harini";
        String password = "harini18";

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertUser() {
        String userID = txtUserID.getText();
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String email = txtEmail.getText();
        String phoneNumber = txtPhoneNumber.getText();
        String role = txtRole.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String departmentID = txtDepartmentID.getText();

        try {
            String query = "INSERT INTO users (userID, fname, lname, email, phonenumber, role, username, password, departmentID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userID);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, email);
            statement.setString(5, phoneNumber);
            statement.setString(6, role);
            statement.setString(7, username);
            statement.setString(8, password);
            statement.setString(9, departmentID);
            statement.executeUpdate();

            clearFields();
            displayUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifyUser() {
        int selectedRow = tblUsers.getSelectedRow();
        if (selectedRow >= 0) {
            String userID = txtUserID.getText();
            String firstName = txtFirstName.getText();
            String lastName = txtLastName.getText();
            String email = txtEmail.getText();
            String phoneNumber = txtPhoneNumber.getText();
            String role = txtRole.getText();
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            String departmentID = txtDepartmentID.getText();

            try {
                String query = "UPDATE users SET fname=?, lname=?, email=?, phonenumber=?, role=?, username=?, password=?, departmentID=? WHERE userID=?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, email);
                statement.setString(4, phoneNumber);
                statement.setString(5, role);
                statement.setString(6, username);
                statement.setString(7, password);
                statement.setString(8, departmentID);
                statement.setString(9, userID);
                statement.executeUpdate();

                clearFields();
                displayUsers();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to modify.");
        }
    }

    private void deleteUser() {
        int selectedRow = tblUsers.getSelectedRow();
        if (selectedRow >= 0) {
            String userID = tblUsers.getValueAt(selectedRow, 0).toString();

            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    String query = "DELETE FROM users WHERE userID=?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, userID);
                    statement.executeUpdate();

                    clearFields();
                    displayUsers();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
        }
    }

    private void displayUsers() {
        try {
            String query = "SELECT * FROM users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                System.out.println("Column " + i + ": " + columnName);
            }


            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                String userID = resultSet.getString("userID");
                String firstName = resultSet.getString("fname");
                String lastName = resultSet.getString("lname");
                String email = resultSet.getString("email");
                String phoneNumber = resultSet.getString("phonenumber");
                String role = resultSet.getString("role");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String departmentID = resultSet.getString("departmentID");
                users.add(new User(userID, firstName, lastName, email, phoneNumber, role, username, password, departmentID));
            }

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"User ID", "First Name", "Last Name", "Email", "Phone Number", "Role", "Username", "Password", "Department ID"});

            for (User user : users) {
                model.addRow(new String[]{user.getUserID(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getRole(), user.getUsername(), user.getPassword(), user.getDepartmentID()});
            }

            tblUsers.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void selectUser() {
        int selectedRow = tblUsers.getSelectedRow();
        if (selectedRow >= 0) {
            String userID = tblUsers.getValueAt(selectedRow, 0).toString();
            String firstName = tblUsers.getValueAt(selectedRow, 1).toString();
            String lastName = tblUsers.getValueAt(selectedRow, 2).toString();
            String email = tblUsers.getValueAt(selectedRow, 3).toString();
            String phoneNumber = tblUsers.getValueAt(selectedRow, 4).toString();
            String role = tblUsers.getValueAt(selectedRow, 5).toString();
            String username = tblUsers.getValueAt(selectedRow, 6).toString();
            String password = tblUsers.getValueAt(selectedRow, 7).toString();
            String departmentID = tblUsers.getValueAt(selectedRow, 8).toString();

            txtUserID.setText(userID);
            txtFirstName.setText(firstName);
            txtLastName.setText(lastName);
            txtEmail.setText(email);
            txtPhoneNumber.setText(phoneNumber);
            txtRole.setText(role);
            txtUsername.setText(username);
            txtPassword.setText(password);
            txtDepartmentID.setText(departmentID);
        }
    }

    private void clearFields() {
        txtUserID.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPhoneNumber.setText("");
        txtRole.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtDepartmentID.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserManagementSystem::new);
    }

    private class User {
        private String userID;
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private String role;
        private String username;
        private String password;
        private String departmentID;

        public User(String userID, String firstName, String lastName, String email, String phoneNumber, String role, String username, String password, String departmentID) {
            this.userID = userID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.role = role;
            this.username = username;
            this.password = password;
            this.departmentID = departmentID;
        }

        public String getUserID() {
            return userID;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getRole() {
            return role;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getDepartmentID() {
            return departmentID;
        }
    }
}
