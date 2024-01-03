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
                    int quantity = resultSet.getInt("quantity");
                    Product product = productDao.getById(productId);
                    ShoppingCartItem item = new ShoppingCartItem(product, quantity);
                    items.put(productId, item);
                }
            }
        } catch (SQLException e) {

            e.printStackTrace();
            throw e;
        }

        return new ShoppingCart(items);
    }



    @Override
    public ShoppingCart addProductToCart(int userId, int productId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, 1) ON DUPLICATE KEY UPDATE quantity = quantity + 1")) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, productId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        try {
            return getByUserId(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void clearCart(int userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM shopping_cart WHERE user_id = ?")) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clear cart for user: " + userId, e);
        }
    }


    @Override
    public List<Product> getCartItemsByUserId(int userId) {
        return null;
    }



    @Override
    public ShoppingCart updateCartItem(int userId, int productId, ShoppingCartItem shoppingCartItem) {
        try {
            ShoppingCart shoppingCart = getByUserId(userId);

            if (shoppingCart == null) {
                throw new RuntimeException("Shopping cart not found for user: " + userId);
            }

            ShoppingCartItem matchedItem = shoppingCart.getItems().get(productId);

            if (matchedItem != null) {
                matchedItem.setQuantity(shoppingCartItem.getQuantity());
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(
                             "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?")) {
                    preparedStatement.setInt(1, matchedItem.getQuantity());
                    preparedStatement.setInt(2, userId);
                    preparedStatement.setInt(3, productId);
                    preparedStatement.executeUpdate();
                }

            } else {
                throw new RuntimeException("Product not found in the shopping cart: " + productId);
            }

            return shoppingCart;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
