import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeviceManagementSystem extends JFrame {
    private JTextField txtDID, txtDName, txtDType, txtMACAddress, txtIPAddress, txtSubnetMask, txtGatewayIP;
    private JTable tblDevices;
    private JButton btnAdd, btnModify, btnDelete, btnDisplay;

    private Connection connection;

    public DeviceManagementSystem() {
        initializeUI();
        connectToDatabase();
        displayDevices();
    }

    private void initializeUI() {
        txtDID = new JTextField();
        txtDName = new JTextField();
        txtDType = new JTextField();
        txtMACAddress = new JTextField();
        txtIPAddress = new JTextField();
        txtSubnetMask = new JTextField();
        txtGatewayIP = new JTextField();

        tblDevices = new JTable();
        tblDevices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDevices.getSelectionModel().addListSelectionListener(e -> selectDevice());

        JScrollPane scrollPane = new JScrollPane(tblDevices);

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

        panel.add(new JLabel("DID:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("DName:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("DType:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("MAC Address:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("IP Address:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Subnet Mask:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Gateway IP:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        panel.add(txtDID, gbc);
        gbc.gridy++;
        panel.add(txtDName, gbc);
        gbc.gridy++;
        panel.add(txtDType, gbc);
        gbc.gridy++;
        panel.add(txtMACAddress, gbc);
        gbc.gridy++;
        panel.add(txtIPAddress, gbc);
        gbc.gridy++;
        panel.add(txtSubnetMask, gbc);
        gbc.gridy++;
        panel.add(txtGatewayIP, gbc);

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

        btnAdd.addActionListener(e -> insertDevice());

        btnModify.addActionListener(e -> modifyDevice());

        btnDelete.addActionListener(e -> deleteDevice());

        btnDisplay.addActionListener(e -> displayDevices());

        setTitle("Device Management System");
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

    private void insertDevice() {
        String did = txtDID.getText();
        String dname = txtDName.getText();
        String dtype = txtDType.getText();
        String macAddress = txtMACAddress.getText();
        String ipAddress = txtIPAddress.getText();
        String subnetMask = txtSubnetMask.getText();
        String gatewayIP = txtGatewayIP.getText();

        try {
            String query = "INSERT INTO devices (DID, Dname, Dtype, MACAddress, IPAddress, subnetmask, gatewayIP) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, did);
            statement.setString(2, dname);
            statement.setString(3, dtype);
            statement.setString(4, macAddress);
            statement.setString(5, ipAddress);
            statement.setString(6, subnetMask);
            statement.setString(7, gatewayIP);
            statement.executeUpdate();

            clearFields();
            displayDevices();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifyDevice() {
        int selectedRow = tblDevices.getSelectedRow();
        if (selectedRow >= 0) {
            String did = txtDID.getText();
            String dname = txtDName.getText();
            String dtype = txtDType.getText();
            String macAddress = txtMACAddress.getText();
            String ipAddress = txtIPAddress.getText();
            String subnetMask = txtSubnetMask.getText();
            String gatewayIP = txtGatewayIP.getText();

            try {
                String query = "UPDATE devices SET Dname=?, Dtype=?, MACAddress=?, IPAddress=?, subnetmask=?, gatewayIP=? WHERE DID=?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, dname);
                statement.setString(2, dtype);
                statement.setString(3, macAddress);
                statement.setString(4, ipAddress);
                statement.setString(5, subnetMask);
                statement.setString(6, gatewayIP);
                statement.setString(7, did);
                statement.executeUpdate();

                clearFields();
                displayDevices();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a device to modify.");
        }
    }

    private void deleteDevice() {
        int selectedRow = tblDevices.getSelectedRow();
        if (selectedRow >= 0) {
            String did = tblDevices.getValueAt(selectedRow, 0).toString();

            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this device?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    String query = "DELETE FROM devices WHERE DID=?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, did);
                    statement.executeUpdate();

                    clearFields();
                    displayDevices();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a device to delete.");
        }
    }

    private void displayDevices() {
        try {
            String query = "SELECT * FROM devices";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Device> devices = new ArrayList<>();
            while (resultSet.next()) {
                String did = resultSet.getString("DID");
                String dname = resultSet.getString("Dname");
                String dtype = resultSet.getString("Dtype");
                String macAddress = resultSet.getString("MACAddress");
                String ipAddress = resultSet.getString("IPAddress");
                String subnetMask = resultSet.getString("subnetmask");
                String gatewayIP = resultSet.getString("gatewayIP");
                devices.add(new Device(did, dname, dtype, macAddress, ipAddress, subnetMask, gatewayIP));
            }

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"DID", "DName", "DType", "MACAddress", "IPAddress", "SubnetMask", "GatewayIP"});

            for (Device device : devices) {
                model.addRow(new String[]{device.getDid(), device.getDname(), device.getDtype(), device.getMacAddress(), device.getIpAddress(), device.getSubnetMask(), device.getGatewayIP()});
            }

            tblDevices.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void selectDevice() {
        int selectedRow = tblDevices.getSelectedRow();
        if (selectedRow >= 0) {
            String did = tblDevices.getValueAt(selectedRow, 0).toString();
            String dname = tblDevices.getValueAt(selectedRow, 1).toString();
            String dtype = tblDevices.getValueAt(selectedRow, 2).toString();
            String macAddress = tblDevices.getValueAt(selectedRow, 3).toString();
            String ipAddress = tblDevices.getValueAt(selectedRow, 4).toString();
            String subnetMask = tblDevices.getValueAt(selectedRow, 5).toString();
            String gatewayIP = tblDevices.getValueAt(selectedRow, 6).toString();

            txtDID.setText(did);
            txtDName.setText(dname);
            txtDType.setText(dtype);
            txtMACAddress.setText(macAddress);
            txtIPAddress.setText(ipAddress);
            txtSubnetMask.setText(subnetMask);
            txtGatewayIP.setText(gatewayIP);
        }
    }

    private void clearFields() {
        txtDID.setText("");
        txtDName.setText("");
        txtDType.setText("");
        txtMACAddress.setText("");
        txtIPAddress.setText("");
        txtSubnetMask.setText("");
        txtGatewayIP.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DeviceManagementSystem::new);
    }

    private class Device {
        private String did;
        private String dname;
        private String dtype;
        private String macAddress;
        private String ipAddress;
        private String subnetMask;
        private String gatewayIP;

        public Device(String did, String dname, String dtype, String macAddress, String ipAddress, String subnetMask, String gatewayIP) {
            this.did = did;
            this.dname = dname;
            this.dtype = dtype;
            this.macAddress = macAddress;
            this.ipAddress = ipAddress;
            this.subnetMask = subnetMask;
            this.gatewayIP = gatewayIP;
        }

        public String getDid() {
            return did;
        }

        public String getDname() {
            return dname;
        }

        public String getDtype() {
            return dtype;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public String getSubnetMask() {
            return subnetMask;
        }

        public String getGatewayIP() {
            return gatewayIP;
        }
    }
}
