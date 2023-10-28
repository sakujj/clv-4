package ru.clevertec.product.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.product.ProductTestData;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.repository.impl.InMemoryProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class InMemoryProductRepositoryTest {

    private InMemoryProductRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new InMemoryProductRepository();
    }

    public void shouldFindById() {
        // given
        UUID idToFindBy = ProductTestData.builder().build().getUuid();
        Product expected = ProductTestData.builder().build().toProduct();

        // when
        Optional<Product> actual = repository.findById(idToFindBy);

        // then
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(expected);
    }

    public void shouldFindAll() {
        // given
        List<Product> expected = repository.getRepository().values()
                .stream()
                .toList();

        // when
        List<Product> actual = repository.findAll();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @ParameterizedTest
    @MethodSource
    public void shouldSave(Product expected) {
        // given

        // when
        Product actual = repository.save(expected);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    static List<Product> shouldSave() {
        return List.of(
                Product.builder()
                        .uuid(UUID.fromString("xx73a91e-39a5-46d7-8635-7184934afc20"))
                        .name("Product_xx")
                        .description("Description_xx")
                        .price(BigDecimal.valueOf(1))
                        .created(LocalDateTime.MIN)
                        .build(),
                Product.builder()
                        .uuid(UUID.fromString("yy73a91e-39a5-46d7-8635-7184934afc20"))
                        .name("Product_yy")
                        .description("Description_yy")
                        .price(BigDecimal.valueOf(2))
                        .created(LocalDateTime.MIN)
                        .build()
        );
    }

    public void shouldDelete() {
        // given
        Product productToDelete = repository.getRepository().values().iterator().next();

        // when
        repository.delete(productToDelete.getUuid());

        // then
        assertThat(repository.getRepository()).doesNotContainKey(productToDelete.getUuid());
        assertThat(repository.getRepository()).doesNotContainValue(productToDelete);
    }
}
