package ssf.assessment.poapp.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Delivery {
    
    @NotBlank(message = "Required field")
    @Size(min = 3, message = "Name must be miniumum 2 characters")
    private String name;

    @NotBlank(message = "Required field")
    private String address;

    // constructors
    public Delivery() {
    }

    // Getters/setter
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    
}
