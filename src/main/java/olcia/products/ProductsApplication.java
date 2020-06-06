package olcia.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties
public class ProductsApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductsApplication.class, args);
  }

}
