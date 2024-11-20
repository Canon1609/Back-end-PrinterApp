package daoImpl;

import java.util.List;

import dao.UserDAO;
import entites.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class UserDAOImpl implements UserDAO {
	private EntityManager entityManager;

	public UserDAOImpl(EntityManager e ) {
		this.entityManager = e;
	}
	@Override
	public void insertUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<User> getAllUser() {
		return entityManager.createQuery("FROM User").getResultList();
	}

	// get role của user theo username
	@Override
	public String getRoleUser(String username) {
		try {
			User user = (User) entityManager.createQuery("FROM User WHERE username = :username").setParameter("username", username).getSingleResult();
			if (user != null) {
				return user.getRole();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	@Override
	public boolean checkLogin(String username, String password) {
	    // TODO Auto-generated method stub
		try {
			User user = (User) entityManager.createQuery("FROM User WHERE username = :username AND password = :password")
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
            if(user != null) {
                return true;
            }
			else {
				return false;
			}
        } catch (Exception e) {
            e.printStackTrace();
		}

		return false;
	}
	@Override
	public User getUserByUsername(String username) {
		try {
            return (User) entityManager.createQuery("FROM User WHERE username = :username").setParameter("username", username).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
		return null;
	}

	}
	@Override
	public boolean addUser(User user) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(user);
			entityManager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public boolean updateUser(User user) {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(user);
			entityManager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public User getUserById(int  id) {
		try {
			return entityManager.find(User.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public boolean deleteUser(int id) {
		// TODO Auto-generated method stub
		try {
			entityManager.getTransaction().begin();
			User user = entityManager.find(User.class, id);
			entityManager.remove(user);
			entityManager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public boolean checkExitMail(String email) {
	    try {
	        User user = (User) entityManager.createQuery("FROM User WHERE email = :email", User.class)
	                                        .setParameter("email", email)
	                                        .getSingleResult();
	        return user != null; // Nếu user không null, email tồn tại
	    } catch (NoResultException e) {
	        // Nếu không có kết quả, email không tồn tại
	        return false;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

}
