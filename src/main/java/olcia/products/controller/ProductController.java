package olcia.products.controller;

import lombok.AllArgsConstructor;
import olcia.products.persistence.Product;
import olcia.products.repository.ProductRepository;
import olcia.products.request.ChangeNameRequest;
import olcia.products.request.ChangeQuantityRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api")
public class ProductController {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    @GetMapping(path = "/products/expiration/to")
    public List<Product> productsDaysToExpiration(@RequestParam("days") int days) {
        return productRepository.productsDaysToExpiration(days);
    }

    @Transactional(readOnly = true)
    @GetMapping(path = "/products/expiration/near")
    public List<Product> productsNearExpiration(@RequestParam("days") int days) {
        return productRepository.productsNearExpiration(days);
    }

    @Transactional(readOnly = true)
    @GetMapping(path = "/products/expired")
    public List<Product> expiredProducts() {
        return productRepository.expiredProducts();
    }

    @Transactional
    @PutMapping(path = "/product/{id}/quantity")
    public void changeQuantity(@PathVariable("id") long id, @RequestBody ChangeQuantityRequest changeQuantityRequest) {
        productRepository.findById(id)
                .ifPresent(product -> product.changeQuantity(changeQuantityRequest.getQuantity(), changeQuantityRequest.getCapacity()));
    }

    @Transactional
    @DeleteMapping(path = "/product/{id}")
    public void deleteProduct(@PathVariable("id") long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    @PutMapping(path = "/product/{id}/name")
    public void changeName(@PathVariable("id") long id, @RequestBody ChangeNameRequest changeNameRequest) {
        productRepository.findById(id)
                .ifPresent(product -> product.changeName(changeNameRequest.getName()));
    }

}
