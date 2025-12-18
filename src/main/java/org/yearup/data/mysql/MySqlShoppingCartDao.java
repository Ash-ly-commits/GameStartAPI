package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource){ super(dataSource); }

    @Override
    public ShoppingCart getByUserId(int userId)
    {
        ShoppingCart cart = new ShoppingCart();

        String sql = """
        SELECT
            p.product_id,
            p.name,
            p.price,
            p.category_id,
            p.description,
            p.subcategory,
            p.stock,
            p.featured,
            p.image_url,
            sc.quantity,
            sc.discount_percent
        FROM shopping_cart sc
        JOIN products p ON sc.product_id = p.product_id
        WHERE sc.user_id = ?;
    """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, userId);
            ResultSet results = statement.executeQuery();

            while (results.next())
            {
                Product product = mapRow(results);

                ShoppingCartItem item = new ShoppingCartItem();
                item.setProduct(product);
                item.setQuantity(results.getInt("quantity"));
                item.setDiscountPercent(results.getBigDecimal("discount_percent"));

                cart.add(item);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return cart;
    }

    @Override
    public void addProduct(int userId, int productId)
    {
        String checkSql = " SELECT quantity " +
                " FROM shopping_cart " +
                " WHERE user_id = ? AND product_id = ?; ";

        String insertSql = "INSERT INTO shopping_cart(user_id, product_id, quantity, discount_percent) " +
                " VALUES (?, ?, 1, 0); ";

        String updateSql = "UPDATE shopping_cart " +
                " SET quantity = quantity + 1 " +
                " WHERE user_id = ? AND product_id = ?; ";

        try (Connection connection = getConnection())
        {
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, productId);

            ResultSet rs = checkStmt.executeQuery();

            if (rs.next())
            {
                PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                updateStmt.setInt(1, userId);
                updateStmt.setInt(2, productId);
                updateStmt.executeUpdate();
            }
            else
            {
                PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, productId);
                insertStmt.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateProduct(int productId, int userId, ShoppingCartItem shoppingCartItem)
    {
        String sql = "UPDATE shopping_cart" +
                " SET quantity = ? " +
                " WHERE user_id = ? AND product_id = ?;";

        try(Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, shoppingCartItem.getQuantity());
            statement.setInt(2, userId);
            statement.setInt(3, productId);

            statement.executeUpdate();

        }catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearCart(int userId)
    {
        String sql = "DELETE FROM shopping_cart " +
                " WHERE user_id = ?;";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected static Product mapRow(ResultSet row) throws SQLException
    {
        int productId = row.getInt("product_id");
        String name = row.getString("name");
        BigDecimal price = row.getBigDecimal("price");
        int categoryId = row.getInt("category_id");
        String description = row.getString("description");
        String subCategory = row.getString("subcategory");
        int stock = row.getInt("stock");
        boolean isFeatured = row.getBoolean("featured");
        String imageUrl = row.getString("image_url");

        return new Product(productId, name, price, categoryId, description, subCategory, stock, isFeatured, imageUrl);
    }
}
