package com.nisum.shoppingcart.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.nisum.shoppingcart.beans.CartItem;
import com.nisum.shoppingcart.beans.Product;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
@Configuration
@PropertySource(value="classpath:config.properties")
public class ProductController {

	private RestTemplate restTemplate = new RestTemplate();
	
	@Value("${shoppingcart.property.baseURL}")
	private String URL;

	// Product List page.
	@RequestMapping({ "/productList" })
	public @SuppressWarnings("unchecked") String listProductHandler(Model model) {
		String uri = URL + "product/";
		List<Product> products = restTemplate.getForObject(uri, List.class);
		model.addAttribute("list", products);
		return "productList";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.GET)
	public String shoppingCart(HttpServletRequest request, Model model) {
		String uri = URL + "cartItem/";
		List<CartItem> cartItems = restTemplate.getForObject(uri, List.class);
		model.addAttribute("list", cartItems);
		return "shoppingCart";
	}

	@RequestMapping({ "/addToCart" })
	public String addToCart(HttpServletRequest request, Model model,
			@RequestParam(value = "id", defaultValue = "") String id) {

		String uri = URL + "cartItem/" + id;
		restTemplate.put(uri, null);

		// Redirect to shoppingCart page.
		return "redirect:/shoppingCart";
	}


	@RequestMapping({ "/removeFromCart" })
	public String removeFromCart(HttpServletRequest request, Model model,
			@RequestParam(value = "id", defaultValue = "") String id) {
		String uri = URL + "cartItem/" + id;
		restTemplate.delete(uri);
		// Redirect to shoppingCart page.
		return "redirect:/shoppingCart";
	}

}
