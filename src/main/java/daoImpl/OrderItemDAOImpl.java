package daoImpl;

import java.util.List;

import dao.OrderItemDAO;
import entity.Order;
import entity.OrderItem;
import jakarta.persistence.EntityManager;

public class OrderItemDAOImpl implements OrderItemDAO {
	private EntityManager entityManager;

	public OrderItemDAOImpl(EntityManager e) {
		this.entityManager = e;
	}
	@Override
	public void addOrderItem(OrderItem orderItem) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(orderItem);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public List<OrderItem> getOrderItemsByOrder(Order order) {
		// TODO Auto-generated method stub
		try {
            return entityManager.createQuery("FROM OrderItem WHERE order = :order", OrderItem.class).setParameter("order", order).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}

	@Override
	public void updateOrderItem(OrderItem orderItem) {
		// TODO Auto-generated method stub
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(orderItem);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteOrderItem(OrderItem orderItem) {
		// TODO Auto-generated method stub
		try {
			entityManager.getTransaction().begin();
			entityManager.remove(orderItem);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public OrderItem getOrderItemById(int id) {
		// TODO Auto-generated method stub
		try {
            return (OrderItem) entityManager.createQuery("FROM OrderItem WHERE id = :id").setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
		return null;
        }

	}
	@Override
	public List<OrderItem> getOrderItemByProductId(int productId) {
		// TODO Auto-generated method stub
		try {
			return entityManager.createQuery("FROM OrderItem WHERE product.id = :productId", OrderItem.class)
					.setParameter("productId", productId).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
