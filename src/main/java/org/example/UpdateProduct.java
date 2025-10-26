package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateProduct extends JFrame {
    JLabel name, price, quantity, expiry, brand, type, sellingPrice, totalPrice;
    JTextField nameField, priceField, brandField, typeField, sellingPriceField, totalPriceField;
    JButton updateButton;
    JSpinner dateSpinner;
    JComboBox<String> quantityCombo;
    Container container;
    GridBagLayout layout;
    GridBagConstraints constraints;
    SimpleDateFormat dateFormat;
    Product originalProduct;

    public UpdateProduct(Product product) {
        this.originalProduct = product;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        name = new JLabel("Name");
        price = new JLabel("Standard Price");
        quantity = new JLabel("Quantity");
        expiry = new JLabel("Expiry Date");
        brand = new JLabel("Brand");
        type = new JLabel("Type");
        sellingPrice = new JLabel("Selling Price");
        totalPrice = new JLabel("Total Price");

        nameField = new JTextField(12);
        priceField = new JTextField(8);
        brandField = new JTextField(10);
        typeField = new JTextField(10);
        sellingPriceField = new JTextField(8);
        totalPriceField = new JTextField(8);
        totalPriceField.setEditable(false);

        updateButton = new JButton("Update Product");

        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);

        quantityCombo = new JComboBox<>();

        populateQuantityOptions();

        dateSpinner.setPreferredSize(new Dimension(120, dateSpinner.getPreferredSize().height));
        quantityCombo.setPreferredSize(new Dimension(100, quantityCombo.getPreferredSize().height));
        priceField.setPreferredSize(new Dimension(80, priceField.getPreferredSize().height));

        quantityCombo.addActionListener(e -> calculateTotalPrice());
        sellingPriceField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateTotalPrice();
            }
        });

        populateFieldsWithProductData(product);

        container = this.getContentPane();
        layout = new GridBagLayout();
        container.setLayout(layout);
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.weightx = 1;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        container.add(name, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        container.add(nameField, constraints);

        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        container.add(brand, constraints);

        constraints.gridx = 4;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        container.add(brandField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        container.add(type, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        container.add(typeField, constraints);

        constraints.gridx = 3;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        container.add(expiry, constraints);

        constraints.gridx = 4;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        container.add(dateSpinner, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        container.add(quantity, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        container.add(quantityCombo, constraints);

        constraints.gridx = 3;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        container.add(price, constraints);

        constraints.gridx = 4;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        container.add(priceField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        container.add(sellingPrice,constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        container.add(sellingPriceField,constraints);

        constraints.gridx = 3;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        container.add(totalPrice,constraints);

        constraints.gridx = 4;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        container.add(totalPriceField,constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 6;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        container.add(updateButton, constraints);

        this.setTitle("Update Product");
        this.setVisible(true);
        this.setSize(650, 250);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    private void populateQuantityOptions() {
        quantityCombo.removeAllItems();
        for (int i = 1; i <= 5; i++) {
            quantityCombo.addItem(String.valueOf(i));
        }
        quantityCombo.addItem("10");
        quantityCombo.addItem("15");
        quantityCombo.addItem("20");
        quantityCombo.addItem("25");
        quantityCombo.addItem("50");
        quantityCombo.addItem("100");
        quantityCombo.addItem("200");
        quantityCombo.addItem("500");
        quantityCombo.setEditable(true);
        quantityCombo.setToolTipText("Select from list or enter custom quantity");
    }

    public void populateFieldsWithProductData(Product product) {
        nameField.setText(product.getName());
        brandField.setText(product.getBrand());
        typeField.setText(product.getType());
        quantityCombo.setSelectedItem(product.getQuantity());
        priceField.setText(product.getPrice());

        try {
            Date expiryDate = dateFormat.parse(product.getExpiry());
            dateSpinner.setValue(expiryDate);
        } catch (Exception e) {
            dateSpinner.setValue(new Date());
        }
    }

    public String getSelectedDate() {
        Date selectedDate = (Date) dateSpinner.getValue();
        return dateFormat.format(selectedDate);
    }

    public String getSelectedQuantity() {
        return quantityCombo.getSelectedItem().toString();
    }

    public void setUpdateButtonAction(java.awt.event.ActionListener action) {
        updateButton.addActionListener(action);
    }

    public void calculateTotalPrice() {
        try {
            int qty = Integer.parseInt(getSelectedQuantity());
            double sellingPrice = Double.parseDouble(sellingPriceField.getText().trim());
            double total = qty * sellingPrice;
            totalPriceField.setText(String.format("%.2f", total));
        } catch (NumberFormatException e) {
            totalPriceField.setText("");
        }
    }

    public Product getUpdatedProduct() {
        return new Product(
                nameField.getText().trim(),
                brandField.getText().trim(),
                typeField.getText().trim(),
                getSelectedDate(),
                getSelectedQuantity().trim(),
                priceField.getText().trim(),
                sellingPriceField.getText().trim(),
                totalPriceField.getText().trim()

        );
    }
}