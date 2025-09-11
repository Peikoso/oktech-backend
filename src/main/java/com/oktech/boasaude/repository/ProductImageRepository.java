package com.oktech.boasaude.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oktech.boasaude.entity.Product;
import com.oktech.boasaude.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {
    
    List<ProductImage> findByProduct(Product product);
    
    List<ProductImage> findByProductId(UUID productId);

    @Query("SELECT pi FROM ProductImage pi " +
        "WHERE pi.id = :imageId AND pi.product.shop.owner.id = :userId")
    Optional<ProductImage> findByIdAndOwner(@Param("imageId") UUID imageId,
                                            @Param("userId") UUID userId);

   

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
           "FROM Product p " +
           "WHERE p.id = :productId AND p.shop.owner.id = :userId")
    boolean existsByIdAndOwner(@Param("productId") UUID productId,
                               @Param("userId") UUID userId);
                               
}



