package ssf.assessment.poapp.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import ssf.assessment.poapp.Model.Cart;
import ssf.assessment.poapp.Model.Delivery;
import ssf.assessment.poapp.Model.Invoice;
import ssf.assessment.poapp.Model.Item;
import ssf.assessment.poapp.Model.Quotation;
import ssf.assessment.poapp.Service.PurchaseOrderService;
import ssf.assessment.poapp.Service.QuotationService;

@Controller
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService pOrderService;

    @Autowired
    private QuotationService quotationService;

    @GetMapping(path = {"/", "index.html"})
    public String getView(Model model, HttpSession session) {

        // check if cart has item using session
        Cart cart = (Cart) session.getAttribute("cart"); 
        if (null == cart) {
            cart = new Cart(); 
            session.setAttribute("cart", cart);
        }
        model.addAttribute("item", new Item()); 
        model.addAttribute("cart", cart);
        return "index";
    }

    // to populate the table of Item class 
    @PostMapping("/")
    public String postView(Model model, HttpSession session, @Valid Item item, BindingResult result) {

        Cart cart = (Cart) session.getAttribute("cart"); 

        // perform error check
        if (result.hasErrors()) {
            // reset page 
            model.addAttribute("item", item);
            model.addAttribute("cart", cart); 
            return "index"; 
        }
        // task 2
        cart = pOrderService.aggregate(cart, item); 
        session.setAttribute("cart", cart);
        session.setAttribute("item", item);

        model.addAttribute("item", item); 
        model.addAttribute("cart", cart);        

        return "index"; 
    }

    // Next button is pressed will run this
    @GetMapping("/shippingaddress")
    public String getAddress(Model model, HttpSession session) {

        Cart cart = (Cart) session.getAttribute("cart"); 
        System.out.println(">>> " + cart.toString());

        if (cart.getContents().isEmpty()) {
            model.addAttribute("item", new Item());
            model.addAttribute("cart", cart);            
            return "index"; 
        }

        model.addAttribute("delivery", new Delivery());

        Delivery delivery = (Delivery) session.getAttribute("delivery"); 
        if (null == delivery) {
            delivery = new Delivery(); 
            session.setAttribute("delivery", delivery);
        }

        model.addAttribute("delivery", delivery);
        return "view2"; 
    }

    @PostMapping("/quotation")
    public String postQuotation(Model model, HttpSession session, @Valid Delivery delivery, BindingResult result) throws Exception {

        if (result.hasErrors()) return "view2";

        Cart cart = (Cart) session.getAttribute("cart");   
        Item item = (Item) session.getAttribute("item");

        // Not working codes. I really tried 
        // List<String> items = quotationService.getList(cart);

        // Quotation quotation = quotationService.getQuotations(items);

        // Invoice invoice = quotationService.createInvoice(quotation, delivery, cart);
        
        // model.addAttribute("invoice", invoice); 
        
        model.addAttribute("item", item);
        model.addAttribute("cart", cart);
        model.addAttribute("delivery", delivery);
        return "view3";
    }

}
