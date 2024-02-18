package ru.sanctio.productmicroservice.service;

import ru.sanctio.productmicroservice.service.dto.CreateProductDto;

public interface ProductService {

    String createProduct(CreateProductDto createProductDto);

}
