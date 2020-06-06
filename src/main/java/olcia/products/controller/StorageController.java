package olcia.products.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olcia.products.persistence.Photo;
import olcia.products.persistence.Product;
import olcia.products.persistence.Storage;
import olcia.products.repository.PhotoRepository;
import olcia.products.repository.StorageRepository;
import olcia.products.request.AddProductRequest;
import olcia.products.request.AddStorageRequest;
import olcia.products.request.ChangeNameRequest;
import olcia.products.request.ChangePhotoRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
public class StorageController {

    private final StorageRepository storageRepository;
    private final PhotoRepository photoRepository;

    @Transactional
    @DeleteMapping(path = "/storage/{id}")
    public void deleteStorage(@PathVariable("id") long id) {
        storageRepository.deleteById(id);
    }

    @Transactional
    @PutMapping(path = "/storage/{id}/name")
    public void changeName(@PathVariable("id") long id, @RequestBody ChangeNameRequest changeNameRequest) {
        storageRepository.findById(id)
                .ifPresent(storage -> storage.changeName(changeNameRequest.getName()));
    }

    @Transactional(readOnly = true)
    @GetMapping(path = "/storages")
    public List<Storage> storages() {
        return storageRepository.findAll();
    }

    @Transactional
    @PostMapping(path = "/storage/{id}/storages")
    public void addStorage(@PathVariable("id") long id, @RequestBody AddStorageRequest addStorageRequest) {
        storageRepository.findById(id)
                .ifPresent(storage -> {
                    Photo photo = photo(addStorageRequest.getPhotoId());
                    storage.addStorage(addStorageRequest.getName(), photo);
                });
    }

    @Transactional
    @PostMapping(path = "/storage/{id}/products")
    public void addProduct(@PathVariable("id") long id, @RequestBody AddProductRequest addProductRequest) {
        storageRepository.findById(id)
                .ifPresent(storage -> {
                    Photo photo = photo(addProductRequest.getPhotoId());
                    Product product = Product.builder()
                            .quantity(addProductRequest.getQuantity())
                            .expirationDate(addProductRequest.getExpirationDate())
                            .barcode(addProductRequest.getBarcode())
                            .photo(photo)
                            .storageDateTime(LocalDateTime.now())
                            .name(addProductRequest.getName())
                            .storage(storage)
                            .build();
                    storage.addProduct(product, addProductRequest.getCapacity());
                });
    }

    @Transactional
    @PutMapping(path = "/storage/{id}/photo")
    public void changePhoto(@PathVariable("id") long id, @RequestBody ChangePhotoRequest changePhotoRequest) {
        storageRepository.findById(id)
                .ifPresent(storage -> {
                    Photo oldPhoto = storage.getPhoto();
                    if (oldPhoto != null) {
                        if (new File(oldPhoto.getPath()).delete()) {
                            photoRepository.delete(oldPhoto);
                        } else {
                            log.warn("Photo " + oldPhoto.getPath() + " could not be deleted");
                        }
                    }
                    Photo photo = photo(changePhotoRequest.getId());
                    storage.changePhoto(photo);
                });
    }

    private Photo photo(Long id) {
        return id == null ? null : photoRepository.findById(id)
                .orElse(null);
    }

}
