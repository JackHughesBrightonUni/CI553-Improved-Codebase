package clients.customer;

import catalogue.Basket;
import clients.Picture;
import middle.MiddleFactory;
import middle.StockReader;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 */
public class CustomerView implements Observer {
    class Name {                              // Names of buttons
        public static final String CHECK = "Check";
        public static final String CLEAR = "Clear";
        public static final String FILTER = "Filter";
        public static final String TOGGLE_THEME = "Toggle Dark Mode";
    }

    private static final int H = 350;       // Height of window pixels
    private static final int W = 400;       // Width of window pixels

    private Container cp;                   // Class-level container for theme management

    private final JLabel pageTitle = new JLabel();
    private final JLabel theAction = new JLabel();
    private final JTextField theInput = new JTextField();
    private final JTextField minPriceInput = new JTextField();  // Min price input
    private final JTextField maxPriceInput = new JTextField();  // Max price input
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtCheck = new JButton(Name.CHECK);
    private final JButton theBtClear = new JButton(Name.CLEAR);
    private final JButton filterButton = new JButton(Name.FILTER); // Filter button
    private final JButton toggleThemeButton = new JButton(Name.TOGGLE_THEME); // Theme toggle button

    private boolean isDarkMode = false; // Tracks the current theme

    private Picture thePicture = new Picture(80, 80);
    private StockReader theStock = null;
    private CustomerController cont = null;

    /**
     * Construct the view
     * @param rpc   Window in which to construct
     * @param mf    Factory to deliver order and stock objects
     * @param x     x-coordinate of position of window on screen
     * @param y     y-coordinate of position of window on screen
     */
    public CustomerView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theStock = mf.makeStockReader();             // Database Access
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        cp = rpc.getContentPane(); // Initialize class-level container
        cp.setLayout(null); // No layout manager
        Container rootWindow = (Container) rpc; // Root Window
        rootWindow.setSize(W, H);                // Size of Window
        rootWindow.setLocation(x, y);

        Font f = new Font("Monospaced", Font.PLAIN, 12);  // Font f is

        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Search products");
        cp.add(pageTitle);

        theBtCheck.setBounds(16, 25 + 60 * 0, 80, 40);    // Check button
        theBtCheck.addActionListener(                   // Call back code
                e -> cont.doCheck(theInput.getText()));
        cp.add(theBtCheck);                           // Add to canvas

        theBtClear.setBounds(16, 25 + 60 * 1, 80, 40);    // Clear button
        theBtClear.addActionListener(                   // Call back code
                e -> cont.doClear());
        cp.add(theBtClear);                           // Add to canvas

        filterButton.setBounds(16, 25 + 60 * 2, 80, 40);  // Filter button
        filterButton.addActionListener(                 // Call back code
                e -> cont.filterByPriceRange(minPriceInput.getText(), maxPriceInput.getText()));
        cp.add(filterButton);                          // Add to canvas

        toggleThemeButton.setBounds(16, 25 + 60 * 3, 150, 40);  // Toggle theme button
        toggleThemeButton.addActionListener(e -> toggleTheme());
        cp.add(toggleThemeButton);                     // Add to canvas

        theAction.setBounds(110, 25, 270, 20);           // Message area
        theAction.setText(" ");                       // blank
        cp.add(theAction);                            // Add to canvas

        theInput.setBounds(110, 50, 270, 40);           // Product no area
        theInput.setText("");                          // Blank
        cp.add(theInput);                             // Add to canvas

        JLabel minPriceLabel = new JLabel("Min Price:");
        minPriceLabel.setBounds(110, 100, 100, 20);
        cp.add(minPriceLabel);

        minPriceInput.setBounds(200, 100, 80, 20);      // Min price area
        minPriceInput.setText("");                    // Blank
        cp.add(minPriceInput);                         // Add to canvas

        JLabel maxPriceLabel = new JLabel("Max Price:");
        maxPriceLabel.setBounds(110, 130, 100, 20);
        cp.add(maxPriceLabel);

        maxPriceInput.setBounds(200, 130, 80, 20);      // Max price area
        maxPriceInput.setText("");                    // Blank
        cp.add(maxPriceInput);                         // Add to canvas

        theSP.setBounds(110, 160, 270, 160);            // Scrolling pane
        theOutput.setText("");                         // Blank
        theOutput.setFont(f);                           // Uses font
        cp.add(theSP);                                  // Add to canvas
        theSP.getViewport().add(theOutput);             // In TextArea

        thePicture.setBounds(16, 25 + 60 * 4, 80, 80);   // Picture area
        cp.add(thePicture);                            // Add to canvas
        thePicture.clear();

        rootWindow.setVisible(true);                    // Make visible);
        theInput.requestFocus();                       // Focus is here
    }

    /**
     * The controller object, used so that an interaction can be passed to the controller
     * @param c   The controller
     */
    public void setController(CustomerController c) {
        cont = c;
    }

    /**
     * Update the view
     * @param modelC   The observed model
     * @param arg      Specific args
     */
    public void update(Observable modelC, Object arg) {
        CustomerModel model = (CustomerModel) modelC;
        String message = (String) arg;

        if (message.startsWith("Filtered Products:")) {
            // Display filtered product list in theOutput
            theOutput.setText(message);
        } else {
            // Display other messages in theAction label
            theAction.setText(message);
            theOutput.setText("");
        }

        ImageIcon image = model.getPicture();  // Image of product
        if (image == null) {
            thePicture.clear();                  // Clear picture
        } else {
            thePicture.set(image);             // Display picture
        }
        theInput.requestFocus();               // Focus is here
    }

    /**
     * Update the view with an error message
     * @param errorMessage The error message to display
     */
    public void updateErrorMessage(String errorMessage) {
        theAction.setText(errorMessage);
    }

    /**
     * Toggle the theme between Light and Dark Modes.
     */
    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        Color background = isDarkMode ? Color.DARK_GRAY : Color.WHITE;
        Color foreground = isDarkMode ? Color.LIGHT_GRAY : Color.BLACK;

        updateTheme(cp, background, foreground);

        // Explicitly update buttons
        updateButtonTheme(theBtCheck, background, foreground);
        updateButtonTheme(theBtClear, background, foreground);
        updateButtonTheme(filterButton, background, foreground);
        updateButtonTheme(toggleThemeButton, background, foreground);

        theOutput.setBackground(background);
        theOutput.setForeground(foreground);
    }

    /**
     * Update the theme for a specific button.
     * @param button The button to update.
     * @param background The background color.
     * @param foreground The foreground color.
     */
    private void updateButtonTheme(JButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setOpaque(true);  // Ensure the background is painted
        button.setBorderPainted(false); // Optional: Remove border for consistent appearance
        button.repaint(); // Force the button to repaint
    }

    /**
     * Recursively update theme for all components.
     * @param component The root component to update.
     * @param background The background color.
     * @param foreground The foreground color.
     */
    private void updateTheme(Component component, Color background, Color foreground) {
        component.setBackground(background);
        component.setForeground(foreground);

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
            }
          }
        }
      }