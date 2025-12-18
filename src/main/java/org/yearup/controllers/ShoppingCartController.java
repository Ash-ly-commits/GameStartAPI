package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("shopping_cart")
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController
{
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao){
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @GetMapping()
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();


            // use the shoppingcartDao to get all items in the cart and return the cart
//            return shoppingCartDao.getByUserId(userId); i assume lol
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
        }
    }

    // add a POST method to add a product to the cart
    @PostMapping("products/{productId}")
    public void addProduct(@PathVariable int productId, Principal principal){

    }


    // add a PUT method to update an existing product in the cart
    @PutMapping("products/{productId}")
    public void updateShoppingCart(@PathVariable int productID, @RequestBody ShoppingCartItem shoppingCartItem,
                                   Principal principal){

    }


    // add a DELETE method to clear all products from the current users cart
    @DeleteMapping
    public void clearCart(Principal principal){

    }

}
