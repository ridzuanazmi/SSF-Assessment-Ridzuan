package ssf.assessment.poapp.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Item {

    // fields with validation
    @NotNull(message = "Please select an item from the drop down list")
    private String itemName;

    @NotNull(message = "Please specify the quantity")
    @Min(value = 1, message = "Minimum 1 quantity")
    private Integer quantity;

    public Item() {
    }

    // getter setter
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Item [itemName=" + itemName + ", quantity=" + quantity + "]";
    }

}