package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;
import static java.util.stream.Collectors.joining;

public class CompletableFutureHelloWorld {

    private HelloWorldService hws;

    public CompletableFutureHelloWorld(HelloWorldService helloWorldService) {
        this.hws = helloWorldService;
    }


    // return type will be completable future will template type same as the return type of the supplyAsync input lambda function
    public CompletableFuture<String> helloWorld() {

        return CompletableFuture.supplyAsync(() -> hws.helloWorld())//  runs this in a common fork-join pool
                .thenApply(String::toUpperCase);
    }

    public CompletableFuture<String> helloWorld_withSize() {

        return CompletableFuture.supplyAsync(() -> hws.helloWorld())//  runs this in a common fork-join pool
                .thenApply(String::toUpperCase)
                .thenApply((s) -> s.length() + " - " + s);
    }

    public String helloWorld_multiple_async_calls() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> this.hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> this.hws.world());

        // then combine() is non-static method, so you have to call like firstCompletableFuture.thenCombine(secondCompletableFuture, BiFunction)
        // BiFunction will take two completableFuture combine and return one completable future.
        String hw = hello
                .thenCombine(world, (h, w) -> h + w) // (first,second)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();

        return hw;
    }


    public String helloWorld_3_async_calls() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> this.hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> this.hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " HI CompletableFuture!";
        });

        String hw = hello
                .thenCombine(world, (h, w) -> h + w) // (first,second)
                .thenCombine(hiCompletableFuture, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();

        return hw;
    }

    public String helloWorld_3_async_calls_log() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> this.hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> this.hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " HI CompletableFuture!";
        });

        String hw = hello
                // .thenCombine(world, (h, w) -> h + w) // (first,second)
                .thenCombine(world, (h, w) -> {
                    log("thenCombine h/w ");
                    return h + w;
                }) // (first,second)
                //.thenCombine(hiCompletableFuture, (previous, current) -> previous + current)
                .thenCombine(hiCompletableFuture, (previous, current) -> {
                    log("thenCombine , previous/current");
                    return previous + current;
                })
                //.thenApply(String::toUpperCase)
                .thenApply(s -> {
                    log("thenApply");
                    return s.toUpperCase();
                })
                .join();

        timeTaken();

        return hw;
    }

    public String helloWorld_3_async_calls_log_async() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> this.hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> this.hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " HI CompletableFuture!";
        });

        String hw = hello
                // .thenCombine(world, (h, w) -> h + w) // (first,second)
                .thenCombineAsync(world, (h, w) -> {
                    log("thenCombine h/w ");
                    return h + w;
                }) // (first,second)
                //.thenCombine(hiCompletableFuture, (previous, current) -> previous + current)
                .thenCombineAsync(hiCompletableFuture, (previous, current) -> {
                    this.hws.hello();
                    log("thenCombine , previous/current");
                    return previous + current;
                })
                //.thenApply(String::toUpperCase)
                .thenApplyAsync(s -> {
                    this.hws.hello();
                    log("thenApply");
                    return s.toUpperCase();
                })
                .join();

        timeTaken();

        return hw;
    }


    public String helloWorld_3_async_calls_custom_threadPool() {

        // creating the thread pool using executor service of java.
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        startTimer();
        // observe second parameter of all three methods, we are specifying the thread pool by passing executor service.
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> this.hws.hello(), executorService);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> this.hws.world(), executorService);

        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " HI CompletableFuture!";
        }, executorService);

        String hw = hello
                // .thenCombine(world, (h, w) -> h + w) // (first,second)
                .thenCombine(world, (h, w) -> {
                    log("thenCombine h/w ");
                    return h + w;
                }) // (first,second)
                //.thenCombine(hiCompletableFuture, (previous, current) -> previous + current)
                .thenCombine(hiCompletableFuture, (previous, current) -> {
                    log("thenCombine , previous/current");
                    return previous + current;
                })
                //.thenApply(String::toUpperCase)
                .thenApply(s -> {
                    log("thenApply");
                    return s.toUpperCase();
                })
                .join();

        timeTaken();

        return hw;
    }

    public String helloWorld_3_async_calls_custom_threadpool_async() {

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> this.hws.hello(), executorService);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> this.hws.world(), executorService);

        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            // delay(1000);
            return " HI CompletableFuture!";
        }, executorService);

        String hw = hello
                // .thenCombine(world, (h, w) -> h + w) // (first,second)
                .thenCombineAsync(world, (h, w) -> {
                    log("thenCombine h/w ");
                    return h + w;
                }, executorService) // (first,second)

                /*  .thenCombineAsync(world, (h, w) -> {
                      log("thenCombine h/w ");
                      return h + w;
                  }) // with no executor service as an input*/
                //.thenCombine(hiCompletableFuture, (previous, current) -> previous + current)
                .thenCombineAsync(hiCompletableFuture, (previous, current) -> {
                    log("thenCombine , previous/current");
                    return previous + current;
                }, executorService)
                //.thenApply(String::toUpperCase)
                .thenApply(s -> {
                    log("thenApply");
                    return s.toUpperCase();
                })
                .join();

        timeTaken();

        return hw;
    }


    public String helloWorld_4_async_calls() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> this.hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> this.hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " HI CompletableFuture!";
        });
        CompletableFuture<String> byeCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Bye!";
        });


        String hw = hello
                .thenCombine(world, (h, w) -> h + w) // (first,second)
                .thenCombine(hiCompletableFuture, (previous, current) -> previous + current)
                .thenCombine(byeCompletableFuture, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();

        return hw;
    }

    public CompletableFuture<String> helloWorld_thenCompose() {

        CompletableFuture<String> helloWorldFuture = CompletableFuture.supplyAsync(() -> this.hws.hello())
                .thenCompose(previous -> hws.worldFuture(previous))
                .thenApply(String::toUpperCase);

        return helloWorldFuture;

    }

    public String allOf() {
        startTimer();

        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return "Hello";
        });

        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
            delay(2000);
            return " World";
        });

        List<CompletableFuture<String>> cfList = List.of(cf1, cf2);
        CompletableFuture<Void> cfAllOf = CompletableFuture.allOf(cfList.toArray(new CompletableFuture[cfList.size()]));
        String result = cfAllOf.thenApply(v -> cfList.stream()
                .map(CompletableFuture::join)
                .collect(joining())).join();

        timeTaken();

        return result;

    }

    public String anyOf() {
        startTimer();

        CompletableFuture<String> db = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            log("response from db");
            return "Hello World";
        });

        CompletableFuture<String> restApi = CompletableFuture.supplyAsync(() -> {
            delay(2000);
            log("response from restApi");
            return "Hello World";
        });

        CompletableFuture<String> soapApi = CompletableFuture.supplyAsync(() -> {
            delay(3000);
            log("response from soapApi");
            return "Hello World";
        });

        List<CompletableFuture<String>> cfList = List.of(db, restApi, soapApi);
        CompletableFuture<Object> cfAllOf = CompletableFuture.anyOf(cfList.toArray(new CompletableFuture[cfList.size()]));
        String result =  (String) cfAllOf.thenApply(v -> {
            if (v instanceof String) {
                return v;
            }
            return null;
        }).join();

        timeTaken();
        return result;
    }


    public String helloWorld_1() {

        return CompletableFuture.supplyAsync(() -> hws.helloWorld())//  runs this in a common fork-join pool
                .thenApply(String::toUpperCase)
                .join();

    }

    public CompletableFuture<String> complete(String input) {

        CompletableFuture<String> completableFuture = new CompletableFuture();
        completableFuture = completableFuture
                .thenApply(String::toUpperCase)
                .thenApply((result) -> result.length() + " - " + result);

        completableFuture.complete(input);

        return completableFuture;

    }

    public static void main(String[] args) {

        // In this example we need to invoke HelloWorldService/helloWorld() in non-blocking fashion for that we are using Completable future.
        HelloWorldService helloWorldService = new HelloWorldService();

        // supplyAsync factory method - initiate asynchronous computation.
        // input to this method is Supplier functional interface which has only one method which doesn't accept any parameter but returns Template type object.
        // that is what we are doing in next line.
        // Note completable future will not wait for helloWorld() method to complete execution, this call will be added to queue and control will move to next line.
        CompletableFuture.supplyAsync(() -> helloWorldService.helloWorld()) //  runs this in a common fork-join pool

                // thenApply() is used to chain the async computation. after supplyAsync() finishes its execution.
                // if we need to utilize that completable future object and perform something we can use thenApply()
                // thenApply() if we need to access completable future, thenAccept() if we need to access the result variable of completable future.
                // Note: this lambda expression can be converted to method reference, it's your choice.
                .thenApply((result)-> result.toUpperCase())
                //.thenApply(String::toUpperCase)

                // thenApply() is a completion stage method.
                // supplyAsync will return the completable future object will have a result variable that will same as the return type of the method invoke from supplyAsync()
                // in this case it will be of String type since helloWorld() returns String.
                // To access that we need to use Consumer functional interface, it has one method which accepts a single argument.
                .thenAccept((result) -> {
                    log("result: " + result);
                })

                // join() is required because, supplyAsync() request will be added to shared queue and control will go to log("Done!") after executing this line program terminates.
                // with join() we are blocking the execution of next line in program until this completable future finish execution.
                .join();

        log("Done!");
        delay(2000);
    }
}
