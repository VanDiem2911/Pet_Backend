package com.petshop.repository;

import com.petshop.model.Cart;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findBySessionId(String sessionId);
}
