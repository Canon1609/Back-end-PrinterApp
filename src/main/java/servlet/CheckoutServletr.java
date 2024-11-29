package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import dao.UserDAO;
import daoImpl.OrderDAOImpl;
import daoImpl.OrderItemDAOImpl;
import daoImpl.ProductDAOImpl;
import daoImpl.UseDAOImpl;
import entity.Order;
import entity.OrderItem;
import entity.Product;
import entity.User;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ManagerFactoryUtils;
import utils.TypeAdapters;

/**
 * Servlet implementation class CheckoutServletr
 */
@WebServlet(urlPatterns = {"/api/v1/checkout" , "/api/v1/checkout/*"})
public class CheckoutServletr extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ManagerFactoryUtils managerFactoryUtils;
    private OrderItemDAO orderItemDAO;
    private OrderDAO orderDAO;
    private UserDAO userDao;
    private ProductDAO productDAO;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckoutServletr() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		managerFactoryUtils = new ManagerFactoryUtils();
		orderDAO = new OrderDAOImpl(managerFactoryUtils.getEntityManager());
		orderItemDAO = new OrderItemDAOImpl(managerFactoryUtils.getEntityManager());
		userDao = new UseDAOImpl(managerFactoryUtils.getEntityManager());
		productDAO = new ProductDAOImpl(managerFactoryUtils.getEntityManager());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
          
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		response.setHeader("Access-Control-Allow-Credentials", "true");
	    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new TypeAdapters()).create();
		JsonObject responseData = new JsonObject();
		try
		{
		     // Đọc dữ liệu JSON từ request
	        BufferedReader reader = request.getReader();
	        StringBuilder jsonBuilder = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            jsonBuilder.append(line);
	        }
	        String json = jsonBuilder.toString();
	        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
	        String username = jsonObject.get("userName").getAsString();
	        String paymentMethod = jsonObject.get("paymentMethod").getAsString();
	        String shippingAddress = jsonObject.get("shippingAddress").getAsString();
	        double totalPrice = jsonObject.get("totalPrice").getAsDouble();
	        String status = jsonObject.get("status").getAsString();
	        String email = jsonObject.get("email").getAsString();
	        String phone = jsonObject.get("phone").getAsString();
	        LocalDate createAt = LocalDate.now();
	        JsonArray ListItemJsonArray = jsonObject.getAsJsonArray("items");
	        
	      
                  
           
	        // tìm user
	       User u = userDao.getUserByUsername(username);
			if (u == null) {
				responseData.addProperty("error", "User not found");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			// tạo order
			Order order = new Order();
			order.setUser(u);
			order.setPaymentMethod(paymentMethod);
			order.setShippingAddress(shippingAddress);
			order.setTotalPrice(totalPrice);
			order.setStatus(status);
			order.setCreateAt(createAt);
			orderDAO.addOrder(order);
			// tạo orderItem
			  for (int i = 0; i < ListItemJsonArray.size(); i++) {
					JsonObject itemJson = ListItemJsonArray.get(i).getAsJsonObject();
					int productId = itemJson.get("productId").getAsInt();
					int quantity = itemJson.get("quantity").getAsInt();
					double price = itemJson.get("price").getAsDouble();
					try {
						Product product = productDAO.getProductById(productId);
						if (product == null) {
							responseData.addProperty("error", "Product not found");
							response.setStatus(HttpServletResponse.SC_NOT_FOUND);
							return;
						}
						product.setInStock(product.getInStock() - quantity);
						OrderItem orderItem = new OrderItem(product, order, quantity, price);
						orderItemDAO.addOrderItem(orderItem);
					} catch (Exception e) {
						responseData.addProperty("error", "Product not found");
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						return;
					}
					
					
		        }
			response.setStatus(HttpServletResponse.SC_CREATED);
			response.getWriter().write(gson.toJson(order));
			

	        
		}
		catch (Exception e) {
			e.printStackTrace();
			 response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		     response.getWriter().write("{\"error\": \"Failed to fetch products\"}");
		}
		finally {
		   
		}
	
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
