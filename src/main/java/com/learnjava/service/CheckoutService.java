package com.learnjava.service;


import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CartItem;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;

import java.util.List;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.toList;

public class CheckoutService {

    private PriceValidatorService priceValidatorService;

    public CheckoutService(PriceValidatorService priceValidatorService) {
        this.priceValidatorService = priceValidatorService;
    }

    public CheckoutResponse checkout(Cart cart, boolean useCollect) {

        startTimer();
        List<CartItem> priceValidationList = cart.getCartItemList()
                //.stream()
                .parallelStream()
                .map(cartItem -> {
                    boolean isPriceValid = priceValidatorService.isCartItemInvalid(cartItem);
                    cartItem.setExpired(isPriceValid);
                    return cartItem;
                })
                .filter(CartItem::isExpired)
                .collect(toList());
        timeTaken();
        stopWatchReset();

        if (priceValidationList.size() > 0) {
            log("Checkout Error");
            return new CheckoutResponse(CheckoutStatus.FAILURE, priceValidationList);
        }

        double finalRate = useCollect ? calculateFinalPrice_collect(cart) : calculateFinalPrice_reduce(cart);
        log("Checkout Complete and the final rate is " + finalRate);

        return new CheckoutResponse(CheckoutStatus.SUCCESS, finalRate);
    }

    private double calculateFinalPrice_collect(Cart cart) {
        log("Using collect() to find the cart total price");
        return cart.getCartItemList()
                .parallelStream()
                .map(cartItem -> cartItem.getQuantity() * cartItem.getRate())
                .collect(summingDouble(Double::doubleValue));
        //.mapToDouble(Double::doubleValue)
        //.sum();
    }

    private double calculateFinalPrice_reduce(Cart cart) {
        log("Using reduce() to find the cart total price");
        return cart.getCartItemList()
                .parallelStream()
                .map(cartItem -> cartItem.getQuantity() * cartItem.getRate())
                //.reduce(0.0, (x,y)->x+y);
                .reduce(0.0, Double::sum);
        //Identity for multiplication is 1
        //Identity for addition  is 0
    }
}
