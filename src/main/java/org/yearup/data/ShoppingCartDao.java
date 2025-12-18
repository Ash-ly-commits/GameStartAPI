package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    void addProduct(int userId, int productId);
    void updateProduct(int userId, int productID, ShoppingCartItem shoppingCartItem);
    void clearCart(int userId);
}