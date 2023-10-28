package ru.clevertec.product;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(setterPrefix = "with")
@Data
public class ProductTestData {
    @Builder.Default
    private UUID uuid = UUID.fromString("c973a91e-39a5-46d7-8635-7184934afc20");

    @Builder.Default
    private String name = "Product_1";

    @Builder.Default
    private String description = "Description_1";

    @Builder.Default
    private BigDecimal price = BigDecimal.valueOf(100);

    @Builder.Default
    private LocalDateTime created = LocalDateTime.MIN;

    public Product toProduct() {
        return Product.builder()
                .uuid(uuid)
                .name(name)
                .description(description)
                .price(price)
                .created(created)
                .build();
    }

    public ProductDto toProductDto() {
        return new ProductDto(name, description, price);
    }

}
