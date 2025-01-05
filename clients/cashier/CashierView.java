package clients.cashier;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * View of the model 
 */
public class CashierView implements Observer {
    private static final int H = 300;       // Height of window pixels
    private static final int W = 400;       // Width  of window pixels

    private static final String CHECK = "Check";
    private static final String BUY = "Buy";
    private static final String BOUGHT = "Bought/Pay";

    private final JLabel pageTitle = new JLabel();
    private final JLabel theAction = new JLabel();
    private final JTextField theInput = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtCheck = new JButton(CHECK);
    private final JButton theBtBuy = new JButton(BUY);
    private final JButton theBtBought = new JButton(BOUGHT);

    // New UI components for discounts
    private final JLabel discountLabel = new JLabel("Discount: £0.00");
    private final JLabel finalTotalLabel = new JLabel("Final Total: £0.00");

    private StockReadWriter theStock = null;
    private OrderProcessing theOrder = null;
    private CashierController cont = null;

    /**
     * Construct the view
     * @param rpc   Window in which to construct
     * @param mf    Factory to deliver order and stock objects
     * @param x     x-coordinate of position of window on screen 
     * @param y     y-coordinate of position of window on screen  
     */

    public CashierView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theStock = mf.makeStockReadWriter();        // Database access
            theOrder = mf.makeOrderProcessing();        // Process order
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        Container cp = rpc.getContentPane();    // Content Pane
        Container rootWindow = (Container) rpc; // Root Window
        cp.setLayout(null);                      // No layout manager
        rootWindow.setSize(W, H);                // Size of Window
        rootWindow.setLocation(x, y);

        Font f = new Font("Monospaced", Font.PLAIN, 12);  // Font f is

        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Thank You for Shopping at MiniStore");
        cp.add(pageTitle);

        theBtCheck.setBounds(16, 25 + 60 * 0, 80, 40);    // Check Button
        theBtCheck.addActionListener(                   // Call back code
                e -> cont.doCheck(theInput.getText()));
        cp.add(theBtCheck);                             // Add to canvas

        theBtBuy.setBounds(16, 25 + 60 * 1, 80, 40);      // Buy button 
        theBtBuy.addActionListener(                     // Call back code
                e -> cont.doBuy());
        cp.add(theBtBuy);                               // Add to canvas

        theBtBought.setBounds(16, 25 + 60 * 3, 80, 40);   // Bought Button
        theBtBought.addActionListener(                  // Call back code
                e -> cont.doBought());
        cp.add(theBtBought);                            // Add to canvas

        theAction.setBounds(110, 25, 270, 20);           // Message area
        theAction.setText("");                          // Blank
        cp.add(theAction);                              // Add to canvas

        theInput.setBounds(110, 50, 270, 40);           // Input Area
        theInput.setText("");                          // Blank
        cp.add(theInput);                               // Add to canvas

        theSP.setBounds(110, 100, 270, 120);            // Scrolling pane
        theOutput.setText("");                         // Blank
        theOutput.setFont(f);                           // Uses font  
        cp.add(theSP);                                  // Add to canvas
        theSP.getViewport().add(theOutput);             // In TextArea

        // New labels for discounts and totals
        discountLabel.setBounds(110, 230, 270, 20);
        cp.add(discountLabel);

        finalTotalLabel.setBounds(110, 250, 270, 20);
        cp.add(finalTotalLabel);

        rootWindow.setVisible(true);                    // Make visible
        theInput.requestFocus();                        // Focus is here
    }

    /**
     * The controller object, used so that an interaction can be passed to the controller
     * @param c   The controller
     */

    public void setController(CashierController c) {
        cont = c;
    }

    /**
     * Update the view
     * @param modelC   The observed model
     * @param arg      Specific args 
     */
    @Override
    public void update(Observable modelC, Object arg) {
        CashierModel model = (CashierModel) modelC;
        String message = (String) arg;
        theAction.setText(message);

        Basket basket = model.getBasket();
        if (basket == null) {
            theOutput.setText("Customer's order");
            discountLabel.setText("Discount: £0.00");
            finalTotalLabel.setText("Final Total: £0.00");
        } else {
            theOutput.setText(basket.getDetails());

            // Extract discount and final total from Basket.getDetails
            // Assuming Basket.getDetails formats these as separate lines
            String[] details = basket.getDetails().split("\n");
            for (String line : details) {
                if (line.startsWith("Discount")) {
                    discountLabel.setText(line);
                } else if (line.startsWith("Final Total")) {
                    finalTotalLabel.setText(line);
                }
            }
        }

        theInput.requestFocus();               // Focus is here
    }
}
