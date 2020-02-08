package io.shubansoft.ims.controller;

import io.shubansoft.ims.model.Orders;
import io.shubansoft.ims.model.OrdersList;
import io.shubansoft.ims.service.StockOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ProductOrderController {

    private final StockOrderService stockOrderService;

    @GetMapping(path = "/processOrders")
    public OrdersList processOrders(
            @RequestParam(required = false)final Integer productId,
            @RequestParam(required = false)final Boolean test){
        return stockOrderService.processOrders(
                Optional.ofNullable(productId),
                Optional.ofNullable(test).orElse(true));
    }
}
