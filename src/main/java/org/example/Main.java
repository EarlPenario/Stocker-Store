package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Frame frame = new Frame();
        FireStoreConnection fireStoreConnection = new FireStoreConnection();
        ArrayList<Product> tableData = fireStoreConnection.getAllProducts();

        for (int i = 0; i < tableData.size(); i++) {
            frame.table.addProduct(tableData.get(i));
        }

        frame.add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddProduct addFrame = new AddProduct();
                addFrame.addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = addFrame.nameField.getText();
                        String brand = addFrame.brandField.getText();
                        String type = addFrame.typeField.getText();
                        String expiry = addFrame.getSelectedDate();
                        String quantity = addFrame.getSelectedQuantity();
                        String price = addFrame.priceField.getText();
                        String sellingPrice = addFrame.sellingPriceField.getText();

                        String totalPrice = "0";
                        try {
                            int qty = Integer.parseInt(quantity);
                            double sellPrice = Double.parseDouble(sellingPrice);
                            totalPrice = String.format("%.2f", qty * sellPrice);
                        } catch (NumberFormatException ex) {

                        }

                        if (name.isEmpty() || brand.isEmpty() || type.isEmpty() ||
                                expiry.isEmpty() || quantity.isEmpty() || price.isEmpty() || sellingPrice.isEmpty()) {
                            JOptionPane.showMessageDialog(addFrame, "Please fill all fields and select a date");
                            return;
                        }

                        try {
                            int quantityValue = Integer.parseInt(quantity);
                            if (quantityValue <= 0) {
                                JOptionPane.showMessageDialog(addFrame, "Please enter a valid quantity number");
                                return;
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(addFrame, "Please enter a valid quantity number");
                            return;
                        }
                        try {
                            double priceValue = Double.parseDouble(price);
                            price = String.format("%.2f", priceValue);
                            if (priceValue <= 0) {
                                JOptionPane.showMessageDialog(addFrame, "Please enter a valid price number");
                                return;
                            }

                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(addFrame, "Please enter a valid price number");
                            return;
                        }

                        try {
                            double priceValue = Double.parseDouble(sellingPrice);
                            sellingPrice = String.format("%.2f", priceValue);
                            if (priceValue <= 0) {
                                JOptionPane.showMessageDialog(addFrame, "Please enter a valid price number");
                                return;
                            }

                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(addFrame, "Please enter a valid price number");
                            return;
                        }

                        Product product = new Product(name, brand, type, expiry, quantity, price, sellingPrice, totalPrice);
                        frame.table.addProduct(product);

                        fireStoreConnection.addEmployee(name, brand, type, expiry, quantity, price, sellingPrice, totalPrice);

                        JOptionPane.showMessageDialog(addFrame, "Product added successfully!");

                        addFrame.dispose();

                        ArrayList<Product> freshData = fireStoreConnection.getAllProducts();
                        frame.table.products.clear();
                        for (Product p : freshData) {
                            frame.table.addProduct(p);
                        }
                        frame.table.fireTableDataChanged();

                        ExpirationChecker checker = new ExpirationChecker(frame, freshData);
                        checker.checkExpiringProducts();
                    }
                });
            }
        });
        frame.remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] index = frame.productTable.getSelectedRows();
                if (index.length > 0) {
                    for (int i = index.length - 1; i >= 0; i--) {
                        int row = index[i];
                        Product product = frame.table.products.get(row);
                        fireStoreConnection.deleteProduct(row);
                        frame.table.removeProduct(row);

                        ArrayList<Product> freshData = fireStoreConnection.getAllProducts();
                        frame.table.products.clear();
                        for (Product p : freshData) {
                            frame.table.addProduct(p);
                        }
                        frame.table.fireTableDataChanged();

                        ExpirationChecker checker = new ExpirationChecker(frame, freshData);
                        checker.checkExpiringProducts();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select products to remove");
                }
            }
        });
        frame.update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = frame.productTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select a product to update");
                    return;
                }

                Product selectedProduct = frame.table.getProduct(selectedRow);

                if (selectedProduct != null) {
                    UpdateProduct updateFrame = new UpdateProduct(selectedProduct);

                    updateFrame.setUpdateButtonAction(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Product updatedProduct = updateFrame.getUpdatedProduct();

                            if (updatedProduct.getName().isEmpty() ||
                                    updatedProduct.getBrand().isEmpty() ||
                                    updatedProduct.getType().isEmpty() ||
                                    updatedProduct.getExpiry().isEmpty() ||
                                    updatedProduct.getQuantity().isEmpty() ||
                                    updatedProduct.getPrice().isEmpty() ||
                                    updatedProduct.getSellingPrice().isEmpty()) {
                                JOptionPane.showMessageDialog(updateFrame, "Please fill in all fields");
                                return;
                            }

                            try {
                                int quantityValue;
                                quantityValue = Integer.parseInt(updatedProduct.getQuantity());
                                if (quantityValue <= 0) {
                                    JOptionPane.showMessageDialog(updateFrame,
                                            "Please enter a valid quantity number");
                                    return;
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(updateFrame, "Please enter a valid quantity number");
                                return;
                            }

                            try {
                                double priceValue;
                                priceValue = Double.parseDouble(updatedProduct.getPrice());
                                if (priceValue <= 0) {
                                    JOptionPane.showMessageDialog(updateFrame, "Please enter a valid price number");
                                    return;
                                }
                                updatedProduct.setPrice(String.format("%.2f", priceValue));
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(updateFrame, "Price must be a price number");
                                return;
                            }

                            try {
                                double priceValue;
                                priceValue = Double.parseDouble(updatedProduct.getSellingPrice());
                                if (priceValue <= 0) {
                                    JOptionPane.showMessageDialog(updateFrame, "Please enter a valid price number");
                                    return;
                                }
                                updatedProduct.setSellingPrice(String.format("%.2f", priceValue));
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(updateFrame, "Price must be a price number");
                                return;
                            }

                            frame.table.updateProduct(selectedRow, updatedProduct);

                            fireStoreConnection.updateProduct(updatedProduct, selectedRow);

                            JOptionPane.showMessageDialog(updateFrame, "Product updated successfully!");

                            updateFrame.dispose();

                            ArrayList<Product> freshData = fireStoreConnection.getAllProducts();
                            frame.table.products.clear();
                            for (Product p : freshData) {
                                frame.table.addProduct(p);
                            }
                            frame.table.fireTableDataChanged();

                            ExpirationChecker checker = new ExpirationChecker(frame, freshData);
                            checker.checkExpiringProducts();
                        }
                    });
                }
            }
        });
        Timer startupTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Refresh data and check expiration
                ArrayList<Product> freshData = fireStoreConnection.getAllProducts();
                frame.table.products.clear();
                for (Product product : freshData) {
                    frame.table.addProduct(product);
                }
                frame.table.fireTableDataChanged();

                ExpirationChecker checker = new ExpirationChecker(frame, freshData);
                checker.checkExpiringProducts();
            }
        });
        startupTimer.setRepeats(false);
        startupTimer.start();

        frame.checkExpiration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Product> freshData = fireStoreConnection.getAllProducts();
                frame.table.products.clear();
                for (Product product : freshData) {
                    frame.table.addProduct(product);
                }
                frame.table.fireTableDataChanged();

                ExpirationChecker checker = new ExpirationChecker(frame, freshData);
                checker.checkExpiringProducts();
            }
        });
    }
}