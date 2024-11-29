package entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name =  "Users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UserId")
	private int id;
	@Column(name = "UserName", columnDefinition = "nvarchar(255)")
	private String username;

	@Column(name = "Role", columnDefinition = "nvarchar(255)")
	private String role;

	@Column(name = "Password", columnDefinition = "nvarchar(255)")
	private String password;

	@Column(name = "Email", columnDefinition = "nvarchar(255)")
	private String email;

	@Column(name = "ShippingAddress", columnDefinition = "nvarchar(255)")
	private String shippingAddress;

	@Column(name = "Phone", columnDefinition = "nvarchar(255)")
	private String phone;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getShippingAddress() {
	return shippingAddress;
}
public void setShippingAddress(String shippingAddress) {
	this.shippingAddress = shippingAddress;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}

public User(int id, String username, String role, String password, String email, String shippingAddress, String phone) {
	super();
	this.id = id;
	this.username = username;
	this.role = role;
	this.password = password;
	this.email = email;
	this.shippingAddress = shippingAddress;
	this.phone = phone;

}

public User(String username, String role, String password, String email,  String phone) {
	super();
	this.username = username;
	this.role = role;
	this.password = password;
	this.email = email;

	this.phone = phone;
}
public User(String username, String password, String email) {
	super();
	this.username = username;
	this.password = password;
	this.email = email;
}
public User() {

}


}