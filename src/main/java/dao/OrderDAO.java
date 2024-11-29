package dao;

import java.util.List;

import entity.Order;

public interface OrderDAO {
	public void addOrder(Order order);
	public Order getOrder(int id);
	public List<Order> getAllOrders();

	public void updateOrder(Order order);
	public void deleteOrder(Order order);
	List<Order> getOrderByUserId(int userId);
	List<Order> getOrderByKeyword(String keyword);
}	
