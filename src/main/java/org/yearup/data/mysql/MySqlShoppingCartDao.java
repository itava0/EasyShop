package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MySqlShoppingCartDao implements ShoppingCartDao {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ProductDao productDao;
    @Override
    public ShoppingCart getByUserId(int userId) throws SQLException {
        Map<Integer, ShoppingCartItem> items = new HashMap<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM shopping_cart WHERE user_id = ?")) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int productId = resultSet.getInt("product_id");
                    Product product = productDao.getById(productId);
                    ShoppingCartItem item = new ShoppingCartItem(product, 1);
                    items.put(productId, item);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setItems(items);

        return shoppingCart;
    }


    @Override
    public ShoppingCart addProductToCart(int userId, int productId) {
        return null;
    }

    @Override
    public void updateCartItemQuantity(int userId, int productId, int quantity) {

    }

    @Override
    public void removeProductFromCart(int userId, int productId) {

    }

    @Override
    public void clearCart(int userId) {

    }

    @Override
    public List<Product> getCartItemsByUserId(int userId) {
        return null;
    }
}
