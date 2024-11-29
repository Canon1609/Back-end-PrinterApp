package dao;

import java.util.List;

import entity.Product;

public interface ProductDAO {
	public List<Product> getAllProducts();
	public Product getProductById(int id);
	public boolean addProduct(Product product);
	public boolean updateProduct(Product product);
	public boolean deleteProduct(int id);
}	
