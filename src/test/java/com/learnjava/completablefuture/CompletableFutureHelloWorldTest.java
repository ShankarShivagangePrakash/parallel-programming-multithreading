package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class CompletableFutureHelloWorldTest {

    HelloWorldService helloWorldService = new HelloWorldService();
    CompletableFutureHelloWorld completableFutureHelloWorld = new CompletableFutureHelloWorld(helloWorldService);

    @Test
    void helloWorld() {

        // invoke the method you want to test.
        CompletableFuture<String> completableFuture = completableFutureHelloWorld.helloWorld();

        // Now we have the result completable future object,
        // now we need to access the result variable and check the value is it matching or not?
        completableFuture.thenAccept((result) -> {
            assertEquals("HELLO WORLD", result.toUpperCase());
        })
                // Note this join is very much important without this assert statement will not execute.
                // system will not wait for assert statement to execute and your test will get passes all time.
                .join();

    }

    @Test
    void helloWorld_multiple_async_calls() {

        // invoke the method you want to test.
        String result = completableFutureHelloWorld.helloWorld_multiple_async_calls();

        assertEquals("HELLO WORLD!", result);


    }

    @Test
    void helloWorld_3_async_calls() {

        // invoke the method you want to test.
        String result = completableFutureHelloWorld.helloWorld_3_async_calls();

        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", result);


    }

    @Test
    void helloWorld_3_async_calls_log() {

        // invoke the method you want to test.
        String result = completableFutureHelloWorld.helloWorld_3_async_calls_log();

        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", result);


    }

    @Test
    void helloWorld_3_async_calls_log_async() {

        // invoke the method you want to test.
        String result = completableFutureHelloWorld.helloWorld_3_async_calls_log_async();

        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", result);


    }

    @Test
    void helloWorld_3_async_calls_custom_threadPool() {

        // invoke the method you want to test.
        String result = completableFutureHelloWorld.helloWorld_3_async_calls_custom_threadPool();

        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", result);


    }

    @Test
    void helloWorld_3_async_calls_custom_threadpool_async() {

        // invoke the method you want to test.
        String result = completableFutureHelloWorld.helloWorld_3_async_calls_custom_threadpool_async();

        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", result);


    }

    @Test
    void allOf() {

        // invoke the method you want to test.
        String result = completableFutureHelloWorld.allOf();

        assertEquals("Hello World", result);

    }

    @Test
    void anyOf() {

        // invoke the method you want to test.
        String result = completableFutureHelloWorld.anyOf();

        assertEquals("Hello World", result);

    }

    @Test
    void helloWorld_thenCompose() {

        // invoke the method you want to test.
        CompletableFuture<String> completableFuture = completableFutureHelloWorld.helloWorld_thenCompose();

        completableFuture.thenAccept((result) -> {
                    assertEquals("HELLO WORLD!", result.toUpperCase());
                })
                .join();

    }
}