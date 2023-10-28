package ru.clevertec.product.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.product.ProductTestData;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.service.impl.ProductServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    ProductMapper productMapper;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    @Captor
    ArgumentCaptor<Product> productCaptor;

    @Test
    public void shouldGet() {
        // given
        UUID id = ProductTestData.builder().build().getUuid();
        Product foundProduct = ProductTestData.builder().build().toProduct();
        InfoProductDto expected = new InfoProductDto(
                foundProduct.getUuid(),
                foundProduct.getName(),
                foundProduct.getDescription(),
                foundProduct.getPrice()
        );
        Mockito.when(productRepository.findById(id))
                .thenReturn(Optional.of(foundProduct));
        Mockito.when(productMapper.toInfoProductDto(foundProduct))
                .thenReturn(expected);

        // when
        InfoProductDto actual = productService.get(id);

        // then
        Mockito.verify(productRepository).findById(Mockito.any());
        Mockito.verify(productMapper).toInfoProductDto(Mockito.any());
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    public void shouldThrowWhenCantGet() {
        // given
        UUID id = ProductTestData.builder().build().getUuid();
        Mockito.when(productRepository.findById(id))
                .thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> productService.get(id))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    public void shouldReturnAll() {
        // given
        List<InfoProductDto> expected = List.of(
                new InfoProductDto(
                        UUID.fromString("c973a91e-39a5-46d7-8635-7184934afc20"),
                        "name_1",
                        "desc_1",
                        BigDecimal.ONE
                ),
                new InfoProductDto(
                        UUID.fromString("98080396-d0e6-44ab-ba71-89b30c2b6632"),
                        "name_2",
                        "desc_2",
                        BigDecimal.TEN
                ),
                new InfoProductDto(
                        UUID.fromString("bdab99fd-be47-4bf5-8172-504d6193baa8"),
                        "name_3",
                        "desc_3",
                        BigDecimal.TEN.multiply(BigDecimal.TEN)
                )
        );
        List<Product> expectedFromRepo = expected
                .stream()
                .map(info -> Product.builder()
                        .uuid(info.uuid())
                        .name(info.name())
                        .description(info.description())
                        .price(info.price())
                        .created(LocalDateTime.MIN)
                        .build())
                .toList();

        Mockito.when(productRepository.findAll())
                .thenReturn(expectedFromRepo);
        IntStream.range(0, expected.size())
                .forEach(
                        i -> Mockito.when(productMapper.toInfoProductDto(expectedFromRepo.get(i)))
                                .thenReturn(expected.get(i))
                );

        // when
        List<InfoProductDto> actual = productService.getAll();

        // then
        Mockito.verify(productRepository).findAll();
        Mockito.verify(productMapper, Mockito.times(expected.size())).toInfoProductDto(Mockito.any());
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void shouldCreate() {
        // given
        ProductDto dto = ProductTestData.builder().build().toProductDto();
        Product product = ProductTestData.builder().build().toProduct();
        product.setUuid(null);
        UUID expected = UUID.fromString("ae64ba98-d244-469f-9901-9b9166921423");
        Product expectedProduct = ProductTestData.builder().build().toProduct();
        expectedProduct.setUuid(expected);
        Mockito.when(productMapper.toProduct(dto)).thenReturn(product);
        Mockito.when(productRepository.save(product)).thenReturn(expectedProduct);

        // when
        UUID actualId = productService.create(dto);

        // then
        Mockito.verify(productMapper).toProduct(Mockito.any());
        Mockito.verify(productRepository).save(Mockito.any());
        assertThat(actualId).isEqualTo(expected);
    }

    @Test
    public void shouldUpdate() {
        // given
        ProductDto dto = ProductTestData.builder().build().toProductDto();
        Product product = ProductTestData.builder().build().toProduct();
        product.setUuid(null);
        UUID uuid = UUID.fromString("ae64ba98-d244-469f-9901-9b9166921423");
        Mockito.when(productMapper.toProduct(dto)).thenReturn(product);

        // when
        productService.update(uuid, dto);

        // then
        Mockito.verify(productMapper).toProduct(dto);
        Mockito.verify(productRepository).save(productCaptor.capture());
        Product captured = productCaptor.getValue();
        assertThat(captured.getUuid()).isEqualTo(uuid);
        assertThat(captured.getName()).isEqualTo(product.getName());
        assertThat(captured.getDescription()).isEqualTo(product.getDescription());
        assertThat(captured.getPrice()).isEqualTo(product.getPrice());
        assertThat(captured.getCreated()).isEqualTo(product.getCreated());
    }

    @ParameterizedTest
    @ValueSource(strings = "ae64ba98-d244-469f-9901-9b9166921423")
    public void shouldDelete(String id) {
        // given
        UUID uuid = UUID.fromString(id);
        Mockito.doNothing().when(productRepository).delete(uuid);

        // when
        productService.delete(uuid);

        // then
        Mockito.verify(productRepository).delete(uuid);
    }
}
