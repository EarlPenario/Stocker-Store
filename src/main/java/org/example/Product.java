package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Product {
    String name,brand,type,expiry,quantity,price,sellingPrice,totalPrice;

    boolean priceReduced = false;
    String originalSellingPrice = "";

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Product(String name, String brand, String type, String expiry, String quantity, String price, String sellingPrice, String totalPrice) {
        this.name = name;
        this.brand = brand;
        this.type = type;
        this.expiry = expiry;
        this.quantity = quantity;
        this.price = price;
        this.sellingPrice=sellingPrice;
        this.totalPrice=totalPrice;
    }
    public boolean isPriceReduced() {
        return priceReduced;
    }

    public void setPriceReduced(boolean priceReduced) {
        this.priceReduced = priceReduced;
    }

    public String getOriginalSellingPrice() {
        return originalSellingPrice;
    }

    public void setOriginalSellingPrice(String originalSellingPrice) {
        this.originalSellingPrice = originalSellingPrice;
    }

    public boolean fiveDayExpiry() {
        try {
            Date expiryDate = dateFormat.parse(expiry);
            Date currentDate = new Date();
            long diff = expiryDate.getTime() - currentDate.getTime();
            long daysUntilExpiry = diff / (1000 * 60 * 60 * 24);
            return daysUntilExpiry <= 5 && daysUntilExpiry >= 0;
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean isExpiringSoon() {
        try {
            Date expiryDate = dateFormat.parse(expiry);
            Date currentDate = new Date();
            long diff = expiryDate.getTime() - currentDate.getTime();
            long daysUntilExpiry = diff / (1000 * 60 * 60 * 24);
            return daysUntilExpiry <= 14 && daysUntilExpiry >= 0;
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean isExpired() {
        try {
            Date expiryDate = dateFormat.parse(expiry);
            Date currentDate = new Date();
            return expiryDate.before(currentDate);
        } catch (ParseException e) {
            return false;
        }
    }

    public int getDaysUntilExpiry() {
        try {
            Date expiryDate = dateFormat.parse(expiry);
            Date currentDate = new Date();
            long diff = expiryDate.getTime() - currentDate.getTime();
            return (int) (diff / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            return Integer.MAX_VALUE;
        }
    }
}
