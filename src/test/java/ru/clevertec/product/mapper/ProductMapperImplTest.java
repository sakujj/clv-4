package ru.clevertec.product.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.product.ProductTestData;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.mapper.impl.ProductMapperImpl;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.map;

public class ProductMapperImplTest {
    private ProductMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ProductMapperImpl();
    }


    @Test
    public void shouldMapToProduct() {
        // given
        ProductDto dtoToBeMapped = ProductTestData.builder().build().toProductDto();
        ProductTestData expected = ProductTestData.builder()
                .withUuid(null)
                .withName(dtoToBeMapped.name())
                .withPrice(dtoToBeMapped.price())
                .withDescription(dtoToBeMapped.description())
                .build();

        // when
        Product actual = mapper.toProduct(dtoToBeMapped);

        // then
        assertThat(actual.getUuid()).isEqualTo(expected.getUuid());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getCreated()).isEqualTo(expected.getCreated());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
    }

    @Test
    public void shouldMapToInfoProductDto() {
        // given
        Product productToBeMapped = ProductTestData.builder().build().toProduct();
        InfoProductDto expected = new InfoProductDto(
                productToBeMapped.getUuid(),
                productToBeMapped.getName(),
                productToBeMapped.getDescription(),
                productToBeMapped.getPrice()
        );

        // when
        InfoProductDto actual = mapper.toInfoProductDto(productToBeMapped);

        // then
        assertThat(actual.uuid()).isEqualTo(expected.uuid());
        assertThat(actual.name()).isEqualTo(expected.name());
        assertThat(actual.description()).isEqualTo(expected.description());
        assertThat(actual.price()).isEqualTo(expected.price());
    }

    @Test
    public void shouldMerge() {
        // given
        Product productToMerge = Product.builder()
                .uuid(ProductTestData.builder().build().getUuid())
                .created(ProductTestData.builder().build().getCreated())
                .build();
        ProductDto productDtoToMerge = ProductTestData.builder().build().toProductDto();
        Product expected = ProductTestData.builder().build().toProduct();

        // when
        Product actual = mapper.merge(productToMerge, productDtoToMerge);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
