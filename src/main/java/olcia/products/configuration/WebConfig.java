package olcia.products.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String uploadPath;

    public WebConfig(@Value("${photo.upload.path}") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://89.73.105.93:3000",
                        "http://192.168.0.2:3000",
                        "http://codebart.pl:3000"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:///" + uploadPath)
                .setCacheControl(CacheControl.noCache().mustRevalidate())
                .setCachePeriod(0);

    }

}
