package daoImpl;

import java.util.List;

import dao.OrderDAO;
import entity.Order;
import jakarta.persistence.EntityManager;

public class OrderDAOImpl implements OrderDAO {
	private EntityManager entityManager;
	
	public OrderDAOImpl(EntityManager e) {
		this.entityManager = e;
	}
	@Override
	public void addOrder(Order order) {
		// TODO Auto-generated method stub
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(order);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Order getOrder(int id) {
		// TODO Auto-generated method stub
		try {
            return (Order) entityManager.createQuery("FROM Order WHERE id = :id").setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            }
		return null;
	}

	@Override
	public List<Order> getAllOrders() {
		// TODO Auto-generated method stub
		try {
            return entityManager.createQuery("FROM Order " , Order.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}

	@Override
	public void updateOrder(Order order) {
		// TODO Auto-generated method stub
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(order);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void deleteOrder(Order order) {
		// TODO Auto-generated method stub
		try {
			entityManager.getTransaction().begin();
			entityManager.remove(order);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public List<Order> getOrderByUserId(int userId) {
		try {
            return entityManager.createQuery("SELECT o FROM Order o WHERE o.user.id = :userId").setParameter("userId", userId).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	@Override
	public List<Order> getOrderByKeyword(String keyword) {
		try {
            return entityManager.createQuery("SELECT o FROM Order o WHERE o.shippingAddress LIKE :keyword or paymentMethod Like :keyword or status like :keyword").setParameter("keyword", "%" + keyword + "%").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
		return null;
	}

	}
	
}	
