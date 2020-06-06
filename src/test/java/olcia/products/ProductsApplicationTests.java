package olcia.products;

import olcia.products.persistence.Product;
import olcia.products.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest
class ProductsApplicationTests {

  private final ProductRepository productRepository;

  @Autowired
  public ProductsApplicationTests(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Test
  void repositoryTest() {
    List<Product> products = productRepository.productsDaysToExpiration(1);
    System.out.println(products);
  }

  @Test
  void contextLoads() {


    LocalDateTime today = LocalDateTime.now();
    LocalDateTime tomorrow = today.plusDays(1);
    System.out.println(today);

    System.out.println(tomorrow);

    long daysToNewYear = today.until(LocalDate.of(2020, 1, 1).atStartOfDay(), ChronoUnit.DAYS);
    System.out.println(daysToNewYear);
  }

}
