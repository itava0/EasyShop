package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;

// convert this class to a REST controller
// only logged-in users should have access to these actions
@CrossOrigin(origins = "http://localhost:63342")
@RestController
public class ShoppingCartController{
    @Autowired
    private ShoppingCartDao shoppingCartDao;
    @Autowired
    private UserDao userDao;

    @Autowired
    private ProductDao productDao;

    // a shopping cart requires
    // each method in this controller requires a Principal object as a parameter
    @GetMapping("/cart")
    public ShoppingCart getCart(Principal principal) {
        try {
            if (principal != null) {
                System.out.println("good");

                String userName = principal.getName();

                // find database user by userId
                User user = userDao.getByUserName(userName);
                int userId = user.getId();

                System.out.println("good2");
                return shoppingCartDao.getByUserId(userId);
            } else {
                System.out.println("error before catch");
            }
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }

        return null;
    }
    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PostMapping("/products/{productId}")
    public ShoppingCart addToCart(@PathVariable int productId, Principal principal) {

        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            return shoppingCartDao.addProductToCart(userId, productId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add product to cart.", e);
        }
    }

    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated


    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart

}
