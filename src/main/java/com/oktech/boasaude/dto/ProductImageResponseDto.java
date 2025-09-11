package com.oktech.boasaude.dto;

import java.util.UUID;

import com.oktech.boasaude.entity.ProductImage;

public record ProductImageResponseDto(
    UUID id,
    String imageUrl,
    UUID productId
) {
    public static ProductImageResponseDto fromEntity(ProductImage productImage) {
        String normalizedUrl = productImage.getImageUrl() != null
            ? productImage.getImageUrl().replace("\\", "/")
            : null;
        
        String fullUrl = normalizedUrl != null
            ? "http://localhost:8080/" + normalizedUrl
            : null;

        return new ProductImageResponseDto(
            productImage.getId(),
            fullUrl,
            productImage.getProduct() != null ? productImage.getProduct().getId() : null
        );
    }
}
