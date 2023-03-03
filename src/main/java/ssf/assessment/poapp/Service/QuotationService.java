package ssf.assessment.poapp.Service;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import ssf.assessment.poapp.Model.Cart;
import ssf.assessment.poapp.Model.Delivery;
import ssf.assessment.poapp.Model.Invoice;
import ssf.assessment.poapp.Model.Item;
import ssf.assessment.poapp.Model.Quotation;

@Service
public class QuotationService {

    public final static String URL = "https://quotation.chuklee.com";

    public Quotation getQuotations(List<String> items) throws Exception {

        Quotation quotation = new Quotation();
        JsonArrayBuilder itemArrayJson = Json.createArrayBuilder();

        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (int i = 0; i < items.size(); i++) {
            arrBuilder.add(items.get(i));
        }

        JsonArray jsonArray = itemArrayJson.build();

        RequestEntity<String> req = RequestEntity.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .body(jsonArray.toString(), String.class);

        // boilerplates
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = null;
        String payload = "";
        Integer statusCode = 0;

        try {
            resp = template.exchange(req, String.class); 
            payload = resp.getBody(); 
            statusCode = resp.getStatusCode().value(); 
        } catch (HttpClientErrorException ex) {
            
            payload = ex.getResponseBodyAsString();
            statusCode= ex.getStatusCode().value();
            quotation.addQuotation("error", (float) statusCode);
            return quotation;  
        } finally {
            System.out.printf(">>> Status Code: %d\n", statusCode);
            System.out.printf(">>> Payload: %s\n", payload);
        }

        // Parse the result to Weather
		JsonReader reader = Json.createReader(new StringReader(payload));
		JsonObject obj = reader.readObject();

        // get the quoteId and quotations from the Qsys
        quotation.setQuoteId(obj.getString("quoteId"));
        JsonArray obj2 = obj.getJsonArray("quotations"); 

        // loop thru array  to add the additional items I think
        for (int i = 0; i < obj2.size(); i++) {
            JsonObject obj3 = obj2.getJsonObject(i); 
            quotation.addQuotation(obj3.getString("item"), (float) obj3.getJsonNumber("unitPrice").doubleValue());
        }

        return quotation;
    }

    public List<String> getList(Cart cart) {

        List<Item> contents = cart.getContents();
        
        // change it to String
        List<String> contentsString = new LinkedList<>(); 

        for (int i = 0; i < contents.size(); i++) {
            contentsString.add(contents.get(i).getItemName()); 
        }
        return contentsString; 
    }

    // method to calculate total cost  from the cart. could not complete as there is an error "because "nameOfItem" is null" 
    public Float calculateCost(Quotation quote, Cart cart) {

        List<Item> contents = cart.getContents(); 
        Float cost = 0.0f; 

        for (int i = 0; i < contents.size(); i++) {
            // match cart item with quotation list 
            Float nameOfItem = quote.getQuotations().get(contents.get(i).getItemName());
            Integer quantity = contents.get(i).getQuantity(); 
            cost += (nameOfItem * quantity); 
        }
        return cost; 
    }

    // Create invoice
    public Invoice createInvoice(Quotation quote, Delivery delivery, Cart cart) {

        Invoice invoice = new Invoice(); 
        invoice.setInvoiceId(quote.getQuoteId()); 
        invoice.setDelivery(delivery);

        // total cost calculation 
        Float cost = calculateCost(quote, cart); 
        invoice.setTotal(cost);

        return invoice; 
    }

    // public JsonObject postAsJson(Cart cart, Delivery delivery) {

    //     List<Item> items = cart.getContents();

    //     JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
    //     JsonArrayBuilder itemArrayJson = Json.createArrayBuilder();

    //     for (Item item : items) {
    //         JsonObjectBuilder itemObjectBuilder = Json.createObjectBuilder()
    //                 .add("itemName", item.getItemName());
    //         itemArrayJson.add(itemObjectBuilder);
    //     }

    //     jsonObjectBuilder.add("items", itemArrayJson);

    //     JsonObject json = jsonObjectBuilder.build();

    //     RequestEntity<String> req = RequestEntity.post("https://quotation.chuklee.com/quotation")
    //             .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
    //             .body(json.toString(), String.class);

    //     RestTemplate template = new RestTemplate();

    //     ResponseEntity<String> resp = template.exchange(req, String.class);

    //     String payload = resp.getBody();

    //     System.out.println(payload);

    //     JsonReader reader = Json.createReader(new StringReader(payload));
    //     JsonObject obj = reader.readObject();

    //     return obj;
    // }
}
