package olcia.products.request;

import lombok.Data;
import olcia.products.persistence.Quantity;

import java.time.LocalDate;

@Data
public class AddProductRequest {

    private Quantity quantity;
    private LocalDate expirationDate;
    private Double capacity;
    private Long barcode;
    private Long photoId;
    private String name;

}
