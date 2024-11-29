package dao;

import java.util.List;

import entity.Order;
import entity.OrderItem;

public interface OrderItemDAO {
	
	public void addOrderItem(OrderItem orderItem);
	public List<OrderItem> getOrderItemsByOrder(Order order);
	public void updateOrderItem(OrderItem orderItem);
	public void deleteOrderItem(OrderItem orderItem);
	public OrderItem getOrderItemById(int id);
	List<OrderItem> getOrderItemByProductId(int productId);
}
