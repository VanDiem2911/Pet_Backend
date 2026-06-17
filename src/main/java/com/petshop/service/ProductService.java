package com.petshop.service;

import com.petshop.exception.NotFoundException;
import com.petshop.model.Product;
import com.petshop.repository.ProductRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findProducts(String section, String category, String q) {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .filter(p -> {
                    if (StringUtils.hasText(section) && (p.getSections() == null || !p.getSections().contains(section))) {
                        return false;
                    }
                    if (StringUtils.hasText(category) && !category.equalsIgnoreCase(p.getCategory())) {
                        return false;
                    }
                    if (StringUtils.hasText(q)) {
                        String queryLower = q.toLowerCase();
                        boolean matchesName = p.getName() != null && p.getName().toLowerCase().contains(queryLower);
                        boolean matchesBrand = p.getBrand() != null && p.getBrand().toLowerCase().contains(queryLower);
                        if (!matchesName && !matchesBrand) {
                            return false;
                        }
                    }
                    return true;
                })
                .sorted(Comparator.comparing(Product::getId))
                .toList();
    }

    public Product getByProductId(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product " + id + " not found"));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product update(Long id, Product input) {
        Product existing = getByProductId(id);
        input.setMongoId(existing.getMongoId());
        input.setId(id);
        return productRepository.save(input);
    }

    public void delete(Long id) {
        Product product = getByProductId(id);
        productRepository.delete(product);
    }
}
