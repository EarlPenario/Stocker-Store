package org.example;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddProduct extends JFrame {
    JLabel name, price, quantity, expiry, brand, type;
    JTextField nameField, priceField, brandField, typeField;
    JButton addButton;
    JSpinner dateSpinner;
    JComboBox<String> quantityCombo;
    Container container;
    GridBagLayout layout;
    GridBagConstraints constraints;
    SimpleDateFormat dateFormat;

    public AddProduct() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        name = new JLabel("Name");
        price = new JLabel("Price");
        quantity = new JLabel("Quantity");
        expiry = new JLabel("Expiry Date");
        brand = new JLabel("Brand");
        type = new JLabel("Type");

        nameField = new JTextField(12);
        priceField = new JTextField(8);
        brandField = new JTextField(10);
        typeField = new JTextField(10);

        addButton = new JButton("Add Product");

        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(new Date());

        quantityCombo = new JComboBox<>();
        populateQuantityOptions();

        dateSpinner.setPreferredSize(new Dimension(120, dateSpinner.getPreferredSize().height));
        quantityCombo.setPreferredSize(new Dimension(100, quantityCombo.getPreferredSize().height));
        priceField.setPreferredSize(new Dimension(80, priceField.getPreferredSize().height));

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
        constraints.gridwidth = 6;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        container.add(addButton, constraints);

        this.setVisible(true);
        this.setSize(650, 250);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    public void populateQuantityOptions() {
        quantityCombo.removeAllItems();

        for (int i = 1; i <= 10; i++) {
            quantityCombo.addItem(String.valueOf(i));
        }

        quantityCombo.addItem("15");
        quantityCombo.addItem("20");
        quantityCombo.addItem("25");
        quantityCombo.addItem("30");
        quantityCombo.addItem("40");
        quantityCombo.addItem("50");

        quantityCombo.addItem("100");
        quantityCombo.addItem("200");
        quantityCombo.addItem("500");
        quantityCombo.addItem("1000");

        quantityCombo.setSelectedItem("1");

        quantityCombo.setEditable(true);

        quantityCombo.setToolTipText("Select from list or enter custom quantity");
    }

    public String getSelectedDate() {
        Date selectedDate = (Date) dateSpinner.getValue();
        return dateFormat.format(selectedDate);
    }

    public String getSelectedQuantity() {
        return quantityCombo.getSelectedItem().toString();
    }

}