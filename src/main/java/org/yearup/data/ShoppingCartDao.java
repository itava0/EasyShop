package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;

import java.util.List;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here

    ShoppingCart addProductToCart(int userId, int productId);


    void updateCartItemQuantity(int userId, int productId, int quantity);


    void removeProductFromCart(int userId, int productId);


    void clearCart(int userId);


    List<Product> getCartItemsByUserId(int userId);
}
