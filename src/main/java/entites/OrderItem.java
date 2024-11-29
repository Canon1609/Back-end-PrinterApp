package entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "OrderItems")
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name = "ProductId")
	private transient Product product;
	
	@ManyToOne
	@JoinColumn(name = "OrderId")
	@JsonBackReference // giúp tránh việc lặp vô hạn khi chuyển sang json
	private Order order;
	
	@Column(name = "Quantity")
	private int quantity;
	@Column(name = "Price")
	private double price;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public OrderItem(Product product, Order order, int quantity, double price) {
		super();
		this.product = product;
		this.order = order;
		this.quantity = quantity;
		this.price = price;
	}
	public OrderItem() {
		super();
	}
	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", product=" + product + ", order=" + order + ", quantity=" + quantity
				+ ", price=" + price + "]";
	}
	
	
}	
