package com.petshop.repository;

import com.petshop.model.NewsletterSubscription;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NewsletterRepository extends MongoRepository<NewsletterSubscription, String> {
    Optional<NewsletterSubscription> findByEmailIgnoreCase(String email);
}
