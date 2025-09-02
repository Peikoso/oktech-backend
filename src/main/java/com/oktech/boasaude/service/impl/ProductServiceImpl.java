package com.oktech.boasaude.service.impl;


import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.oktech.boasaude.dto.CreateProductDto;
import com.oktech.boasaude.dto.ProductResponseDto;
import com.oktech.boasaude.entity.Product;
import com.oktech.boasaude.entity.Shop;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.repository.ProductRepository;
import com.oktech.boasaude.service.ProductService;

/**
 * ProductServiceImpl é a implementação do serviço de produtos.
 * Ele implementa a interface ProductService e fornece métodos para gerenciar produtos.
 * @author João Martins
 * @version 1.0
 */

@Service
public class ProductServiceImpl implements ProductService {
    
    /**
     * Repositório de produtos para operações CRUD.
     */
    private final ProductRepository productRepository;
    
    private final ShopServiceImpl shopServiceImpl;
    
    /**
     * Injetando o repositório de produtos.
     * @param productRepository Repositório de produtos para operações CRUD.
     * @param shopRepository Repositório de lojas para operações CRUD.
     */
    public ProductServiceImpl(ProductRepository productRepository, ShopServiceImpl shopServiceImpl) {
        this.productRepository = productRepository;
        this.shopServiceImpl = shopServiceImpl;
    }

    /**
     * Cria um novo produto com os dados fornecidos e associa-o a uma loja.
     * @param createProductDto DTO com os dados do produto a ser criado.
     * @param shop Loja à qual o produto será associado.
     * @param currentUser Usuário que está criando o produto.
     * @return O produto criado.
     */
    @Override
    public ProductResponseDto createProduct(CreateProductDto createProductDto, UUID shopId, User currentUser) {
        if(createProductDto.price() <= 0){
            throw new IllegalArgumentException("Must be positive price.");
        }

        if(createProductDto.stock() <= 0){
            throw new IllegalArgumentException("Must be positive stock.");
        }

        Shop shop = shopServiceImpl.getShopById(shopId);


        if(!shop.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to create products for this shop.");
        }

        Product product = new Product(createProductDto, shop);
        
        product = productRepository.save(product);

        return new ProductResponseDto(product);
    }

    /**
     * Obtém um produto pelo seu ID.
     * @param id ID do produto a ser obtido.
     * @return O produto encontrado.
     */
    @Override
    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }
    /**
     * Obtém um produto pelo seu ID e retorna um DTO de resposta.
     * @param id ID do produto a ser obtido.
     * @return DTO de resposta do produto encontrado.
     */

    @Override
    public ProductResponseDto getProductByIdResponse(UUID id) {
        Product product = getProductById(id);
        return new ProductResponseDto(product);
    }

    /**
     * Obtém todos os produtos com paginação podendo ser filtrado por category.
     * @param pageable Objeto Pageable para paginação.
     * @return Página de produtos.
     */
    @Override
    public Page<ProductResponseDto> getAllProducts(Pageable pageable, String category) {
        Page<Product> productsPage;
        if (category != null && !category.isEmpty()) {
            productsPage = productRepository.findByCategory(category, pageable);
        } else {
            productsPage = productRepository.findAll(pageable);
        }

        return productsPage.map(ProductResponseDto::new);
    }

    /**
     * Atualiza um produto existente com os novos dados fornecidos.
     * @param id ID do produto a ser atualizado.
     * @param product Objeto Product com os novos dados.
     * @param currentUser Usuário que está tentando atualizar o produto.
     * @return O produto atualizado.
     */
    @Override
    public ProductResponseDto updateProduct(UUID id, CreateProductDto CreateProductDto, User currentUser) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        if(!product.getShop().getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to update this product.");
        }

        if(CreateProductDto.price() <= 0){
            throw new IllegalArgumentException("Must be positive price.");
        }

        if(CreateProductDto.stock() <= 0){
            throw new IllegalArgumentException("Must be positive stock.");
        }

        product.setName(CreateProductDto.name());
        product.setDescription(CreateProductDto.description());
        product.setPrice(CreateProductDto.price());
        product.setStock(CreateProductDto.stock());
        product.setCategory(CreateProductDto.category());

        product = productRepository.save(product);

        return new ProductResponseDto(product);
    }

    /**
     * Exclui um produto pelo seu ID.
     * @param id ID do produto a ser excluído.
     * @param currentUser Usuário que está tentando excluir o produto.
     */
    @Override
    public void deleteProduct(UUID id, User currentUser) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        if(!product.getShop().getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this product.");
        }

        productRepository.delete(product);
    }

    /**
     * Obtém todos os produtos de uma loja específica com paginação.
     * @param shopId ID da loja cujos produtos serão obtidos.
     * @param pageable Objeto Pageable para paginação.
     * @return Página de produtos da loja especificada.
     */
    @Override
    public Page<ProductResponseDto> getProductsByShopId(UUID shopId, Pageable pageable) {
        Page<Product> productsPage=  productRepository.findByShopId(shopId, pageable);

        return productsPage.map(ProductResponseDto::new);
    }

}
