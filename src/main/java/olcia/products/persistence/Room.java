package olcia.products.persistence;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Set;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @ManyToOne
    private Photo photo;

    @OrderBy("name")
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private Set<Storage> storages;

    @JsonGetter
    public Double capacity() {
        OptionalDouble average = storages.stream()
                .map(Storage::capacity)
                .filter(Objects::nonNull)
                .mapToDouble(capacity -> capacity)
                .average();
        return average.isPresent() ? average.getAsDouble() : null;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void addStorage(String name, Photo photo) {
        this.storages.add(Storage.builder()
                .name(name)
                .room(this)
                .photo(photo)
                .build());
    }

}
