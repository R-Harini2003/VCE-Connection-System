import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NetworkConnectionManagementSystem extends JFrame {
    private JTextField txtuserID, txtDID, txtconnectionID, txtconnectionTypeID, txtconnectionStatus;
    private JTable tblConnections;
    private JButton btnAdd, btnModify, btnDelete, btnDisplay;

    private Connection connection;

    public NetworkConnectionManagementSystem() {
        initializeUI();
        connectToDatabase();
        displayConnections();
    }

    private void initializeUI() {
        txtuserID = new JTextField();
        txtDID = new JTextField();
        txtconnectionID = new JTextField();
        txtconnectionTypeID = new JTextField();
        txtconnectionStatus = new JTextField();

        tblConnections = new JTable();
        tblConnections.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblConnections.getSelectionModel().addListSelectionListener(e -> selectConnection());

        JScrollPane scrollPane = new JScrollPane(tblConnections);

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

        panel.add(new JLabel("userID:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("DID:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("connectionID:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("connectionTypeID:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("connectionStatus:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        panel.add(txtuserID, gbc);
        gbc.gridy++;
        panel.add(txtDID, gbc);
        gbc.gridy++;
        panel.add(txtconnectionID, gbc);
        gbc.gridy++;
        panel.add(txtconnectionTypeID, gbc);
        gbc.gridy++;
        panel.add(txtconnectionStatus, gbc);

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

        btnAdd.addActionListener(e -> insertConnection());

        btnModify.addActionListener(e -> modifyConnection());

        btnDelete.addActionListener(e -> deleteConnection());

        btnDisplay.addActionListener(e -> displayConnections());

        setTitle("Network Connection Management System");
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

    private void insertConnection() {
        String userID = txtuserID.getText();
        String DID = txtDID.getText();
        String connectionID = txtconnectionID.getText();
        String connectionTypeID = txtconnectionTypeID.getText();
        String connectionStatus = txtconnectionStatus.getText();

        String sql = "INSERT INTO networkConnections (userID, DID, connectionID,connectionTypeID, connectionStatus) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userID);
            statement.setString(2, DID);
            statement.setString(3, connectionID);
            statement.setString(4, connectionTypeID);
            statement.setString(5, connectionStatus);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Connection inserted successfully.");
                displayConnections();
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifyConnection() {
        int selectedRow = tblConnections.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a connection to modify.");
            return;
        }

        String connectionID = tblConnections.getValueAt(selectedRow, 0).toString();
        String userID = txtuserID.getText();
        String DID= txtDID.getText();
        String connectionTypeID= txtconnectionTypeID.getText();
       // String status = txtStatus.getText();
        String connectionStatus = txtconnectionStatus.getText();

        String sql = "UPDATE networkConnections SET userID = ?, DID = ?, connectionTypeID = ?, " +
                "connectionStatus = ? WHERE connectionID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userID);
            statement.setString(2, DID);
            statement.setString(3, connectionID);
            statement.setString(4, connectionTypeID);
            statement.setString(5,connectionStatus);
            //statement.setString(6, connectionId);
            
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Connection modified successfully.");
                displayConnections();
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteConnection() {
        int selectedRow = tblConnections.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a connection to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected connection?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String connectionId = tblConnections.getValueAt(selectedRow, 0).toString();
            String sql = "DELETE FROM networkConnections WHERE connectionID = ?";

            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, connectionId);

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Connection deleted successfully.");
                    displayConnections();
                    clearFields();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayConnections() {
        String sql = "SELECT * FROM networkConnections";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ConnectionID");
            model.addColumn("userID");
            model.addColumn("DID");
            model.addColumn("connectionTypeID");
            model.addColumn("connectionStatus");
            //model.addColumn("Bandwidth");

            while (resultSet.next()) {
                String connectionId = resultSet.getString("connectionID");
                String userID = resultSet.getString("userID");
                String DID= resultSet.getString("DID");
                String connectionTypeID= resultSet.getString("connectionTypeID");
                //String status = resultSet.getString("status");
                String connectionStatus = resultSet.getString("connectionStatus");

                model.addRow(new Object[]{connectionId, userID, DID, connectionTypeID,  connectionStatus});
            }

            tblConnections.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void selectConnection() {
        int selectedRow = tblConnections.getSelectedRow();
        if (selectedRow != -1) {
            String userID = tblConnections.getValueAt(selectedRow, 1).toString();
            String DID = tblConnections.getValueAt(selectedRow, 2).toString();
            String connectionTypeID = tblConnections.getValueAt(selectedRow, 3).toString();
            String connectionStatus = tblConnections.getValueAt(selectedRow, 4).toString();
            //String bandwidth = tblConnections.getValueAt(selectedRow, 5).toString();

            txtuserID.setText(userID);
            txtDID.setText(DID);
            txtconnectionTypeID.setText(connectionTypeID);
           // txtStatus.setText(status);
            txtconnectionStatus.setText(connectionStatus);
        }
    }

    private void clearFields() {
        txtuserID.setText("");
        txtDID.setText("");
        txtconnectionTypeID.setText("");
        //txtStatus.setText("");
        txtconnectionStatus.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NetworkConnectionManagementSystem::new);
    }
}
