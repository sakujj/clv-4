package ru.clevertec.product.service.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.service.ProductService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductRepository productRepository;

    @Override
    public InfoProductDto get(UUID uuid) {
        Product foundProduct = productRepository.findById(uuid)
                .orElseThrow(() -> new ProductNotFoundException(uuid));

        return mapper.toInfoProductDto(foundProduct);
    }

    @Override
    public List<InfoProductDto> getAll() {
        return productRepository.findAll()
                .stream()
                .map(mapper::toInfoProductDto)
                .toList();
    }

    @Override
    public UUID create(ProductDto productDto) {
        Product productToCreate = mapper.toProduct(productDto);
        return productRepository.save(productToCreate).getUuid();
    }

    @Override
    public void update(UUID uuid, ProductDto productDto) {
        Product productToUpdate = mapper.toProduct(productDto);
        productToUpdate.setUuid(uuid);
        productRepository.save(productToUpdate);
    }

    @Override
    public void delete(UUID uuid) {
        productRepository.delete(uuid);
    }
}
