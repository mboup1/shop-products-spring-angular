package com.dame.backend_shop_products.service;

import com.dame.backend_shop_products.Exception.MissingProductFieldException;
import com.dame.backend_shop_products.Exception.ProductEntityNotNullException;
import com.dame.backend_shop_products.Exception.ProductNotFoundException;
import com.dame.backend_shop_products.entity.Product;
import com.dame.backend_shop_products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("No product found with this ID: " + id));
    }

    @Override
    public Product saveProduct(Product product) {
        validateProduct(product);
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("No product found with this ID: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("No product found with this ID: " + id));

        existingProduct.setCode(productDetails.getCode());
        existingProduct.setName(productDetails.getName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setImage(productDetails.getImage());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setCategory(productDetails.getCategory());
        existingProduct.setQuantity(productDetails.getQuantity());
        existingProduct.setInventoryStatus(productDetails.getInventoryStatus());
        existingProduct.setRating(productDetails.getRating());

        return productRepository.save(existingProduct);
    }

    private void validateProduct(Product product) {
        if (Objects.isNull(product)) {
            throw new ProductEntityNotNullException("Product data is null");
        }

        validateField(product.getCode(), "Product code");
        validateField(product.getName(), "Product name");
        validateField(product.getCategory(), "Product category");
        validateField(product.getPrice(), "Product price");
        validateField(product.getInventoryStatus(), "Product inventory status");
        validateField(product.getRating(), "Product rating");
    }

    private void validateField(Object field, String fieldName) {
        if (Objects.isNull(field)) {
            throw new MissingProductFieldException(fieldName + " is required");
        } else if (field instanceof String && StringUtils.isEmpty((String) field)) {
            throw new MissingProductFieldException(fieldName + " cannot be empty");
        }
    }
}
