package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


// In this test class we need to test exception handling methods of Completable future.
// For that we need to mock, if we are mocking, in JUnit it is mandatory to extend the class with MockitoExtension
@ExtendWith(MockitoExtension.class)
class CompletableFutureHelloWorldExceptionTest {


    // we need to mock helloWorldService class. Defining that. Why we need to mock helloService means, our target is CompletableFutureHelloWorldException.
    @Mock
    private HelloWorldService helloWorldService = mock(HelloWorldService.class);

    // class being tested, to the objects of this all mock objects will be added.
    @InjectMocks
    CompletableFutureHelloWorldException completableFutureHelloWorldException;

    @Test
    void helloWorld_3_async_calls_handle() {

        // when() is a mockito method. we will specify when condition A happens, what should be the result by mocking means,
        // we don't actually execute that code.
        // In below example, when helloWorldService.hello() is invoked then throw new RuntimeException.
        // when helloWorldService.world() is invoked execute the actual real method.
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception occurred"));
        when(helloWorldService.world()).thenCallRealMethod();

        // invoking the actual method, above mocks will be executed if the specified criteria are met.
        String result = completableFutureHelloWorldException.helloWorld_3_async_calls_handle();

        // Note: we are returning, "" string from hello() if exception occurs, we are creating exception using mock
        // so that will return "" and other two completable futures will execute. That is the output represented below.
        assertEquals( " WORLD! HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void helloWorld_3_async_calls_handle_2() {

        // when() is a mockito method. we will specify when condition A happens, what should be the result by mocking means,
        // we don't actually execute that code.
        // In below example, when helloWorldService.hello() is invoked then throw new RuntimeException.
        // when helloWorldService.world() is invoked then throw new RuntimeException.
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception occurred"));
        when(helloWorldService.world()).thenThrow(new RuntimeException("Exception occurred"));

        // invoking the actual method, above mocks will be executed if the specified criteria are met.
        String result = completableFutureHelloWorldException.helloWorld_3_async_calls_handle();

        // Note: we are returning, "" string from hello() if exception occurs, we are creating exception using mock
        // so that will return "" and world also throws exception because of mock.
        // only last completable future will execute. That is the output represented below.
        assertEquals( " HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void helloWorld_3_async_calls_handle_3() {

        // when() is a mockito method. we will specify when condition A happens, what should be the result by mocking means,
        // we don't actually execute that code.
        // In below example, when helloWorldService.hello() is invoked execute the actual real method.
        // when helloWorldService.world() is invoked execute the actual real method.
        when(helloWorldService.hello()).thenCallRealMethod();
        when(helloWorldService.world()).thenCallRealMethod();

        // invoking the actual method, above mocks will be executed if the specified criteria are met.
        String result = completableFutureHelloWorldException.helloWorld_3_async_calls_handle();

        // Note: All completable Futures will execute.
        assertEquals( "HELLO WORLD! HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void helloWorld_3_async_calls_exceptionally() {

        when(helloWorldService.hello()).thenCallRealMethod();
        when(helloWorldService.world()).thenCallRealMethod();

        // invoking the actual method, above mocks will be executed if the specified criteria are met.
        String result = completableFutureHelloWorldException.helloWorld_3_async_calls_exceptionally();

        assertEquals( "HELLO WORLD! HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void helloWorld_3_async_calls_exceptionally_2() {

        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception occurred"));
        when(helloWorldService.world()).thenThrow(new RuntimeException("Exception occurred"));

        // invoking the actual method, above mocks will be executed if the specified criteria are met.
        String result = completableFutureHelloWorldException.helloWorld_3_async_calls_exceptionally();

        // Note: we are returning, "" string from hello() if exception occurs, we are creating exception using mock
        // so that will return "" and world also throws exception because of mock.
        // only last completable future will execute. That is the output represented below.
        assertEquals( " HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void helloWorld_3_async_calls_exceptionally_3() {

        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception occurred"));
        when(helloWorldService.world()).thenCallRealMethod();

        // invoking the actual method, above mocks will be executed if the specified criteria are met.
        String result = completableFutureHelloWorldException.helloWorld_3_async_calls_exceptionally();

        // Note: we are returning, "" string from hello() if exception occurs, we are creating exception using mock
        // so that will return "" and other two completable futures will execute. That is the output represented below.
        assertEquals( " WORLD! HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void helloWorld_3_async_whenComplete() {

        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception occurred"));
        when(helloWorldService.world()).thenCallRealMethod();

        // invoking the actual method, above mocks will be executed if the specified criteria are met.
        String result = completableFutureHelloWorldException.helloWorld_3_async_whenComplete();

        // Note: we are returning, "" string from hello() if exception occurs, we are creating exception using mock
        // so that will return "" and other two completable futures will execute. That is the output represented below.
        assertEquals( " HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void helloWorld_3_async_whenComplete_2() {

        when(helloWorldService.hello()).thenCallRealMethod();
        when(helloWorldService.world()).thenCallRealMethod();

        // invoking the actual method, above mocks will be executed if the specified criteria are met.
        String result = completableFutureHelloWorldException.helloWorld_3_async_whenComplete();

        assertEquals( "HELLO WORLD! HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void helloWorld_3_async_whenComplete_3() {

        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception occurred"));
        when(helloWorldService.world()).thenThrow(new RuntimeException("Exception occurred"));

        // invoking the actual method, above mocks will be executed if the specified criteria are met.
        String result = completableFutureHelloWorldException.helloWorld_3_async_whenComplete();

        // Note: we are returning, "" string from hello() if exception occurs, we are creating exception using mock
        // so that will return "" and world also throws exception because of mock.
        // only last completable future will execute. That is the output represented below.
        assertEquals( " HI COMPLETABLEFUTURE!", result);
    }



}