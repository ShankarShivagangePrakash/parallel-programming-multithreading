package com.learnjava.completablefuture;

import com.learnjava.domain.Product;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ProductService;
import com.learnjava.service.ReviewService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceUsingCompletableFutureTest {

    ProductInfoService productInfoService = new ProductInfoService();
    ReviewService reviewService = new ReviewService();
    InventoryService inventoryService = new InventoryService();

    ProductServiceUsingCompletableFuture productServiceUsingCompletableFuture = new ProductServiceUsingCompletableFuture(productInfoService, reviewService);
    ProductServiceUsingCompletableFuture productServiceUsingCompletableFuture2 = new ProductServiceUsingCompletableFuture(productInfoService, reviewService, inventoryService);

    @Test
    void retrieveProductDetails() {

       Product product = productServiceUsingCompletableFuture.retrieveProductDetails("ABC123");

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetails_CF() {

        CompletableFuture<Product> productCompletableFuture = productServiceUsingCompletableFuture.retrieveProductDetails_CF("ABC123");

        productCompletableFuture.thenAccept((product) -> {
            assertNotNull(product);
            assertTrue(product.getProductInfo().getProductOptions().size() > 0);
            assertNotNull(product.getReview());
        });
    }

    @Test
    void retrieveProductDetailsWithInventory() {

        Product product = productServiceUsingCompletableFuture2.retrieveProductDetailsWithInventory("ABC123");

        // make sure we have set inventory for all product options.
        product.getProductInfo().getProductOptions()
                        .forEach((productOption) -> assertNotNull(productOption.getInventory()));

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetailsWithInventory_approach2() {

        Product product = productServiceUsingCompletableFuture2.retrieveProductDetailsWithInventory_approach2("ABC123");

        // make sure we have set inventory for all product options.
        product.getProductInfo().getProductOptions()
                .forEach((productOption) -> assertNotNull(productOption.getInventory()));

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        assertNotNull(product.getReview());
    }
}