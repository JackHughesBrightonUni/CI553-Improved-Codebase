package clients.backDoor;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.StockException;
import middle.StockReadWriter;

import java.util.Observable;

/**
 * Implements the Model of the back door client
 */
public class BackDoorModel extends Observable {
    private Basket theBasket = null;            // Bought items
    private String pn = "";                      // Product being processed

    private StockReadWriter theStock = null;

    // Low stock threshold
    private final int lowStockThreshold = 5;

    /**
     * Construct the model of the back door client
     * @param mf The factory to create the connection objects
     */
    public BackDoorModel(MiddleFactory mf) {
        try {
            theStock = mf.makeStockReadWriter();        // Database access
        } catch (Exception e) {
            DEBUG.error("BackDoorModel.constructor\n%s", e.getMessage());
        }

        theBasket = makeBasket();                     // Initial Basket
    }

    /**
     * Get the Basket of products
     * @return basket
     */
    public Basket getBasket() {
        return theBasket;
    }

    /**
     * Check The current stock level
     * @param productNum The product number
     */
    public void doCheck(String productNum) {
        pn = productNum.trim();                    // Product no.
        checkLowStock(pn);                         // Check for low stock
    }

    /**
     * Query 
     * @param productNum The product number of the item
     */
    public void doQuery(String productNum) {
        String theAction = "";
        pn = productNum.trim();                    // Product no.
        try {
            if (theStock.exists(pn)) {              // Stock Exists?
                Product pr = theStock.getDetails(pn); // Product
                theAction =                             // Display 
                        String.format("%s : %7.2f (%2d) ",   //
                                pr.getDescription(),                  // description
                                pr.getPrice(),                        // price
                                pr.getQuantity());                   // quantity
                setChanged(); notifyObservers(theAction);            // Notify product details

                // Check for low stock and notify if applicable
                checkLowStock(pn);
            } else {
                theAction =                             // Inform
                        "Unknown product number " + pn;       // product number
                setChanged(); notifyObservers(theAction);
            }
        } catch (StockException e) {
            theAction = e.getMessage();
            setChanged(); notifyObservers(theAction);
        }
    }

    /**
     * Re stock 
     * @param productNum The product number of the item
     * @param quantity How many to be added
     */
    public void doRStock(String productNum, String quantity) {
        String theAction = "";
        theBasket = makeBasket();
        pn = productNum.trim();                    // Product no.
        int amount = 0;
        try {
            String aQuantity = quantity.trim();
            try {
                amount = Integer.parseInt(aQuantity);   // Convert
                if (amount < 0)
                    throw new NumberFormatException("-ve");
            } catch (Exception err) {
                theAction = "Invalid quantity";
                setChanged(); notifyObservers(theAction);
                return;
            }

            if (theStock.exists(pn)) {              // Stock Exists?
                theStock.addStock(pn, amount);          // Re stock
                Product pr = theStock.getDetails(pn);   // Get details
                theBasket.add(pr);                      //
                theAction = "Restocked successfully";   // Notify success
                setChanged(); notifyObservers(theAction);

                // Check for low stock post restock
                checkLowStock(pn);
            } else {
                theAction =                             // Inform Unknown
                        "Unknown product number " + pn;       // product number
                setChanged(); notifyObservers(theAction);
            }
        } catch (StockException e) {
            theAction = e.getMessage();
            setChanged(); notifyObservers(theAction);
        }
    }

    /**
     * Clear the product()
     */
    public void doClear() {
        String theAction = "";
        theBasket.clear();                        // Clear s. list
        theAction = "Enter Product Number";       // Set display
        setChanged(); notifyObservers(theAction);  // Inform the observer view that model changed
    }

    /**
     * Check for low stock levels and notify observers if stock is below the threshold.
     * @param productNum The product number to check.
     */
    private void checkLowStock(String productNum) {
        try {
            if (theStock.exists(productNum)) {
                Product pr = theStock.getDetails(productNum);
                if (pr.getQuantity() < lowStockThreshold) {
                    String lowStockMessage = String.format(
                            "LOW STOCK ALERT: %s (%d remaining)",
                            pr.getDescription(),
                            pr.getQuantity());
                    setChanged(); notifyObservers(lowStockMessage);
                }
            }
        } catch (StockException e) {
            DEBUG.error("BackDoorModel.checkLowStock\n%s", e.getMessage());
        }
    }

    /**
     * Return an instance of a Basket
     * @return a new instance of a Basket
     */
    protected Basket makeBasket() {
        return new Basket();
    }
}
