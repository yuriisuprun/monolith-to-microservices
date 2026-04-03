package com.suprun.demo.modules.orders.api;

import java.util.List;

public interface OrderService {

    OrderResponse create(CreateOrderRequest request);

    List<OrderResponse> list();
}

