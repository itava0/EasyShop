package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.mysql.mySqlOrderDao;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("orders")
public class OrdersController {
    @Autowired
    private mySqlOrderDao orderDao;


    @Autowired
    public OrdersController(mySqlOrderDao orderDao) {
        this.orderDao = orderDao;

    }

   @PostMapping("")
    public int createOrder(@RequestBody Order order){
        orderDao.createOrder(order);

        return order.getOrderId();
    }

    @PostMapping("orderlineitem")
    public void createOrderLineItem(@RequestBody OrderLineItem orderLineItem){
        orderDao.createOrderLineItem(orderLineItem);


    }
}
