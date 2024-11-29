package daoImpl;

import java.util.List;

import dao.CategoryDAO;
import entity.Category;
import entity.Product;
import jakarta.persistence.EntityManager;

public class CategoryDAOImpl implements CategoryDAO {
	private EntityManager entityManager;

	public CategoryDAOImpl(EntityManager e) {
		this.entityManager = e;
	}
	@Override
	public List<Category> getAllCategory() {
		// TODO Auto-generated method stub
		try {
            return entityManager.createQuery("FROM Category " , Category.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();

        }
	return null;
	}

	@Override
	public Category getCategoryById(int id) {
		try {
            return (Category) entityManager.createQuery("FROM Category WHERE id = :id").setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}

	@Override
	public boolean insertCategory(Category category) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(category);
			entityManager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateCategory(Category category) {
		try {
            entityManager.getTransaction().begin();
            entityManager.merge(category);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
	}

	@Override
	public boolean deleteCategory(int id) {
		try {
			// tìm category theo id
			Category category = entityManager.find(Category.class, id);
			List<Product> products = category.getProducts(); // lấy danh sách product của category đó và set category cuar product đó = null trước khi xóa category
			for (Product product : products) {
                product.setCategory(null);
			}
			// thực hiện xóa category
			entityManager.getTransaction().begin();
			entityManager.remove(category);
			entityManager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
