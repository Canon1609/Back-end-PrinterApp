 package entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "Orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "OrderId" , columnDefinition = "int")
	private int id;
	@ManyToOne
	@JoinColumn(name = "UserId" , nullable = true)
	@JsonManagedReference
	private User user;
	@Column(name = "TotalPrice" , columnDefinition = "float")
	private double totalPrice;
	@Column(name = "Status" , columnDefinition = "nvarchar(255)")
	private String status;
	@Column(name = "ShippingAddress" , columnDefinition = "nvarchar(255)")
	private String shippingAddress;
	@Column(name = "CreateAt" , columnDefinition = "date")
	private LocalDate createAt;
	@Column(name = "PaymentMethod" , columnDefinition = "nvarchar(255)")
	private String paymentMethod;
	
	@OneToMany(mappedBy = "order")
	@JsonManagedReference
	private transient  List<OrderItem> orderItems;
	
	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUserId() {
		return user;
	}
	public void setUserId(User userId) {
		this.user = userId;
	}
	
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public LocalDate getCreateAt() {
		return createAt;
	}
	public void setCreateAt(LocalDate createAt) {
		this.createAt = createAt;
	}
	
	public Order( User user, double totalPrice, String status, String shippingAddress, LocalDate createAt,
			String paymentMethod, List<OrderItem> orderItems) {
		super();
		
		this.user = user;
		this.totalPrice = totalPrice;
		this.status = status;
		this.shippingAddress = shippingAddress;
		this.createAt = createAt;
		this.paymentMethod = paymentMethod;
		this.orderItems = orderItems;
	}
	public Order() {
		super();
	}
	

}
