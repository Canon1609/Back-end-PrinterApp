package utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ManagerFactoryUtils {
	private EntityManagerFactory emf;
	private EntityManager em;

	public ManagerFactoryUtils() {
		emf = Persistence.createEntityManagerFactory("QuanLiMayIn");
		em = emf.createEntityManager();
	}
	public EntityManager getEntityManager() {
        return em;
    }

	public static void main(String[] args) {
		ManagerFactoryUtils managerFactoryUtils = new ManagerFactoryUtils();
		EntityManager em = managerFactoryUtils.getEntityManager();
		System.out.println(em);
	}


}

