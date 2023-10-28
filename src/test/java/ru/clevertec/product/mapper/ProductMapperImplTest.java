package ru.clevertec.product.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.clevertec.product.ProductTestData;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductMapperImplTest {
    private ProductMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(ProductMapper.class);
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
                .withCreated(null)
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
