package com.petshop.repository;

import com.petshop.model.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findById(Long id);
    boolean existsById(Long id);
    List<Product> findBySectionsContaining(String section);
    List<Product> findByCategoryIgnoreCase(String category);
    List<Product> findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(String name, String brand);
}
