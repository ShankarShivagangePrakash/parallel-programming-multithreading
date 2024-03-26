package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorldException {

    private HelloWorldService hws;

    public CompletableFutureHelloWorldException(HelloWorldService helloWorldService) {
        this.hws = helloWorldService;
    }

    public String helloWorld_3_async_calls_handle() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> this.hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> this.hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " HI CompletableFuture!";
        });

        // Let's see how to handle any exception in hello() using .handle() function.
        // Note: .handle() can catch exception and recover from it.
        String hw = hello

                // .handle() is applied for hello, how do we know,
                // hello.handle() - it takes two arguments, result of that async call and second argument is exception.
                // Note: handle() will execute even if there is no exception, so we have to make sure exception logic executes if and only if exception has occured.
                // How to know exception has occurred means, check is exception object not null, i.e. e!=null then only execute the exception logic and recover logic.
                // else return the default value.
                .handle((result, e) -> { // this gets invoked for both success and failure
                    log("result is : " + result);
                    if (e != null) {
                        log("Exception is : " + e.getMessage());
                        return "";
                    }
                    // recover from exception and return some value, not we can set any value for this result, see inside if block above we are returning empty string for result.
                    return result;

                })
                .thenCombine(world, (h, w) -> h + w) // (first,second)

                // below handle() is for second completable future. that is world completable future.
                // we can have different handle() for different completable future.
                .handle((result, e) -> { // this gets invoked for both success and failure
                    log("result is : " + result);
                    if (e != null) {
                        log("Exception Handle after world : " + e.getMessage());
                        return "";
                    }
                    return result;
                })
                .thenCombine(hiCompletableFuture, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)

                .join();

        timeTaken();

        return hw;
    }

    public String helloWorld_3_async_calls_exceptionally() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> this.hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> this.hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " HI CompletableFuture!";
        });

        String hw = hello

                // exceptionally() takes only exception object and will be triggered only if there is exception.
                // so it is much simpler than handle()
                .exceptionally((e) -> { // this gets invoked for both success and failure
                        log("Exception is : " + e.getMessage());
                    return "";
                })
                .thenCombine(world, (h, w) -> h + w) // (first,second)
                .exceptionally((e) -> { // this gets invoked for both success and failure
                        log("Exception Handle after world : " + e.getMessage());
                        return "";
                })
                .thenCombine(hiCompletableFuture, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)

                .join();

        timeTaken();

        return hw;
    }


    public String helloWorld_3_async_whenComplete() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> this.hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> this.hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " HI CompletableFuture!";
        });

        String hw = hello
                //similar to handle() but cannot recover from exception, so we can return any value.
                .whenComplete((result, e) -> { // this gets invoked for both success and failure
                    log("result is : " + result);
                    if (e != null) {
                        log("Exception is : " + e.getMessage());
                    }
                })
                .thenCombine(world, (h, w) -> h + w) // (first,second)
                .whenComplete((result, e) -> { // this gets invoked for both success and failure
                    log("result is : " + result);
                    if (e != null) {
                        log("Exception Handle after world : " + e.getMessage());
                    }
                })
                // exceptionally is required when to terminate whenComplete()
                .exceptionally((e) -> { // this gets invoked for both success and failure
                    log("Exception Handle after world : " + e.getMessage());
                    return "";
                })
                .thenCombine(hiCompletableFuture, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)

                .join();

        timeTaken();
        return hw;
    }

}
