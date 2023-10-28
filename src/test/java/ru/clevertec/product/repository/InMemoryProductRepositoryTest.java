package ru.clevertec.product.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import ru.clevertec.product.ProductTestData;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.repository.impl.InMemoryProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class InMemoryProductRepositoryTest {

    private InMemoryProductRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new InMemoryProductRepository();
    }

    @Test
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

    @Test
    public void shouldNotFindByIdWhenAbsent() {
        // given
        UUID idToFindBy = UUID.fromString("ae64ba98-d244-469f-9901-9b9166921423");
        Optional<Product> expected = Optional.empty();

        // when
        Optional<Product> actual = repository.findById(idToFindBy);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
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
        assertThat(repository.getRepository()).containsEntry(
                actual.getUuid(), actual
        );
    }

    static List<Product> shouldSave() {
        return List.of(
                Product.builder()
                        .uuid(null)
                        .name("Product_xx")
                        .description("Description_xx")
                        .price(BigDecimal.valueOf(1))
                        .created(LocalDateTime.MIN)
                        .build(),
                Product.builder()
                        .uuid(null)
                        .name("Product_yy")
                        .description("Description_yy")
                        .price(BigDecimal.valueOf(2))
                        .created(LocalDateTime.MIN)
                        .build()
        );
    }

    @ParameterizedTest
    @NullSource
    public void shouldThrowWhenSavingNull(Product nullProduct) {
        // given

        // when
        // then
        assertThatThrownBy(() -> repository.save(nullProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldUpdate() {
        // given
        Product productToUpdate = repository.getRepository().values().iterator().next();
        productToUpdate.setName("Updated name");
        productToUpdate.setDescription("Updated description");
        productToUpdate.setPrice(productToUpdate.getPrice().multiply(BigDecimal.TEN));
        productToUpdate.setCreated(LocalDateTime.MAX);

        // when
        Product actual = repository.save(productToUpdate);

        // then
        assertThat(actual).isEqualTo(productToUpdate);
        assertThat(repository.getRepository()).containsEntry(
                actual.getUuid(), actual
        );
    }

    @Test
    public void shouldDelete() {
        // given
        Product productToDelete = repository.getRepository().values().iterator().next();

        // when
        repository.delete(productToDelete.getUuid());

        // then
        assertThat(repository.getRepository()).doesNotContainKey(
                productToDelete.getUuid()
        );
    }
}
