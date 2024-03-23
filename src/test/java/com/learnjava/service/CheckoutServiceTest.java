package com.learnjava.service;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;
import com.learnjava.util.DataSet;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {

    PriceValidatorService priceValidatorService = new PriceValidatorService();
    CheckoutService checkoutService = new CheckoutService(priceValidatorService);

    @Test
    void checkout_5_items() {

        Cart cart = DataSet.createCart(5);

        CheckoutResponse checkoutResponse = checkoutService.checkout(cart, true);

        assertEquals(CheckoutStatus.SUCCESS, checkoutResponse.getCheckoutStatus());
    }

    @Test
    void checkout_more_than_number_of_cores() {

        Cart cart = DataSet.createCart(Runtime.getRuntime().availableProcessors() + 1);
        System.out.println("Number of cores: " + Runtime.getRuntime().availableProcessors());

        CheckoutResponse checkoutResponse = checkoutService.checkout(cart, true);

        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
    }

    @Test
    void checkout_5_items_collect() {

        Cart cart = DataSet.createCart(5);

        CheckoutResponse checkoutResponse = checkoutService.checkout(cart, true);

        assertEquals(CheckoutStatus.SUCCESS, checkoutResponse.getCheckoutStatus());
    }

    @Test
    void checkout_5_items__reduce() {

        Cart cart = DataSet.createCart(5);
        System.out.println("Number of cores: " + Runtime.getRuntime().availableProcessors());

        CheckoutResponse checkoutResponse = checkoutService.checkout(cart, false);

        assertEquals(CheckoutStatus.SUCCESS, checkoutResponse.getCheckoutStatus());
    }

    @Test
    void parallelism() {
        // parallelism =  no of cores -1
        System.out.println("parallelism : " + ForkJoinPool.getCommonPoolParallelism());
    }

    @Test
    void checkout_modify_parallelism() {

        //given
        // -Djava.util.concurrent.ForkJoinPool.common.parallelism=100
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "100");

        Cart cart = DataSet.createCart(105);

        //when
        CheckoutResponse checkoutResponse = checkoutService.checkout(cart, false);

        //then
        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
    }

}