import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPage extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private JButton retrieveMarksButton;

    public MainPage() {
        // Set frame properties
        setTitle("VCE connection system"); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create label
        JLabel welcomeLabel = new JLabel("Welcome to VCE Connection System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(welcomeLabel, BorderLayout.NORTH);

        // Create panel for the button
        //JPanel buttonPanel = new JPanel();
        //retrieveMarksButton = new JButton("Retrieve Marks");
        //buttonPanel.add(retrieveMarksButton);

        // Create menu bar
        
        JMenuBar menuBar = new JMenuBar();

        // Create menus
        JMenu userMenu = new JMenu("user details");
        JMenu deviceMenu = new JMenu("device Details");
        
        JMenu departmentMenu = new JMenu("department Details");
        JMenu connectionTypeMenu = new JMenu("connectionType Details");
        JMenu networkConnectionsMenu = new JMenu("networkConnections Details");

        // Create menu item for student menu
        JMenuItem viewUserDetails = new JMenuItem("View User Details");
        viewUserDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UserManagementSystem();
            }
        });

        // Create menu item for course menu
        JMenuItem viewDeviceDetails = new JMenuItem("View Device Details");
        viewDeviceDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DeviceManagementSystem();
            }
        });

        // Create menu item for enrollment menu
        JMenuItem viewDepartmentDetails = new JMenuItem("View Departmentment Details");
        viewDepartmentDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DepartmentManagementSystem();
            }
        });

        // Create menu item for semester menu
        JMenuItem viewconnectionTypeDetails = new JMenuItem("View connectionType Details");
        viewconnectionTypeDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ConnectionTypeManagementSystem();
            }
        });

        // Create menu item for grade menu
        JMenuItem viewnetworkConnectionsDetails = new JMenuItem("View networkConnections Details");
        viewnetworkConnectionsDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new NetworkConnectionManagementSystem();
            }
        });

        // Add menu items to respective menus
        userMenu.add(viewUserDetails);
        deviceMenu.add(viewDeviceDetails);
        departmentMenu.add(viewDepartmentDetails);
        connectionTypeMenu.add(viewconnectionTypeDetails);
        networkConnectionsMenu.add(viewnetworkConnectionsDetails);

        // Add menus to the menu bar
        menuBar.add(userMenu);
        menuBar.add(deviceMenu);
        menuBar.add(departmentMenu);
        menuBar.add(connectionTypeMenu);
        menuBar.add(networkConnectionsMenu);

        // Set the menu bar
        setJMenuBar(menuBar);

        // Add the button panel to the frame
        //add(buttonPanel, BorderLayout.CENTER);

        // Set button action for "Retrieve Marks"
       // retrieveMarksButton.addActionListener(new ActionListener() {
         //   public void actionPerformed(ActionEvent e) {
           //     new Retreive();
            //}
        //});
     // Add window listener to handle maximizing the window
        addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
                if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
                    System.out.println("Window maximized");
                } else {
                    System.out.println("Window not maximized");
                }
            } 
        });

        // Set frame size and visibility
        setSize(800, 600);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainPage();
    }
}

