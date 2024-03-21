package com.learnjava.thread;

import com.learnjava.domain.Product;
import com.learnjava.domain.ProductInfo;
import com.learnjava.domain.Review;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceUsingThread {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    public ProductServiceUsingThread(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public Product retrieveProductDetails(String productId) throws InterruptedException {
        stopWatch.start();

        /*
            Idea here is to simultaneously execute productInfo and review service methods.
            combine output and return value.
         */

        /*
            To do that we need to have two threads one for ProductInfo and one for ProductReview
            We create threads using Runnable interface.
            We create two inner classes which implements Runnable interface, which we pass to thread
            the runnable classes we create are ProductInfoRunnable, ReviewRunnable

            ProductInfoRunnable accepts productId and returns the product info by invoking appropriate service method
            ReviewRunnable accepts productId and returns review for product by accepting service method.
         */
        ProductInfoRunnable productInfoRunnable = new ProductInfoRunnable(productId);
        ReviewRunnable reviewRunnable = new ReviewRunnable(productId);

        // creating threads using runnable objects.
        Thread productInfoThread = new Thread(productInfoRunnable);
        Thread reviewThread = new Thread(reviewRunnable);

        // Actually starting threads.
        productInfoThread.start();
        reviewThread.start();

        // waiting for threads to finish execution.
        productInfoThread.join();
        reviewThread.join();

        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());

        // observe second and third parameters,
        /*problem with Runnable interface is it has only one abstract method public void run(), it doesn't accept or return anything.
        To send a parameter, we used constructor of class which implements Runnable. declare a new variable. set it
        To get the value we have a access that variable
        That is what we are doing in second and third parameters.*/
        return new Product(productId, productInfoRunnable.productInfo, reviewRunnable.review);
    }

    public static void main(String[] args) throws InterruptedException {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingThread productService = new ProductServiceUsingThread(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

    }

    // We are implementing Runnable interface this class object we pass to thread, to create thread object using Runnable
    // This class will get productInfo.
    private class ProductInfoRunnable implements Runnable {
        private String productId;
        private ProductInfo productInfo;

        public ProductInfoRunnable(String productId) {
            this.productId = productId;
        }

        @Override
        public void run() {

            // get product info.
            productInfo = productInfoService.retrieveProductInfo(productId);
        }
    }

    // Another inner class implements Runnable interface.
    // to get review
    private class ReviewRunnable implements Runnable {
        private String productId;
        private Review review;
        public ReviewRunnable(String productId) {
            this.productId = productId;
        }

        @Override
        public void run() {
            // get review of product.
            review = reviewService.retrieveReviews(productId);
        }
    }
}
