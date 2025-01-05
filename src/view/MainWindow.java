package view;

import javax.swing.*;
import java.awt.*;
import model.ParcelMap;
import util.Log;
import model.CustomersQueue;
import controller.Manager;
import model.Parcel;
import model.Customer;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class MainWindow extends JFrame {
    private JPanel parcelPanel;
    private JPanel queuePanel;
    private JPanel currentParcelPanel;
    private JTextArea logArea;
    private Manager controller;

    public MainWindow(Manager controller) {
        this.controller = controller;
        setTitle("Parcel Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setLayout(new BorderLayout());

        parcelPanel = new JPanel();
        parcelPanel.setLayout(new BoxLayout(parcelPanel, BoxLayout.Y_AXIS));
        
        queuePanel = new JPanel();
        queuePanel.setLayout(new BoxLayout(queuePanel, BoxLayout.Y_AXIS));
        
        currentParcelPanel = new JPanel();
        currentParcelPanel.setLayout(new BorderLayout());
        currentParcelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        logArea = new JTextArea(10, 30);
        logArea.setEditable(false);

        JScrollPane parcelScrollPane = new JScrollPane(parcelPanel);
        parcelScrollPane.setBorder(BorderFactory.createTitledBorder("Pending Parcels"));
        parcelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        JScrollPane queueScrollPane = new JScrollPane(queuePanel);
        queueScrollPane.setBorder(BorderFactory.createTitledBorder("Customer Queue"));
        queueScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Log"));

        JPanel controlPanel = createControlPanel();

        JSplitPane centerSplitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT, 
            parcelScrollPane, 
            queueScrollPane
        );
        centerSplitPane.setDividerLocation(500);

        add(currentParcelPanel, BorderLayout.NORTH);
        add(centerSplitPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(logScrollPane, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
    }
    
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
        parcelPanel = new JPanel();
        parcelPanel.setLayout(new BoxLayout(parcelPanel, BoxLayout.Y_AXIS));
        JScrollPane parcelScrollPane = new JScrollPane(parcelPanel);
        parcelScrollPane.setBorder(BorderFactory.createTitledBorder("Parcels"));
        parcelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        queuePanel = new JPanel();
        queuePanel.setLayout(new BoxLayout(queuePanel, BoxLayout.Y_AXIS));
        JScrollPane queueScrollPane = new JScrollPane(queuePanel);
        queueScrollPane.setBorder(BorderFactory.createTitledBorder("Customer Queue"));
        queueScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


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
            
            parcelPanel.removeAll();
            queuePanel.removeAll();
            currentParcelPanel.removeAll();

            Map<String, Parcel> parcels = controller.getParcelMap().getParcels();
            parcels.values().stream()
                .filter(parcel -> parcel.getStatus() != util.StatusP.COLLECTED)
                .forEach(parcel -> {
                    JPanel parcelInfoPanel = new JPanel();
                    parcelInfoPanel.setLayout(new BoxLayout(parcelInfoPanel, BoxLayout.Y_AXIS));
                    parcelInfoPanel.setBorder(BorderFactory.createEtchedBorder());
                    parcelInfoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
                    
                    parcelInfoPanel.add(new JLabel(String.format("ID: %s", parcel.getParcelID())));
                    parcelInfoPanel.add(new JLabel(String.format("Weight: %.2f kg", parcel.getWeight())));
                    parcelInfoPanel.add(new JLabel(String.format("Storage Days: %d", parcel.getNoOfDays())));
                    parcelInfoPanel.add(new JLabel(String.format("Dimensions: %s", parcel.getDimensions())));
                    parcelInfoPanel.add(new JLabel(String.format("Status: %s", parcel.getStatus())));
                    
                    parcelPanel.add(parcelInfoPanel);
                    parcelPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                });

            Customer currentCustomer = controller.getCurrentCustomer();
            if (currentCustomer != null) {
                JPanel currentProcessingInfo = new JPanel();
                currentProcessingInfo.setLayout(new GridLayout(0, 1, 5, 5));
                currentProcessingInfo.setBorder(BorderFactory.createTitledBorder("Current Processing"));
                
                Parcel currentParcel = controller.getParcelMap().pFind(currentCustomer.getParcelID());
                if (currentParcel != null) {
                    currentProcessingInfo.add(new JLabel("Customer: " + currentCustomer.getName()));
                    currentProcessingInfo.add(new JLabel("Queue Number: " + currentCustomer.getQno()));
                    currentProcessingInfo.add(new JLabel("Parcel ID: " + currentParcel.getParcelID()));
                    currentProcessingInfo.add(new JLabel(String.format("Weight: %.2f kg", currentParcel.getWeight())));
                    currentProcessingInfo.add(new JLabel("Storage Days: " + currentParcel.getNoOfDays()));
                    currentProcessingInfo.add(new JLabel("Dimensions: " + currentParcel.getDimensions()));
                    currentProcessingInfo.add(new JLabel("Collection Fee: $" + 
                        String.format("%.2f", (5.0 + (currentParcel.getWeight() * 2.0) + (currentParcel.getNoOfDays() * 1.0)))));
                }
                currentParcelPanel.add(currentProcessingInfo, BorderLayout.CENTER);
            } else {
                JLabel noProcessingLabel = new JLabel("No customer currently being processed");
                noProcessingLabel.setBorder(BorderFactory.createTitledBorder("Current Processing"));
                currentParcelPanel.add(noProcessingLabel, BorderLayout.CENTER);
            }

            Queue<Customer> customers = controller.getQueue().getCustomers();
            customers.forEach(customer -> {
                JPanel customerInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                customerInfoPanel.add(new JLabel(String.format(
                    "Queue #%d: %s (Parcel: %s)",
                    customer.getQno(), customer.getName(), 
                    customer.getParcelID()
                )));
                queuePanel.add(customerInfoPanel);
            });

            String logContents = Log.getInstance().getLogContents();
            logArea.setText(logContents);
            logArea.setCaretPosition(logArea.getDocument().getLength());

            revalidateAndRepaintAll();
        });
    }
    private void revalidateAndRepaintAll() {
        parcelPanel.revalidate();
        parcelPanel.repaint();
        queuePanel.revalidate();
        queuePanel.repaint();
        currentParcelPanel.revalidate();
        currentParcelPanel.repaint();
    }
}