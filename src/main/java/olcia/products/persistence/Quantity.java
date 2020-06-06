package olcia.products.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Embeddable
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Quantity {

    @JsonProperty("value")
    private double quantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Unit unit;

}
