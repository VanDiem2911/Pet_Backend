package com.petshop.config;

import com.petshop.model.Product;
import com.petshop.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    private final ProductRepository productRepository;
    private final boolean seedEnabled;

    public DataSeeder(ProductRepository productRepository, @Value("${app.seed.enabled}") boolean seedEnabled) {
        this.productRepository = productRepository;
        this.seedEnabled = seedEnabled;
    }

    @Override
    public void run(String... args) {
        if (!seedEnabled || productRepository.count() > 0) {
            return;
        }

        productRepository.saveAll(List.of(
                product(1, "Royal Canin Mini Adult 2kg", "Royal Canin", 285000, 380000, 25, false, 5, 128, "🍖", "Chó", "flashSale"),
                product(2, "Whiskas Pate Tuna 85g (12 gói)", "Whiskas", 95000, 120000, 21, false, 5, 94, "🐟", "Mèo", "flashSale"),
                product(3, "Pedigree Xương thưởng Dentastix 7 cái", "Pedigree", 45000, 55000, 18, false, 4, 63, "🦴", "Chó", "flashSale"),
                product(4, "Me-O Adult Ocean Fish 1.2kg", "Me-O", 72000, 90000, 20, false, 4, 47, "🐠", "Mèo", "flashSale"),
                product(5, "Hill's Science Diet Kitten 1.6kg", "Hill's", 345000, 420000, 18, false, 5, 82, "🌾", "Mèo", "flashSale"),
                product(10, "Balo vận chuyển thú cưng có cửa sổ", "PetCarry", 380000, null, 0, true, 5, 55, "🎒", "Phụ kiện", "featured", "bestSeller"),
                product(11, "Dầu gội Bio-Groom cho chó lông dài", "Bio-Groom", 155000, 180000, 14, false, 4, 38, "🛁", "Chó", "featured", "bestSeller"),
                product(12, "Chuồng sắt cao cấp 2 tầng 60x45x80cm", "PetHome", 850000, null, 0, true, 5, 29, "🏠", "Phụ kiện", "featured", "bestSeller"),
                product(13, "Đồ chơi cá ngựa squeaky cho chó", "Kong", 85000, 110000, 23, false, 5, 71, "🐴", "Chó", "featured"),
                product(14, "Vòng cổ da cho mèo có chuông - Đỏ", "PetFashion", 55000, null, 0, true, 4, 44, "🎀", "Mèo", "featured"),
                product(15, "Máy cho ăn tự động thông minh 5L", "SmartFeed", 650000, 780000, 17, false, 5, 92, "🤖", "Phụ kiện", "featured"),
                product(16, "Royal Canin Poodle Adult 1.5kg", "Royal Canin", 265000, 310000, 15, false, 5, 106, "🍗", "Chó", "featured"),
                product(17, "Purina Pro Plan Kitten Chicken 1.5kg", "Purina", 295000, 350000, 16, false, 4, 59, "🐣", "Mèo", "featured"),
                product(18, "Cần câu đồ chơi mèo lông vũ", "Petstages", 35000, null, 0, true, 5, 133, "🪶", "Mèo", "featured"),
                product(19, "Đế cào móng mèo sisal 50cm", "PetHome", 125000, 150000, 17, false, 4, 48, "🌿", "Mèo", "featured"),
                product(20, "Purina One Adult Dog Chicken 1.5kg", "Purina", 195000, 230000, 15, false, 5, 211, "🥩", "Chó", "bestSeller"),
                product(21, "Whiskas Dry Adult Ocean Fish 1.2kg", "Whiskas", 75000, 95000, 21, false, 5, 185, "🐟", "Mèo", "bestSeller"),
                product(22, "Đồ chơi kéo co bằng bông cho chó", "Kong", 65000, null, 0, true, 4, 97, "🪢", "Chó", "bestSeller"),
                product(23, "Vắc-xin phòng dại cho mèo - tại nhà", "VetCare", 120000, null, 0, true, 5, 55, "💉", "Mèo", "bestSeller"),
                product(24, "Bát ăn sứ chống trượt size M", "PetHome", 45000, null, 0, false, 4, 142, "🍽️", "Phụ kiện", "bestSeller")
        ));
    }

    private Product product(
            long id,
            String name,
            String brand,
            int price,
            Integer oldPrice,
            int discount,
            boolean hot,
            int rating,
            int reviews,
            String emoji,
            String category,
            String... sections
    ) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setBrand(brand);
        product.setPrice(BigDecimal.valueOf(price));
        product.setOldPrice(oldPrice == null ? null : BigDecimal.valueOf(oldPrice));
        product.setDiscount(discount);
        product.setHot(hot);
        product.setRating(rating);
        product.setReviews(reviews);
        product.setEmoji(emoji);
        product.setImageUrl(null);
        product.setCategory(category);
        product.setSections(Set.of(sections));
        product.setStock(100);
        return product;
    }
}
