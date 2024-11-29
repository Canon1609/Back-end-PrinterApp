package daoImpl;

import java.util.List;

import dao.CouponDAO;
import entity.Coupon;
import jakarta.persistence.EntityManager;

public class CouponDAOImpl implements CouponDAO {
	EntityManager em;

	public CouponDAOImpl(EntityManager em) {
		this.em = em;
	}
	@Override
	public List<Coupon> getAllCoupon() {
		try {
			return em.createQuery("FROM Coupon", Coupon.class).getResultList();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Coupon getCouponByCode(String code) {
		try {
            return em.find(Coupon.class, code);
        }
        catch (Exception e) {
            e.printStackTrace();
            }
		return null;
	}

	@Override
	public boolean insertCoupon(Coupon coupon) {
		try {
			em.getTransaction().begin();
			em.persist(coupon);
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateCoupon(Coupon coupon) {
		 try {
			 em.getTransaction().begin();
			 em.merge(coupon);
			 em.getTransaction().commit();
			 return true;
		 }
			catch (Exception e) {
				e.printStackTrace();
			}
		return false;
	}

	@Override
	public boolean deleteCoupon(int  id) {
		try {
            Coupon coupon = em.find(Coupon.class, id);
            em.getTransaction().begin();
            em.remove(coupon);
            em.getTransaction().commit();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
		return false;
	}
	@Override
	public Coupon getCouponById(int id) {
		try {
			return em.find(Coupon.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
