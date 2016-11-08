package com.nisum.shoppingcart.rest;

import com.nisum.shoppingcart.beans.Product;
import com.nisum.shoppingcart.dao.ProductDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductRestService {

	@Autowired
	private ProductDao productDao;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<List<Product>> listProducts() {
		return new ResponseEntity<List<Product>>(productDao.getProductList(), HttpStatus.OK);
	}

	@RequestMapping(value = "/{productId}", method = RequestMethod.GET, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> getProductById(@PathVariable String productId, Model model) throws IOException {
		Product product = productDao.getProductByID(productId);
		if (product == null) {
			System.out.println("Unable to get product with id " + productId + " not found");
			return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/{productId}", method = RequestMethod.DELETE)
	public ResponseEntity<Product> deleteProduct(@PathVariable("productId") String productId) {
		Product product = productDao.getProductByID(productId);
		if (product == null) {
			System.out.println("Unable to delete. product with id " + productId + " not found");
			return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}
		productDao.deleteProduct(productId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<Void> createProduct(@RequestBody Product product) {
		System.out.println("Creating product " + product.getName());

		if (productDao.getProductByID(product.getId()) != null) {
			System.out.println("Product " + product.getName() + " already found");

			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		productDao.createProduct(product);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{productId}", method = RequestMethod.PUT)
	public ResponseEntity<Product> updateProduct(@PathVariable("productId") String productId,
			@RequestBody Product product) {
		System.out.println("Updating product " + productId);

		Product currentProduct = productDao.getProductByID(productId);

		if (currentProduct == null) {
			System.out.println("currentProduct with id " + productId + " not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		currentProduct.setName(product.getName());
		currentProduct.setPrice(product.getPrice());
		productDao.updateProduct(currentProduct);
		return new ResponseEntity<>(currentProduct, HttpStatus.NO_CONTENT);
	}

}
