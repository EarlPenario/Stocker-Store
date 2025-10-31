package org.example;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;

public class Table extends AbstractTableModel {
    ArrayList<Product>products;
    String [] columns={"Name","Brand","Type","Expiry","Quantity","Price", "Selling Price", "Total Price"};
    public Table(){
        products=new ArrayList<>();
    }
    @Override
    public int getRowCount() {
        return products.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }
    public void addProduct(Product product){
        products.add(product);
        fireTableDataChanged();
    }
    public void removeProduct(int index){
        products.remove(index);
        fireTableDataChanged();
    }
    public void updateProduct(int index, Product updatedProduct) {
        if (index >= 0 && index < products.size()) {
            products.set(index, updatedProduct);
            fireTableRowsUpdated(index, index);
        }
    }
    public Product getProduct(int index) {
        if (index >= 0 && index < products.size()) {
            return products.get(index);
        }
        return null;
    }

    public Color getRowColor(int row) {
        Product product = products.get(row);
        if (product.isExpired()) {
            return new Color(255, 200, 200); // Light red for expired
        } else if (product.isExpiringSoon()) {
            return new Color(255, 255, 200); // Light yellow for expiring soon
        }
        return null; // Default color
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product product=products.get(rowIndex);
        if(columnIndex==0){
            return product.name;
        } else if (columnIndex==1) {
            return product.brand;
        }else if (columnIndex==2) {
            return product.type;
        }else if (columnIndex==3) {
            return product.expiry;
        }else if (columnIndex==4) {
            return product.quantity;
        }else if (columnIndex==5){
            return product.price;
        } else if (columnIndex==6) {
            return product.sellingPrice;
        }else {
            return product.totalPrice;
        }
    }
}
