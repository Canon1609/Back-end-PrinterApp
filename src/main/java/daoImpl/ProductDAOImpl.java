package daoImpl;

import java.util.List;

import dao.ProductDAO;
import entity.Order;
import entity.Product;
import jakarta.persistence.EntityManager;

public class ProductDAOImpl implements ProductDAO {
	private EntityManager entityManager;

	public ProductDAOImpl(EntityManager e) {
        this.entityManager = e;
        }
	@Override
	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		try {
            return entityManager.createQuery("FROM Product" , Product.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Product getProductById(int id) {
		Product product = null;
		// TODO Auto-generated method stub
		try {
            product =  entityManager.find(Product.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return product;
	}

	@Override
	public boolean addProduct(Product product) {
		// TODO Auto-generated method stub
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(product);
			entityManager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateProduct(Product product) {
		// TODO Auto-generated method stub
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(product);
			entityManager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			   if (entityManager.getTransaction().isActive()) {
		          entityManager.getTransaction().rollback();
		        }
		}
		return false;
	}

	@Override
	public boolean deleteProduct(int id) {
		// TODO Auto-generated method stub
		try {
			entityManager.getTransaction().begin();
			Product product = entityManager.find(Product.class, id);
			entityManager.remove(product);
			entityManager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
