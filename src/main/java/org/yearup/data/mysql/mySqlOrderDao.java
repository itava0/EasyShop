package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.models.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class mySqlOrderDao extends MySqlDaoBase {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private ProductDao productDao;

    @Autowired
    public mySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    public ShoppingCart getCart(int userId) throws SQLException {
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

    public Order createOrder(Order order) {
        String sql = "INSERT INTO orders(user_id,date,address,city,state,zip,shipping_amount) VALUES (?,?,?,?,?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setInt(1, order.getUserId());
            statement.setDate(2, java.sql.Date.valueOf(order.getDate()));
            statement.setString(3, order.getAddress());
            statement.setString(4, order.getCity());
            statement.setString(5, order.getState());
            statement.setInt(6, order.getZip());
            statement.setDouble(7, order.getShippingAmount());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }


    public OrderLineItem createOrderLineItem(OrderLineItem orderLineItem) {
        String sql = "INSERT INTO order_line_items(order_id,product_id,sales_price,quantity,discount) VALUES (?,?,?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setInt(1, orderLineItem.getOrderId());
            statement.setInt(2, orderLineItem.getProductId());
            statement.setDouble(3, orderLineItem.getSalesPrice());
            statement.setInt(4, orderLineItem.getQuantity());
            statement.setDouble(5, orderLineItem.getDiscount());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderLineItem;
    }


}
