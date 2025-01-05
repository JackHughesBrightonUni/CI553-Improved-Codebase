package clients.backDoor;

/**
 * The BackDoor Controller
 */
public class BackDoorController {
    private BackDoorModel model = null;
    private BackDoorView view = null;

    /**
     * Constructor
     * @param model The model
     * @param view  The view from which the interaction came
     */
    public BackDoorController(BackDoorModel model, BackDoorView view) {
        this.view = view;
        this.model = model;
    }

    /**
     * Query interaction from view
     * @param pn The product number to be checked
     */
    public void doQuery(String pn) {
        if (pn == null || pn.trim().isEmpty()) {
            view.update(model, "Invalid product number. Please enter a valid number.");
            return;
        }

        try {
            model.doQuery(pn.trim());
        } catch (Exception e) {
            view.update(model, "Error querying product: " + e.getMessage());
        }
    }

    /**
     * RStock interaction from view
     * @param pn       The product number to be re-stocked
     * @param quantity The quantity to be re-stocked
     */
    public void doRStock(String pn, String quantity) {
        if (pn == null || pn.trim().isEmpty()) {
            view.update(model, "Invalid product number. Please enter a valid number.");
            return;
        }

        if (quantity == null || quantity.trim().isEmpty()) {
            view.update(model, "Invalid quantity. Please enter a valid number.");
            return;
        }

        try {
            model.doRStock(pn.trim(), quantity.trim());
        } catch (Exception e) {
            view.update(model, "Error restocking product: " + e.getMessage());
        }
    }

    /**
     * Clear interaction from view
     */
    public void doClear() {
        try {
            model.doClear();
        } catch (Exception e) {
            view.update(model, "Error clearing products: " + e.getMessage());
        }
    }
}