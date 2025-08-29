package com.bekassyl.productstoreapp.services;

import com.bekassyl.productstoreapp.exceptions.ProductNotFoundException;
import com.bekassyl.productstoreapp.models.Product;
import com.bekassyl.productstoreapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> getPage(int page) {
        return productRepository.findAll(PageRequest.of(page - 1, 5, Sort.by("id")));
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    @Transactional
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(int id) {
        if (getProductById(id) == null) {
            throw new ProductNotFoundException();
        }

        productRepository.deleteById(id);
    }

    @Transactional
    public void editProduct(Product product) {
        productRepository.save(product);
    }
}
