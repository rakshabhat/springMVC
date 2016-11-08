package com.nisum.shoppingcart.beans;

public class CartItem {

	Product product;
	double quantity;
	double totalPrice;
	
	public CartItem(Product product, double quantity, double totalPrice) {
		this.product = product;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}
	
	public CartItem() {
	}

	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	
}
