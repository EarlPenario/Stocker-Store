package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExpirationChecker extends JFrame {
    public Frame frame;
    public ArrayList<Product> products;
    public FireStoreConnection fireStoreConnection;

    public ExpirationChecker(Frame mainFrame, ArrayList<Product> products) {
        this.frame = mainFrame;
        this.products = products;
        this.fireStoreConnection = new FireStoreConnection();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(mainFrame);
    }

    public void checkExpiringProducts() {
        List<Product> expiringProducts = new ArrayList<>();
        List<Product> expiredProducts = new ArrayList<>();

        for (Product product : products) {
            if (product.isExpired()) {
                expiredProducts.add(product);
            } else if (product.isExpiringSoon()) {
                expiringProducts.add(product);
            }
        }

        if (!expiredProducts.isEmpty()) {
            showExpiredAlert(expiredProducts);
        } else if (!expiringProducts.isEmpty()) {
            showExpiringSoonAlert(expiringProducts);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No products are expiring soon or expired!",
                    "Expiration Check",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void showExpiredAlert(List<Product> expiredProducts) {
        StringBuilder message = new StringBuilder();
        message.append("The following products have EXPIRED:\n\n");

        for (Product product : expiredProducts) {
            message.append("• ").append(product.getName())
                    .append(" (").append(product.getBrand()).append(")")
                    .append(" - Expired on: ").append(product.getExpiry())
                    .append("\n");
        }

        message.append("\nThese products should be removed from inventory immediately!");

        JTextArea textArea = new JTextArea(message.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 200));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Expired Products Alert", JOptionPane.ERROR_MESSAGE);
    }

    public void showExpiringSoonAlert(List<Product> expiringProducts) {
        StringBuilder message = new StringBuilder();
        message.append("The following products are expiring soon:\n\n");

        for (Product product : expiringProducts) {
            int daysLeft = product.getDaysUntilExpiry();
            message.append("• ").append(product.getName())
                    .append(" (").append(product.getBrand()).append(")")
                    .append(" - Expires in: ").append(daysLeft).append(" days")
                    .append(" (Date: ").append(product.getExpiry()).append(")")
                    .append("\n");
        }

        message.append("\nConsider lowering prices to sell these quickly!");

        JTextArea textArea = new JTextArea(message.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 200));

        Object[] options = {"Update Prices", "Ignore for Now"};
        int choice = JOptionPane.showOptionDialog(this, scrollPane,
                "Products Expiring Soon", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            showProductSelectionDialog(expiringProducts);
        }
    }

    public void showProductSelectionDialog(List<Product> expiringProducts) {
        JDialog selectionDialog = new JDialog(this, "Select Product to Update", true);
        selectionDialog.setLayout(new BorderLayout());
        selectionDialog.setSize(600, 400);
        selectionDialog.setLocationRelativeTo(this);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Product product : expiringProducts) {
            int daysLeft = product.getDaysUntilExpiry();
            String item = String.format("%s (%s) - Expires in %d days - Current Price: $%s",
                    product.getName(), product.getBrand(), daysLeft, product.getSellingPrice());
            listModel.addElement(item);
        }

        JList<String> productList = new JList<>(listModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(productList);

        JButton updateButton = new JButton("Update Selected Product");
        JButton cancelButton = new JButton("Cancel");

        updateButton.addActionListener(e -> {
            int selectedIndex = productList.getSelectedIndex();
            if (selectedIndex != -1) {
                Product selectedProduct = expiringProducts.get(selectedIndex);
                selectionDialog.dispose();
                openUpdateDialog(selectedProduct);
            } else {
                JOptionPane.showMessageDialog(selectionDialog,
                        "Please select a product to update", "No Selection",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> selectionDialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        selectionDialog.add(new JLabel("Select a product to update its price:"), BorderLayout.NORTH);
        selectionDialog.add(listScrollPane, BorderLayout.CENTER);
        selectionDialog.add(buttonPanel, BorderLayout.SOUTH);

        selectionDialog.setVisible(true);
    }

    public void openUpdateDialog(Product product) {
        int productIndex = findProductIndex(product);

        if (productIndex != -1) {
            frame.productTable.setRowSelectionInterval(productIndex, productIndex);
            frame.productTable.scrollRectToVisible(
                    frame.productTable.getCellRect(productIndex, 0, true)
            );


            applyAutomaticPriceReduction(product, productIndex);

            frame.update.doClick();
            this.dispose();
        }
    }

    public int findProductIndex(Product product) {
        for (int i = 0; i < frame.table.products.size(); i++) {
            Product p = frame.table.products.get(i);
            if (p.getName().equals(product.getName()) &&
                    p.getBrand().equals(product.getBrand()) &&
                    p.getExpiry().equals(product.getExpiry())) {
                return i;
            }
        }
        return -1;
    }

    public void applyAutomaticPriceReduction(Product product, int productIndex) {
        try {
            double currentPrice = Double.parseDouble(product.getSellingPrice());
            double suggestedPrice = currentPrice * 0.7;


            Product updatedProduct = new Product(
                    product.getName(),
                    product.getBrand(),
                    product.getType(),
                    product.getExpiry(),
                    product.getQuantity(),
                    product.getPrice(),
                    String.format("%.2f", suggestedPrice),
                    calculateTotalPrice(product.getQuantity(), String.format("%.2f", suggestedPrice)),
                    true,
                    product.getSellingPrice()
            );


            frame.table.updateProduct(productIndex, updatedProduct);


            fireStoreConnection.updateProduct(updatedProduct, productIndex);

            String message = String.format(
                    "Automatic price reduction applied for %s:\n\n" +
                            "Original selling price: $%.2f\n" +
                            "New reduced price: $%.2f\n\n" +
                            "This is a 30%% discount to help sell the product before expiration.",
                    product.getName(), currentPrice, suggestedPrice
            );

            JOptionPane.showMessageDialog(frame, message,
                    "Price Reduction Applied", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Error applying price reduction: Invalid price format",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String calculateTotalPrice(String quantity, String sellingPrice) {
        try {
            int qty = Integer.parseInt(quantity);
            double sellPrice = Double.parseDouble(sellingPrice);
            return String.format("%.2f", qty * sellPrice);
        } catch (NumberFormatException e) {
            return "0.00";
        }
    }
}