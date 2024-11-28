package entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name = "Category")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CategoryId")
	private int id;
	@Column(name = "CategoryName" , columnDefinition = "nvarchar(255)")
	private String name;
	@Column(name = "Description" , columnDefinition = "nvarchar(255)")
	private String description;
	@Column(name = "ImgUri" , columnDefinition = "nvarchar(255)")
	private String imguri;
	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Product> products;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImguri() {
		return imguri;
	}
	public void setImguri(String imguri) {
		this.imguri = imguri;
	}

	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public Category(int id, String name, String description, String imguri, List<Product> products) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.imguri = imguri;
		this.products = products;
	}
	public Category(String name, String description, String imguri, List<Product> products) {
		super();
		this.name = name;
		this.description = description;
		this.imguri = imguri;
		this.products = products;
	}
	public Category() {

	}


}
