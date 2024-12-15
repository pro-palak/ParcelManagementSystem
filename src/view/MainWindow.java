package view;

import javax.swing.*;
import java.awt.*;
import model.ParcelMap;
import util.Log;
import model.CustomersQueue;
import controller.ParcelController;
import model.Parcel;
import model.Customer;
import java.util.Map;
import java.util.Queue;

public class MainWindow extends JFrame {
    private JPanel parcelPanel;
    private JPanel queuePanel;
    private JPanel currentParcelPanel;
    private JTextArea logArea;
    private ParcelController controller;

    public MainWindow(ParcelController controller) {
        this.controller = controller;
        setTitle("Parcel Management System");
        setSize(1000, 700);  // Increased size for better visibility
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Use BorderLayout explicitly
        setLayout(new BorderLayout());

        // Initialize panels first
        parcelPanel = new JPanel(new GridLayout(0, 1));
        queuePanel = new JPanel(new GridLayout(0, 1));
        currentParcelPanel = new JPanel(new BorderLayout());
        logArea = new JTextArea(10, 30);
        logArea.setEditable(false);

        // Create scroll panes for panels
        JScrollPane parcelScrollPane = new JScrollPane(parcelPanel);
        parcelScrollPane.setBorder(BorderFactory.createTitledBorder("Parcels"));
        
        JScrollPane queueScrollPane = new JScrollPane(queuePanel);
        queueScrollPane.setBorder(BorderFactory.createTitledBorder("Customer Queue"));
        
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Log"));

        // Create control panel
        JPanel controlPanel = new JPanel();
        JButton processButton = new JButton("Process Next Customer");
        JButton addCustomerButton = new JButton("Add Customer");
        JButton addParcelButton = new JButton("Add Parcel");
        
        processButton.addActionListener(e -> processNextCustomer());
        addCustomerButton.addActionListener(e -> showAddCustomerDialog());
        addParcelButton.addActionListener(e -> showAddParcelDialog());
        
        controlPanel.add(processButton);
        controlPanel.add(addCustomerButton);
        controlPanel.add(addParcelButton);

        // Set up main layout
        JSplitPane centerSplitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT, 
            parcelScrollPane, 
            queueScrollPane
        );
        centerSplitPane.setDividerLocation(500);

        // Add components to frame
        add(currentParcelPanel, BorderLayout.NORTH);
        add(centerSplitPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(logScrollPane, BorderLayout.EAST);

        // Final setup
        pack();  // Adjusts window to preferred sizes
        setLocationRelativeTo(null);  // Center on screen
    }
    
 // Add to MainWindow
    private boolean validateParcelInput(String id, String weight, String days) {
        if (id.trim().isEmpty()) {
            System.out.println("Parcel ID cannot be empty");
            return false;
        }
        try {
            double w = Double.parseDouble(weight);
            int d = Integer.parseInt(days);
            if (w <= 0 || d < 0) {
                System.out.println("Invalid weight or days");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format");
            return false;
        }
        return true;
    }

    private void createPanels() {
        // Parcels Panel with scroll
        parcelPanel = new JPanel();
        parcelPanel.setLayout(new BoxLayout(parcelPanel, BoxLayout.Y_AXIS));
        JScrollPane parcelScrollPane = new JScrollPane(parcelPanel);
        parcelScrollPane.setBorder(BorderFactory.createTitledBorder("Parcels"));
        parcelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Queue Panel with scroll
        queuePanel = new JPanel();
        queuePanel.setLayout(new BoxLayout(queuePanel, BoxLayout.Y_AXIS));
        JScrollPane queueScrollPane = new JScrollPane(queuePanel);
        queueScrollPane.setBorder(BorderFactory.createTitledBorder("Customer Queue"));
        queueScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // In your constructor or layout setup, use the scroll panes instead of direct panels
        // For example:
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
            parcelScrollPane, queueScrollPane);
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        
        JButton processButton = new JButton("Process Next Customer");
        JButton addCustomerButton = new JButton("Add Customer");
        JButton addParcelButton = new JButton("Add Parcel");
        
        processButton.addActionListener(e -> processNextCustomer());
        addCustomerButton.addActionListener(e -> showAddCustomerDialog());
        addParcelButton.addActionListener(e -> showAddParcelDialog());
        
        controlPanel.add(processButton);
        controlPanel.add(addCustomerButton);
        controlPanel.add(addParcelButton);
        
        return controlPanel;
    }

    private void showAddCustomerDialog() {
        JDialog dialog = new JDialog(this, "Add Customer", true);
        dialog.setLayout(new GridLayout(4, 2));
        
        JTextField nameField = new JTextField();
        JTextField parcelIdField = new JTextField();
        
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Parcel ID:"));
        dialog.add(parcelIdField);
        
        JButton submitButton = new JButton("Add");
        submitButton.addActionListener(e -> {
            controller.addCustomer(nameField.getText(), parcelIdField.getText());
            updateDisplay();
            dialog.dispose();
        });
        
        dialog.add(submitButton);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void showAddParcelDialog() {
        JDialog dialog = new JDialog(this, "Add Parcel", true);
        dialog.setLayout(new GridLayout(5, 2));
        
        JTextField idField = new JTextField();
        JTextField weightField = new JTextField();
        JTextField daysField = new JTextField();
        JTextField dimensionsField = new JTextField();
        
        dialog.add(new JLabel("Parcel ID:"));
        dialog.add(idField);
        dialog.add(new JLabel("Weight:"));
        dialog.add(weightField);
        dialog.add(new JLabel("Days:"));
        dialog.add(daysField);
        dialog.add(new JLabel("Dimensions:"));
        dialog.add(dimensionsField);
        
        JButton submitButton = new JButton("Add");
        submitButton.addActionListener(e -> {
            try {
                controller.addParcel(
                    idField.getText(),
                    Double.parseDouble(weightField.getText()),
                    Integer.parseInt(daysField.getText())
                );
                updateDisplay();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid number format");
            }
        });
        
        dialog.add(submitButton);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void processNextCustomer() {
        controller.processNextCustomer();
        updateDisplay();
    }

    public void updateDisplay() {
        SwingUtilities.invokeLater(() -> {
            if (controller == null) return;
            
            // Clear existing panels
            parcelPanel.removeAll();
            queuePanel.removeAll();
            currentParcelPanel.removeAll();

            // Parcels display - show only unprocessed parcels
            Map<String, Parcel> parcels = controller.getParcelMap().getParcels();
            parcels.values().stream()
                .filter(parcel -> !parcel.getStatus().equals("Processed"))
                .limit(10)
                .forEach(parcel -> {
                    JPanel parcelInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    parcelInfoPanel.add(new JLabel(String.format(
                        "ID: %s, Weight: %.2f, Days: %d, Status: %s",
                        parcel.getParcelID(), parcel.getWeight(), 
                        parcel.getNoOfDays(), parcel.getStatus()
                    )));
                    parcelPanel.add(parcelInfoPanel);
                });

            // Customers display
            Queue<Customer> customers = controller.getQueue().getCustomers();
            customers.stream()
                .limit(10)
                .forEach(customer -> {
                    JPanel customerInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    customerInfoPanel.add(new JLabel(String.format(
                        "Queue #%d: %s (Parcel: %s)",
                        customer.getQno(), customer.getName(), 
                        customer.getParcelID()
                    )));
                    queuePanel.add(customerInfoPanel);
                });

            // Current Processing Panel
            Customer currentCustomer = controller.getCurrentCustomer();
            if (currentCustomer != null) {
                currentParcelPanel.setLayout(new BorderLayout());
                JLabel currentProcessingLabel = new JLabel(
                    "Currently Processing: " + currentCustomer.getName() + 
                    " (Parcel: " + currentCustomer.getParcelID() + ")"
                );
                currentProcessingLabel.setBorder(BorderFactory.createTitledBorder("Current Processing"));
                currentParcelPanel.add(currentProcessingLabel, BorderLayout.CENTER);
            }

            // Log display (moved to the last column as requested)
            String logContents = Log.getInstance().getLogContents();
            logArea.setText(logContents);
            logArea.setCaretPosition(logArea.getDocument().getLength());

            // Refresh panels
            parcelPanel.revalidate();
            parcelPanel.repaint();
            queuePanel.revalidate();
            queuePanel.repaint();
            currentParcelPanel.revalidate();
            currentParcelPanel.repaint();
        });
    }
}