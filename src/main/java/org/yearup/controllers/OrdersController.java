package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.OrderDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/orders")
@PreAuthorize("isAuthenticated()")
public class OrdersController
{
    private final OrderDao orderDao;
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProfileDao profileDao;

    @Autowired
    public OrdersController(
            OrderDao orderDao,
            ShoppingCartDao shoppingCartDao,
            UserDao userDao,
            ProfileDao profileDao)
    {
        this.orderDao = orderDao;
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.profileDao = profileDao;
    }

    @PostMapping
    public void checkout(Principal principal)
    {
        try
        {
            String username = principal.getName();
            User user = userDao.getByUserName(username);

            ShoppingCart cart = shoppingCartDao.getByUserId(user.getId());
            if (cart.getItems().isEmpty())
            {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Shopping cart is empty..."
                );
            }

            Profile profile = profileDao.getByUserId(user.getId());
            if (profile == null)
            {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User profile not found"
                );
            }

            Order order = new Order();
            order.setUserId(user.getId());
            order.setShippingAmount(BigDecimal.ZERO);
            order.setAddress(profile.getAddress());
            order.setCity(profile.getCity());
            order.setState(profile.getState());
            order.setZip(profile.getZip());

            order = orderDao.create(order);

            for (ShoppingCartItem item : cart.getItems().values())
            {
                OrderLineItem lineItem = new OrderLineItem();
                lineItem.setOrderId(order.getOrderId());
                lineItem.setProductId(item.getProductId());
                lineItem.setSalesPrice(item.getProduct().getPrice());
                lineItem.setQuantity(item.getQuantity());
                lineItem.setDiscount(item.getDiscountPercent());

                orderDao.addLineItem(lineItem);
            }

            shoppingCartDao.clearCart(user.getId());
        }
        catch(ResponseStatusException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Checkout failed...."
            );
        }
    }
}