package be.tvanwinckel;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InventoryConfiguration implements WebMvcConfigurer {

    private final InventoryInterceptor inventoryInterceptor;

    public InventoryConfiguration(final InventoryInterceptor inventoryInterceptor) {
        this.inventoryInterceptor = inventoryInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(inventoryInterceptor);
    }
}
