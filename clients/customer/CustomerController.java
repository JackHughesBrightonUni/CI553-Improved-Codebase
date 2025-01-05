package clients.customer;

/**
 * The Customer Controller
 */
public class CustomerController {
    private CustomerModel model = null;
    private CustomerView view = null;

    /**
     * Constructor
     * @param model The model
     * @param view  The view from which the interaction came
     */
    public CustomerController(CustomerModel model, CustomerView view) {
        this.view = view;
        this.model = model;
    }

    /**
     * Check interaction from view
     * @param pn The product number to be checked
     */
    public void doCheck(String pn) {
        model.doCheck(pn);
    }

    /**
     * Clear interaction from view
     */
    public void doClear() {
        model.doClear();
    }

    /**
     * Filter products by price range interaction from view
     * @param minPriceInput The minimum price input as a string
     * @param maxPriceInput The maximum price input as a string
     */
    public void filterByPriceRange(String minPriceInput, String maxPriceInput) {
        try {
            double minPrice = Double.parseDouble(minPriceInput);
            double maxPrice = Double.parseDouble(maxPriceInput);

            if (minPrice > maxPrice) {
                view.updateErrorMessage("Invalid price range. Minimum price cannot exceed maximum price.");
                return;
            }

            model.filterProductsByPriceRange(minPrice, maxPrice);
        } catch (NumberFormatException e) {
            view.updateErrorMessage("Please enter valid numeric values for the price range.");
        }
    }
}