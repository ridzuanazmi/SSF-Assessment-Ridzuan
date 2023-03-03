package ssf.assessment.poapp.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import ssf.assessment.poapp.Model.Cart;
import ssf.assessment.poapp.Model.Item;

@Service
public class PurchaseOrderService {

    public Cart aggregate(Cart cart, Item item) {

        List<Item> items = cart.getContents(); 
        System.out.println(items); 

        // loop through the list to see if there are similar item name already existing in the cart 
        for (int i = 0; i < items.size(); i++) {

            String name = items.get(i).getItemName(); // name is the item Name for existing cart items 
            System.out.println(name); 
            if (name.equals(item.getItemName())) {
                // add to the list
                Integer qty = items.get(i).getQuantity(); 
                qty += item.getQuantity(); 
                items.get(i).setQuantity(qty);
                return cart; 
            }
        }
        // if reach this step it means itemName != all names in the cart
        cart.addItemToCart(item);
        return cart; 
    }
}
