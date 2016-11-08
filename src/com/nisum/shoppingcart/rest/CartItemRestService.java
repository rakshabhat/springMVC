package com.nisum.shoppingcart.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nisum.shoppingcart.beans.CartItem;
import com.nisum.shoppingcart.dao.CartItemDao;

@RestController
@RequestMapping("cartItem")
public class CartItemRestService {

	@Autowired
	private CartItemDao cartItemDao;

	/*
	 * Returns list of cart Items
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<List<CartItem>> listCartItemHandler(Model model) {
		return new ResponseEntity<List<CartItem>>(cartItemDao.getCartItems(), HttpStatus.OK);
	}

	/*
	 * Returns CartItem for given Cart Item ID
	 */
	@RequestMapping(value = "/{cartItemId}", method = RequestMethod.GET, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CartItem> getCartItemById(@PathVariable String cartItemId, Model model) throws IOException {
		
		CartItem cartItem = cartItemDao.getCartItemByID(cartItemId);
		if(cartItem == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(cartItem, HttpStatus.OK);
	}

	/*
	 * Deletes Cart Item for given Cart ID
	 */
	@RequestMapping(value = "/{cartItemId}", method = RequestMethod.DELETE)
	public ResponseEntity<CartItem> deleteCartItem(@PathVariable("cartItemId") String cartItemId) {
		CartItem cartItem = cartItemDao.getCartItemByID(cartItemId);
		if (cartItem == null) {
			System.out.println("Unable to delete. cartItem with id " + cartItemId + " not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		cartItemDao.removeFromCart(cartItemId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/*
	 * Method to Creating Cart Item for given Cart ID
	 */
	@RequestMapping(value = "/{cartId}", method = RequestMethod.POST)
	public ResponseEntity<CartItem> updateUser(@RequestBody CartItem cartItem) {
		System.out.println("Creating product ");
		cartItemDao.addToCart(cartItem);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/*
	 * Method to Updating Cart Item for given Cart ID
	 */
	@RequestMapping(value = "/{cartId}", method = RequestMethod.PUT)
	public ResponseEntity<CartItem> updateUser(@PathVariable("cartId") String cartItemID) {
		System.out.println("Updating cartItemID " + cartItemID);
		if(cartItemDao.addToCart(cartItemID) == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

}
