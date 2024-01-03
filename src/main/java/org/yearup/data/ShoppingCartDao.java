package org.yearup.data;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.sql.SQLException;
import java.util.List;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId) throws SQLException;


    ShoppingCart addProductToCart(int userId, int productId);




    void clearCart(int userId);


    List<Product> getCartItemsByUserId(int userId);

    ShoppingCart updateCartItem(int userId, int productId, ShoppingCartItem shoppingCartItem);
}
