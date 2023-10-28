package ru.clevertec.product.repository.impl;

import lombok.Getter;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Getter
public class InMemoryProductRepository implements ProductRepository {

    private final Map<UUID, Product> repository = new HashMap<>(Map.of(
            UUID.fromString("c973a91e-39a5-46d7-8635-7184934afc20"), Product.builder()
                    .uuid(UUID.fromString("c973a91e-39a5-46d7-8635-7184934afc20"))
                    .name("Product_1")
                    .description("Description_1")
                    .price(BigDecimal.valueOf(100))
                    .created(LocalDateTime.MIN)
                    .build(),
            UUID.fromString("98080396-d0e6-44ab-ba71-89b30c2b6632"), Product.builder()
                    .uuid(UUID.fromString("98080396-d0e6-44ab-ba71-89b30c2b6632"))
                    .name("Product_2")
                    .description("Description_2")
                    .price(BigDecimal.valueOf(200))
                    .created(LocalDateTime.MIN)
                    .build(),
            UUID.fromString("bdab99fd-be47-4bf5-8172-504d6193baa8"), Product.builder()
                    .uuid(UUID.fromString("bdab99fd-be47-4bf5-8172-504d6193baa8"))
                    .name("Product_3")
                    .description("Description_3")
                    .price(BigDecimal.valueOf(300))
                    .created(LocalDateTime.MIN)
                    .build()
    ));

    @Override
    public Optional<Product> findById(UUID uuid) {
        return Optional.ofNullable(repository.get(uuid));
    }

    @Override
    public List<Product> findAll() {
        return repository.values().stream().toList();
    }

    @Override
    public Product save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Null can't be saved");
        }

        if (product.getUuid() == null) {
            UUID randomUuid = UUID.randomUUID();
            product.setUuid(randomUuid);
        }

        repository.put(product.getUuid(), product);
        return product;
    }

    @Override
    public void delete(UUID uuid) {
        repository.remove(uuid);
    }
}
