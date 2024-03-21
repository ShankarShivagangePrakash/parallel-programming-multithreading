package com.learnjava.executor;

import com.learnjava.domain.Product;
import com.learnjava.domain.ProductInfo;
import com.learnjava.domain.Review;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;

import java.util.concurrent.*;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceUsingExecutor {

    // get the instance of executor service. With thread pool size 6
    static ExecutorService executorService = Executors.newFixedThreadPool(6);

    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    public ProductServiceUsingExecutor(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public Product retrieveProductDetails(String productId) throws ExecutionException, InterruptedException, TimeoutException {
        stopWatch.start();

        // Now we have to add the tasks to working queue from which threads will pick this task.
        // executorService.submit() will do this task.
        // submit() can accept Runnable, callable, in this case we are passing Callable
        // once the task is executed by the task, result will be pushed Completion queue, future will fetch this result for that reason we are assigning result to Future.
        Future<ProductInfo> productInfoFuture = executorService.submit(() -> productInfoService.retrieveProductInfo(productId));
        Future<Review> reviewFuture = executorService.submit(() -> reviewService.retrieveReviews(productId));

        // we should get the result from future, once future has value. we are doing that here.
        ProductInfo productInfo = productInfoFuture.get();

        // Below line is optional, we can specify timeout, means within how much time are we expecting the result.
        // If we don't get result within that it will throw exception.
        // ProductInfo productInfo = productInfoFuture.get(2, TimeUnit.SECONDS);

        Review review = reviewFuture.get();
        //Review review = reviewFuture.get(2, TimeUnit.SECONDS);

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());
        return new Product(productId, productInfo, review);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingExecutor productService = new ProductServiceUsingExecutor(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

        // We have to execute the executor service explicitly.
        executorService.shutdown();
    }
}
