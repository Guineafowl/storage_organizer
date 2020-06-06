package olcia.products.repository;

import olcia.products.persistence.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query(value = "select * from product where datediff(product.expiration_date, current_date()) = :days and product.expiration_date is not null", nativeQuery = true)
    List<Product> productsDaysToExpiration(@Param("days") int days);

    @Query(value = "select * from product where datediff(product.expiration_date, current_date()) <= :days and product.expiration_date is not null and datediff(product.expiration_date, current_date()) > 0 order by product.expiration_date", nativeQuery = true)
    List<Product> productsNearExpiration(@Param("days") int days);

    @Query(value = "select * from product where product.expiration_date < current_date() and product.expiration_date is not null order by product.expiration_date", nativeQuery = true)
    List<Product> expiredProducts();

}
