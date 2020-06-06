package olcia.products.notifications;

import olcia.products.persistence.Product;
import olcia.products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    private final ProductRepository productRepository;
    private final JavaMailSender mailSender;
    private final List<String> emailAddresses;

    @Autowired
    public NotificationService(ProductRepository productRepository, JavaMailSender mailSender, @Value("${notifications.email}") List<String> emailAddresses) {
        this.productRepository = productRepository;
        this.mailSender = mailSender;
        this.emailAddresses = emailAddresses;
    }

//    @Scheduled(fixedRate = 30_000)
    public void checkExpirationDate() throws MessagingException {
        List<Product> expiredProducts = productRepository.expiredProducts();
        List<Product> monthTillExpirationProducts = productRepository.productsDaysToExpiration(30);
        List<Product> twoWeeksOrLessTillExpirationProducts = productRepository.productsNearExpiration(14);
        if (!expiredProducts.isEmpty() || !monthTillExpirationProducts.isEmpty() || !twoWeeksOrLessTillExpirationProducts.isEmpty()) {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            helper.setSubject("Expiration report - " + LocalDate.now());
            helper.setTo(emailAddresses.toArray(new String[0]));

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<h1>This is your daily product expiration report:</h1>\n\n");
            if (!expiredProducts.isEmpty()) {
                stringBuilder.append("<h3>The following products <b><font color=\"orangered\">have expired</font></b></h3>\n");
                addProductList(expiredProducts, stringBuilder);
            }
            if (!twoWeeksOrLessTillExpirationProducts.isEmpty()) {
                stringBuilder.append("\n\n<h3>The following products will <b><font color=\"orange\">expire soon</font></b></h3>\n");
                addProductList(twoWeeksOrLessTillExpirationProducts, stringBuilder);
            }
            if (!monthTillExpirationProducts.isEmpty()) {
                stringBuilder.append("\n\n<h3>The following products will <b><font color=\"gold\">expire in one month</font></b></h3>\n");
                addProductList(monthTillExpirationProducts, stringBuilder);
            }

            helper.setText(stringBuilder.toString(), true);
            mailSender.send(message);
        }
    }

    private void addProductList(List<Product> products, StringBuilder stringBuilder) {
        stringBuilder.append("<table width=\"600\"><tr><th align=\"left\">Name</th><th align=\"left\">Expiration date</th><th align=\"left\">Room</th><th align=\"left\">Storage</th></tr>");
        for (Product product : products) {
            stringBuilder.append("<tr><td align=\"left\">");
            stringBuilder.append(product.getName());
            stringBuilder.append("</td><td align=\"left\">");
            stringBuilder.append(product.getExpirationDate());
            stringBuilder.append("</td><td align=\"left\">");
            stringBuilder.append(product.getStorage().topStorageRoom().getName());
            stringBuilder.append("</td><td align=\"left\">");
            stringBuilder.append(product.getStorage().fullStorageName());
            stringBuilder.append("</td></tr>");
        }
        stringBuilder.append("</table>");
    }


}
