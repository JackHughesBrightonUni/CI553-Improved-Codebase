package middle;

import catalogue.Product;

import javax.swing.*;
import java.util.List;

/**
 * Interface for read access to the stock list.
 * @author Mike Smith University of Brighton
 * @version 2.1
 */
public interface StockReader {

    /**
     * Checks if the product exists in the stock list.
     * @param pNum Product number
     * @return true if exists otherwise false
     * @throws StockException if issue
     */
    boolean exists(String pNum) throws StockException;

    /**
     * Returns details about the product in the stock list.
     * @param pNum Product number
     * @return StockNumber, Description, Price, Quantity
     * @throws StockException if issue
     */
    Product getDetails(String pNum) throws StockException;

    /**
     * Returns an image of the product in the stock list.
     * @param pNum Product number
     * @return Image
     * @throws StockException if issue
     */
    ImageIcon getImage(String pNum) throws StockException;

    /**
     * Retrieves a list of products within the specified price range.
     * @param minPrice Minimum price (inclusive)
     * @param maxPrice Maximum price (inclusive)
     * @return List of products within the specified price range
     * @throws StockException if issue
     */
    List<Product> getProductsByPriceRange(double minPrice, double maxPrice) throws StockException;
}
