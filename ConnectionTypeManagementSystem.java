import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionTypeManagementSystem extends JFrame {
    private JTextField txtConnectionTypeID, txtConnectionTypeName;
    private JTable tblConnectionTypes;
    private JButton btnAdd, btnModify, btnDelete, btnDisplay;

    private Connection connection;

    public ConnectionTypeManagementSystem() {
        initializeUI();
        connectToDatabase();
        displayConnectionTypes();
    }

    private void initializeUI() {
        txtConnectionTypeID = new JTextField();
        txtConnectionTypeName = new JTextField();

        tblConnectionTypes = new JTable();
        tblConnectionTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblConnectionTypes.getSelectionModel().addListSelectionListener(e -> selectConnectionType());

        JScrollPane scrollPane = new JScrollPane(tblConnectionTypes);

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

        panel.add(new JLabel("ConnectionTypeID:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("ConnectionTypeName:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        panel.add(txtConnectionTypeID, gbc);
        gbc.gridy++;
        panel.add(txtConnectionTypeName, gbc);

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

        btnAdd.addActionListener(e -> insertConnectionType());
        btnModify.addActionListener(e -> modifyConnectionType());
        btnDelete.addActionListener(e -> deleteConnectionType());
        btnDisplay.addActionListener(e -> displayConnectionTypes());

        setTitle("Connection Types Management System");
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

    private void insertConnectionType() {
        String connectionTypeID = txtConnectionTypeID.getText();
        String connectionTypeName = txtConnectionTypeName.getText();

        try {
            String query = "INSERT INTO connectionTypes (connectionTypeID, connectionTypeName) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, connectionTypeID);
            statement.setString(2, connectionTypeName);
            statement.executeUpdate();

            clearFields();
            displayConnectionTypes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifyConnectionType() {
        int selectedRow = tblConnectionTypes.getSelectedRow();
        if (selectedRow >= 0) {
            String connectionTypeID = txtConnectionTypeID.getText();
            String connectionTypeName = txtConnectionTypeName.getText();

            try {
                String query = "UPDATE connectionTypes SET connectionTypeName=? WHERE connectionTypeID=?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, connectionTypeName);
                statement.setString(2, connectionTypeID);
                statement.executeUpdate();

                clearFields();
                displayConnectionTypes();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a connection type to modify.");
        }
    }

    private void deleteConnectionType() {
        int selectedRow = tblConnectionTypes.getSelectedRow();
        if (selectedRow >= 0) {
            String connectionTypeID = tblConnectionTypes.getValueAt(selectedRow, 0).toString();

            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this connection type?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    String query = "DELETE FROM connectionTypes WHERE connectionTypeID=?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, connectionTypeID);
                    statement.executeUpdate();

                    clearFields();
                    displayConnectionTypes();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a connection type to delete.");
        }
    }

    private void displayConnectionTypes() {
        try {
            String query = "SELECT * FROM connectionTypes";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<ConnectionType> connectionTypes = new ArrayList<>();
            while (resultSet.next()) {
                String connectionTypeID = resultSet.getString("connectionTypeID");
                String connectionTypeName = resultSet.getString("connectionTypeName");
                connectionTypes.add(new ConnectionType(connectionTypeID, connectionTypeName));
            }

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"ConnectionTypeID", "ConnectionTypeName"});

            for (ConnectionType connectionType : connectionTypes) {
                model.addRow(new String[]{connectionType.getConnectionTypeID(), connectionType.getConnectionTypeName()});
            }

            tblConnectionTypes.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void selectConnectionType() {
        int selectedRow = tblConnectionTypes.getSelectedRow();
        if (selectedRow >= 0) {
            String connectionTypeID = tblConnectionTypes.getValueAt(selectedRow, 0).toString();
            String connectionTypeName = tblConnectionTypes.getValueAt(selectedRow, 1).toString();

            txtConnectionTypeID.setText(connectionTypeID);
            txtConnectionTypeName.setText(connectionTypeName);
        }
    }

    private void clearFields() {
        txtConnectionTypeID.setText("");
        txtConnectionTypeName.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConnectionTypeManagementSystem::new);
    }

    private class ConnectionType {
        private String connectionTypeID;
        private String connectionTypeName;

        public ConnectionType(String connectionTypeID, String connectionTypeName) {
            this.connectionTypeID = connectionTypeID;
            this.connectionTypeName = connectionTypeName;
        }

        public String getConnectionTypeID() {
            return connectionTypeID;
        }

        public String getConnectionTypeName() {
            return connectionTypeName;
        }
    }
}
