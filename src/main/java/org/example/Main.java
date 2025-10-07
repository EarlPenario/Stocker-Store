package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Frame frame=new Frame();
        FireStoreConnection fireStoreConnection=new FireStoreConnection();
        ArrayList<Product>tableData=fireStoreConnection.getAllProducts();

        for(int i=0;i<tableData.size();i++){
            frame.table.addProduct(tableData.get(i));
        }

        frame.add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddProduct addFrame=new AddProduct();
                addFrame.addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name=addFrame.nameField.getText();
                        String brand=addFrame.brandField.getText();
                        String type=addFrame.typeField.getText();
                        String expiry=addFrame.getSelectedDate();
                        String quantity=addFrame.getSelectedQuantity();
                        String price=addFrame.priceField.getText();

                        if(name.isEmpty() || brand.isEmpty() || type.isEmpty() ||
                                expiry.isEmpty() || quantity.isEmpty() || price.isEmpty()) {
                            JOptionPane.showMessageDialog(addFrame, "Please fill all fields and select a date");
                            return;
                        }

                        try {
                            int quantityValue=Integer.parseInt(quantity);
                            if (quantityValue <= 0) {
                                JOptionPane.showMessageDialog(addFrame, "Please enter a valid quantity number");
                                return;
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(addFrame,"Please enter a valid quantity number");
                            return;
                        }
                        try {
                            double priceValue = Double.parseDouble(price);
                            price = String.format("%.2f", priceValue);
                            if (priceValue <= 0) {
                                JOptionPane.showMessageDialog(addFrame,"Please enter a valid price number");
                                return;
                            }

                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(addFrame,"Please enter a valid price number");
                            return;
                        }

                        Product product=new Product(name,brand,type,expiry,quantity,price);
                        frame.table.addProduct(product);

                        fireStoreConnection.addEmployee(name,brand,type,expiry,quantity,price);

                        JOptionPane.showMessageDialog(addFrame, "Product added successfully!");

                        addFrame.dispose();
                    }
                });
            }
        });
        frame.remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int []index=frame.productTable.getSelectedRows();
                if(index.length>0){
                    for(int i=index.length-1;i>=0;i--){
                        int row=index[i];
                       Product product=frame.table.products.get(row);
                        fireStoreConnection.deleteProduct(row);
                        frame.table.removeProduct(row);
                    }
                }else {
                    JOptionPane.showMessageDialog(frame, "Please select products to remove");
                }
            }
        });
        frame.update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = frame.productTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame,"Please select a product to update");
                    return;
                }

                Product selectedProduct = frame.table.getProduct(selectedRow);

                if (selectedProduct != null) {
                    UpdateProduct updateFrame = new UpdateProduct(selectedProduct);

                    updateFrame.setUpdateButtonAction(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Product updatedProduct = updateFrame.getUpdatedProduct();

                            if(updatedProduct.getName().isEmpty() ||
                                    updatedProduct.getBrand().isEmpty() ||
                                    updatedProduct.getType().isEmpty() ||
                                    updatedProduct.getExpiry().isEmpty() ||
                                    updatedProduct.getQuantity().isEmpty() ||
                                    updatedProduct.getPrice().isEmpty()) {
                                JOptionPane.showMessageDialog(updateFrame,"Please fill in all fields");
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
                                JOptionPane.showMessageDialog(updateFrame,"Please enter a valid quantity number");
                                return;
                            }

                            try {
                                double priceValue;
                                priceValue = Double.parseDouble(updatedProduct.getPrice());
                                if (priceValue <= 0) {
                                    JOptionPane.showMessageDialog(updateFrame,"Please enter a valid price number");
                                    return;
                                }
                                updatedProduct.setPrice(String.format("%.2f", priceValue));
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(updateFrame,"Price must be a price number");
                                return;
                            }

                            frame.table.updateProduct(selectedRow, updatedProduct);

                            fireStoreConnection.updateProduct(updatedProduct, selectedRow);

                            JOptionPane.showMessageDialog(updateFrame,"Product updated successfully!");

                            updateFrame.dispose();
                        }
                    });
                }
            }
        });
    }
}