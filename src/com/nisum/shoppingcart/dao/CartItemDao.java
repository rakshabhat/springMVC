package com.nisum.shoppingcart.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nisum.shoppingcart.beans.CartItem;

@Component
public class CartItemDao {
	
	@Autowired
	private ProductDao productDao;
	private static HashMap<String, CartItem> cartItems = new HashMap<>();
		
	
	
	public int addToCart(String cartItemID) {

		if (productDao.getProductByID(cartItemID) == null) {
			// CartItem not found
			return 0;
		}
		CartItem cartItem = new CartItem();
		if (cartItems.containsKey(cartItemID)) {
			cartItem = cartItems.get(cartItemID);
			cartItem.setTotalPrice(cartItem.getTotalPrice() + cartItem.getProduct().getPrice());
			cartItem.setQuantity(cartItem.getQuantity() + 1);

		} else {
			cartItem.setProduct(productDao.getProductByID(cartItemID));
			cartItem.setTotalPrice(cartItem.getProduct().getPrice());
			cartItem.setQuantity(1);
		}

		cartItems.put(cartItemID, cartItem);
		return 1;
	}
	
	
	public int addToCart(CartItem cartItem) {
		
		String cartItemID = cartItem.getProduct().getId();
		if (productDao.getProductByID(cartItemID) == null) {
			// CartItem not found
			return 0;
		}
		cartItems.put(cartItemID, cartItem);
		return 1;
	}

	public int removeFromCart(String cartItemID) {
		if (productDao.getProductByID(cartItemID) == null) {
			// CartItem not found
			return 0;
		}
		CartItem cartItem = new CartItem();
		if (cartItems.containsKey(cartItemID)) {
			cartItem = cartItems.get(cartItemID);
			cartItem.setTotalPrice(cartItem.getTotalPrice() - cartItem.getProduct().getPrice());
			if (cartItem.getQuantity() > 1) {
				cartItem.setQuantity(cartItem.getQuantity() - 1);
			} else {
				cartItems.remove(cartItemID);

			}

		} else {
			cartItems.remove(cartItemID);
		}

		return 1;
	}

	public List<CartItem> getCartItems() {
		return new ArrayList<CartItem>(cartItems.values());
	}
	
	public CartItem getCartItemByID(String cartItemID) {
		return cartItems.get(cartItemID);
	}
	
	public void deleteCartItem(String id) {
		cartItems.remove(id);
	}
	
	public void createCartItem(CartItem cartItem) {
		cartItems.put(cartItem.getProduct().getId(), cartItem);
	}
	
	public void updateCartItem(CartItem cartItem) {
		cartItems.put(cartItem.getProduct().getId(), cartItem);
	}

}
