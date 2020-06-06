package olcia.products.persistence;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Room room;

    @OrderBy("expirationDate")
    @OneToMany(mappedBy = "storage", cascade = CascadeType.ALL)
    private Set<Product> products;

    @OrderBy("name")
    @OneToMany(mappedBy = "parentStorage", cascade = CascadeType.ALL)
    private Set<Storage> storages;

    @JsonIgnore
    @ManyToOne
    private Storage parentStorage;

    @JsonIgnore
    private Double capacity;

    @ManyToOne
    private Photo photo;

    private String name;

    public void changePhoto(Photo photo) {
        this.photo = photo;
    }

    public void addProduct(Product product, double capacity) {
        if (!storages.isEmpty()) {
            throw new IllegalStateException("Cannot add product to storage that has substorages");
        }
        products.add(product);
        changeCapacity(capacity);
    }

    @JsonGetter
    public Double capacity() {
        if (this.capacity != null) {
            return this.capacity;
        }
        double sum = 0d;
        int size = 0;
        for (Storage childrenStorage : storages) {
            Double capacity = childrenStorage.capacity();
            if (capacity != null) {
                sum += capacity;
                size++;
            }
        }
        if (size == 0) {
            return null;
        }
        return sum / size;
    }

    public void changeCapacity(double capacity) {
        if (capacity > 1 || capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be lower than 0 or greater than 1");
        }
        this.capacity = capacity;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void addStorage(String name, Photo photo) {
        if (!products.isEmpty()) {
            throw new IllegalStateException("Cannot add substorage to not empty storage");
        }
        Storage storage = Storage.builder()
                .name(name)
                .photo(photo)
                .parentStorage(this)
                .build();
        storages.add(storage);
        capacity = null;
    }

    public Room topStorageRoom() {
        if (room != null) {
            return room;
        }
        return parentStorage.topStorageRoom();
    }

    @JsonGetter
    public String fullStorageName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name);
        Storage currentStorage = this;
        while (true) {
            currentStorage = currentStorage.parentStorage;
            if (currentStorage != null) {
                stringBuilder.insert(0, currentStorage.name + "->");
            } else {
                return stringBuilder.toString();
            }
        }
    }

}
