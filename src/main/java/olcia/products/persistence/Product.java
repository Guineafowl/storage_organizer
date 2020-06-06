package olcia.products.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Quantity quantity;

    @NotNull
    private LocalDateTime storageDateTime = LocalDateTime.now();

    @JsonIgnore
    @ManyToOne(optional = false)
    private Storage storage;

    @ManyToOne
    private Photo photo;

    private LocalDate expirationDate;
    private Long barcode;
    private String name;

    public void changeQuantity(double quantity, double storageCapacity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = new Quantity(this.quantity.getUnit() == Unit.PCS ? Math.round(quantity) : quantity, this.quantity.getUnit());
        storage.changeCapacity(storageCapacity);
    }

    public void changeName(String name) {
        this.name = name;
    }

}
