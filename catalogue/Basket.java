package catalogue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Formatter;
import java.util.Locale;

/**
 * A collection of products,
 * used to record the products that are to be wished to be purchased.
 * Updated to support bulk purchase discounts.
 * 
 * @author Mike Smith University of Brighton
 * @version 2.3
 */
public class Basket extends ArrayList<Product> implements Serializable {
    private static final long serialVersionUID = 1;
    private int theOrderNum = 0; // Order number
    private double discountAmount = 0.0; // Total discount applied
    private boolean isDiscountApplied = false; // Flag for discount status

    /**
     * Constructor for a basket which is
     * used to represent a customer order/ wish list
     */
    public Basket() {
        theOrderNum = 0;
    }

    /**
     * Set the customers unique order number
     * Valid order Numbers 1 .. N
     * 
     * @param anOrderNum A unique order number
     */
    public void setOrderNum(int anOrderNum) {
        theOrderNum = anOrderNum;
    }

    /**
     * Returns the customers unique order number
     * 
     * @return the customers order number
     */
    public int getOrderNum() {
        return theOrderNum;
    }

    /**
     * Add a product to the Basket.
     * Product is appended to the end of the existing products
     * in the basket.
     * 
     * @param pr A product to be added to the basket
     * @return true if successfully adds the product
     */
    @Override
    public boolean add(Product pr) {
        return super.add(pr); // Call add in ArrayList
    }

    /**
     * Applies a discount to the basket if the conditions are met.
     * 
     * @param percentage The discount percentage to apply (e.g., 10 for 10%).
     */
    public void applyDiscount(double percentage) {
        if (size() >= 5) { // Threshold for bulk purchase discount
            double total = calculateTotal();
            discountAmount = (total * percentage) / 100;
            isDiscountApplied = true;
        } else {
            clearDiscount();
        }
    }

    /**
     * Clears any applied discount from the basket.
     */
    public void clearDiscount() {
        discountAmount = 0.0;
        isDiscountApplied = false;
    }

    /**
     * Calculates the total price of the products in the basket.
     * 
     * @return The total price before any discounts.
     */
    private double calculateTotal() {
        double total = 0.0;
        for (Product pr : this) {
            total += pr.getPrice() * pr.getQuantity();
        }
        return total;
    }

    /**
     * Returns a description of the products in the basket suitable for printing.
     * Includes details of any discounts applied.
     * 
     * @return a string description of the basket products
     */
    public String getDetails() {
        Locale uk = Locale.UK;
        StringBuilder sb = new StringBuilder(256);
        Formatter fr = new Formatter(sb, uk);
        String csign = (Currency.getInstance(uk)).getSymbol();
        double total = calculateTotal();

        if (theOrderNum != 0)
            fr.format("Order number: %03d\n", theOrderNum);

        if (this.size() > 0) {
            for (Product pr : this) {
                int number = pr.getQuantity();
                fr.format("%-7s", pr.getProductNum());
                fr.format("%-14.14s ", pr.getDescription());
                fr.format("(%3d) ", number);
                fr.format("%s%7.2f", csign, pr.getPrice() * number);
                fr.format("\n");
            }
            fr.format("----------------------------\n");
            fr.format("Total                       %s%7.2f\n", csign, total);

            if (isDiscountApplied) {
                fr.format("Discount                   -%s%7.2f\n", csign, discountAmount);
                fr.format("Final Total                %s%7.2f\n", csign, total - discountAmount);
            }

            fr.close();
        }

        return sb.toString();
    }
}
