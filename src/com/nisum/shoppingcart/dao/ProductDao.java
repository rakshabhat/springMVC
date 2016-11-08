package com.nisum.shoppingcart.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nisum.shoppingcart.beans.Product;

@Component
public class ProductDao {
	
	private static HashMap<String, Product> dummyproducts = new HashMap<>();
	

	public ProductDao() {
		
		Product product = new Product();
		product.setId("1");
		product.setName("Michael Kors Watches Darci Watch");
		product.setPrice(250);
		dummyproducts.put(product.getId(), product);
		
		Product product2 = new Product();
		product2.setId("2");
		product2.setName("Michael Kors Watches Parker Watch");
		product2.setPrice(150);
		dummyproducts.put(product2.getId(), product2);
		
		Product product3 = new Product();
		product3.setId("3");
		product3.setName("Michael Kors Quartz Stainless Steel Casual Watch");
		product3.setPrice(200);
		dummyproducts.put(product3.getId(), product3);	
	}
	
	
	
	public Product getProductByID(String productID) {
		return dummyproducts.get(productID);
	}
	
	public List<Product> getProductList() {
		return new ArrayList<Product>(dummyproducts.values());
	}
	
	public void deleteProduct(String id) {
		dummyproducts.remove(id);
	}
	
	public void createProduct(Product product) {
		dummyproducts.put(product.getId(), product);
	}
	
	public void updateProduct(Product product) {
		dummyproducts.put(product.getId(), product);
	}
	

}

