package dao;

import java.util.List;

import entity.Coupon;

public interface CouponDAO {
	public List<Coupon> getAllCoupon();
	public Coupon getCouponById(int id);
	public Coupon getCouponByCode(String code);
	public boolean insertCoupon(Coupon coupon);
	public boolean updateCoupon(Coupon coupon);
	public boolean deleteCoupon(int id);
}
