import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentManagementSystem extends JFrame {
    private JTextField txtDepartmentID, txtDepartmentName, txtHODName;
    private JTable tblDepartments;
    private JButton btnAdd, btnModify, btnDelete, btnDisplay;

    private Connection connection;

    public DepartmentManagementSystem() {
        initializeUI();
        connectToDatabase();
        displayDepartments();
    }

    private void initializeUI() {
        txtDepartmentID = new JTextField();
        txtDepartmentName = new JTextField();
        txtHODName = new JTextField();

        tblDepartments = new JTable();
        tblDepartments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDepartments.getSelectionModel().addListSelectionListener(e -> selectDepartment());

        JScrollPane scrollPane = new JScrollPane(tblDepartments);

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

        panel.add(new JLabel("Department ID:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Department Name:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("HOD Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        panel.add(txtDepartmentID, gbc);
        gbc.gridy++;
        panel.add(txtDepartmentName, gbc);
        gbc.gridy++;
        panel.add(txtHODName, gbc);

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

        btnAdd.addActionListener(e -> insertDepartment());

        btnModify.addActionListener(e -> modifyDepartment());

        btnDelete.addActionListener(e -> deleteDepartment());

        btnDisplay.addActionListener(e -> displayDepartments());

        setTitle("Department Management System");
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

    private void insertDepartment() {
        String departmentID = txtDepartmentID.getText();
        String departmentName = txtDepartmentName.getText();
        String hodName = txtHODName.getText();

        try {
            String query = "INSERT INTO departments (departmentID, departmentName, HODName) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, departmentID);
            statement.setString(2, departmentName);
            statement.setString(3, hodName);
            statement.executeUpdate();

            clearFields();
            displayDepartments();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifyDepartment() {
        int selectedRow = tblDepartments.getSelectedRow();
        if (selectedRow >= 0) {
            String departmentID = txtDepartmentID.getText();
            String departmentName = txtDepartmentName.getText();
            String hodName = txtHODName.getText();

            try {
                String query = "UPDATE departments SET departmentName=?, HODName=? WHERE departmentID=?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, departmentName);
                statement.setString(2, hodName);
                statement.setString(3, departmentID);
                statement.executeUpdate();

                clearFields();
                displayDepartments();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a department to modify.");
        }
    }

    private void deleteDepartment() {
        int selectedRow = tblDepartments.getSelectedRow();
        if (selectedRow >= 0) {
            String departmentID = tblDepartments.getValueAt(selectedRow, 0).toString();

            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this department?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    String query = "DELETE FROM departments WHERE departmentID=?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, departmentID);
                    statement.executeUpdate();

                    clearFields();
                    displayDepartments();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a department to delete.");
        }
    }

    private void displayDepartments() {
        try {
            String query = "SELECT * FROM departments";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Department> departments = new ArrayList<>();
            while (resultSet.next()) {
                String departmentID = resultSet.getString("departmentID");
                String departmentName = resultSet.getString("departmentName");
                String hodName = resultSet.getString("HODName");
                departments.add(new Department(departmentID, departmentName, hodName));
            }

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"Department ID", "Department Name", "HOD Name"});

            for (Department department : departments) {
                model.addRow(new String[]{department.getDepartmentID(), department.getDepartmentName(), department.getHODName()});
            }

            tblDepartments.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void selectDepartment() {
        int selectedRow = tblDepartments.getSelectedRow();
        if (selectedRow >= 0) {
            String departmentID = tblDepartments.getValueAt(selectedRow, 0).toString();
            String departmentName = tblDepartments.getValueAt(selectedRow, 1).toString();
            String hodName = tblDepartments.getValueAt(selectedRow, 2).toString();

            txtDepartmentID.setText(departmentID);
            txtDepartmentName.setText(departmentName);
            txtHODName.setText(hodName);
        }
    }

    private void clearFields() {
        txtDepartmentID.setText("");
        txtDepartmentName.setText("");
        txtHODName.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DepartmentManagementSystem::new);
    }

    private class Department {
        private String departmentID;
        private String departmentName;
        private String hodName;

        public Department(String departmentID, String departmentName, String hodName) {
            this.departmentID = departmentID;
            this.departmentName = departmentName;
            this.hodName = hodName;
        }

        public String getDepartmentID() {
            return departmentID;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public String getHODName() {
            return hodName;
        }
    }
}
