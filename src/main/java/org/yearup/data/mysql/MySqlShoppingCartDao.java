package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlShoppingCartDao implements ShoppingCartDao {
    @Autowired
    private DataSource dataSource;
    @Override
    public ShoppingCart getByUserId(int userId) throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM shopping_cart_items WHERE user_id = ?")) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int productId = resultSet.getInt("product_id");
//                    int quantity = resultSet.getInt("quantity");
                    MySqlProductDao productDao = null;
                    Product product = productDao.getById(productId);


                    products.add(product);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
            return (ShoppingCart) products;
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
