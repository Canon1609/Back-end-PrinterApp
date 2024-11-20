package controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dao.UserDAO;
import daoImpl.UserDAOImpl;
import entites.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ManagerFactoryUtils;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet( urlPatterns = {"/api/v1/user", "/api/v1/user/*"} )
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     private ManagerFactoryUtils managerFactoryUtils;
	private UserDAO userDAO;
    /**
     * @throws ServletException
     * @see HttpServlet#HttpServlet()
     */
    @Override
	public void  init () throws ServletException {
    	super.init();
    	managerFactoryUtils = new ManagerFactoryUtils();
    	userDAO = new UserDAOImpl(managerFactoryUtils.getEntityManager());
    }
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		   response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		   response.setContentType("application/json");
	       response.setCharacterEncoding("UTF-8");
	       String pathInfo = request.getPathInfo();
	       Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
	       PrintWriter out = response.getWriter();
	       try {
				if (pathInfo == null || pathInfo.equals("/")) {
					   	List<User> users = new ArrayList<>();
						users = userDAO.getAllUser();
			            response.setStatus(HttpServletResponse.SC_OK);
			            out.print(gson.toJson(users));
				}
				else if("/role".equals(pathInfo)) {
					int id = Integer.parseInt(request.getParameter("id"));
					response.setStatus(HttpServletResponse.SC_OK);

				} else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			out.flush();
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		   response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
	        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
	        response.setHeader("Access-Control-Allow-Credentials", "true");
         Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
         PrintWriter out = response.getWriter();
         JsonObject jsonObject = new JsonObject();
         try {
           String pathInfo = request.getPathInfo();
           System.out.println("PathInfo: " + pathInfo);
           // xử lý thêm mới 1 user vào db
           if(pathInfo.equals("/add")) {
        	   	String username = request.getParameter("username");
        	   	String password = request.getParameter("password");
        	   	String role = request.getParameter("role");
        	   	String email = request.getParameter("email");
        	   	String phone = request.getParameter("phone");
        	   	User user = new User(username, role, password, email, phone);
        	   	boolean isAddUser = userDAO.addUser(user);
        	   	if(isAddUser) {
					response.setStatus(HttpServletResponse.SC_OK);
					jsonObject.addProperty("message", "Add user success");
					jsonObject.addProperty("status", HttpServletResponse.SC_CREATED);

				} else {
					jsonObject.addProperty("message", "Add user fail");
					jsonObject.addProperty("status", HttpServletResponse.SC_BAD_REQUEST);

        	   	}
        	   	out.print(gson.toJson(jsonObject));
           }
           
           if(pathInfo.equals("/signup")) {
        	    StringBuilder sb = new StringBuilder();
                BufferedReader reader = request.getReader();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                JsonObject jsonRequest = JsonParser.parseString(sb.toString()).getAsJsonObject();
                String username = jsonRequest.get("username").getAsString();
                String password = jsonRequest.get("password").getAsString();
                String email = jsonRequest.get("email").getAsString();
        	   boolean check = userDAO.checkExitMail(email);
        
				if (userDAO.checkExitMail(email)) {
					jsonObject.addProperty("message", "Email already exists");
					jsonObject.addProperty("status", HttpServletResponse.SC_BAD_REQUEST);
					out.print(gson.toJson(jsonObject));
					return;
				}
				else {
					  User user = new User(username, password, email);
					   if(userDAO.addUser(user))
		        	   {
		        		   jsonObject.addProperty("message", "Signup success");
		        		   jsonObject.addProperty("status", HttpServletResponse.SC_CREATED);
		        		   sendEmail(email);
		        	   }
						else {
							jsonObject.addProperty("message", "Signup fail");
							jsonObject.addProperty("status", HttpServletResponse.SC_BAD_REQUEST);
						}
						out.print(gson.toJson(jsonObject));
						
				}
        	 
        
           }

        } catch (Exception e) {
		e.printStackTrace();
	} finally {
		out.flush();
		out.close();
	}
  }
	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
		PrintWriter out = response.getWriter();
		JsonObject jsonObject = new JsonObject();
		try {
			String pathInfo = request.getPathInfo();
			if (pathInfo.equals("/update")) {
				int id = Integer.parseInt(request.getParameter("id"));
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				String role = request.getParameter("role");
				String email = request.getParameter("email");
				String phone = request.getParameter("phone");
				User user = userDAO.getUserById(id);
				if (user == null) {
					jsonObject.addProperty("message", "Update fail, user not found");
					jsonObject.addProperty("status", HttpServletResponse.SC_NOT_FOUND);
					out.print(gson.toJson(jsonObject));
					return;
				}
				else {
					user.setUsername(username);
					user.setPassword(password);
					user.setRole(role);
					user.setEmail(email);
					user.setPhone(phone);
					boolean isUpdate = userDAO.updateUser(user);
					if (isUpdate) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        jsonObject.addProperty("message", "Update user success");
                        jsonObject.addProperty("status", HttpServletResponse.SC_OK);
                    } else {
                        jsonObject.addProperty("message", "Update user fail");
                        jsonObject.addProperty("status", HttpServletResponse.SC_BAD_REQUEST);
				}

				out.print(gson.toJson(jsonObject));
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}
	 @Override
	    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        // Header CORS cho preflight request
	        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
	        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
	        response.setHeader("Access-Control-Allow-Credentials", "true");
	        response.setStatus(HttpServletResponse.SC_OK);
	    }
	 
	 // Hàm gửi email thông báo đăng ký thành công
	    private void sendEmail(String recipientEmail) throws MessagingException {
	        String senderEmail = "phuochuynguyen1002@gmail.com";
	        String senderPassword = "xcfr qwdd quvv qusr";
	        
	        // Cấu hình properties cho mail server
	        Properties properties = new Properties();
	        properties.put("mail.smtp.host", "smtp.gmail.com"); // SMTP của nhà cung cấp dịch vụ
	        properties.put("mail.smtp.port", "587"); // Thông thường là 587 cho TLS hoặc 465 cho SSL
	        properties.put("mail.smtp.auth", "true");
	        properties.put("mail.smtp.starttls.enable", "true");
	         
	        // Tạo authenticator
	        javax.mail.Authenticator auth = new javax.mail.Authenticator() {
	        	@Override
	            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
	                return new javax.mail.PasswordAuthentication(senderEmail, senderPassword);
	            }
	        };
	        // Tạo session với authentication
			Session session = Session.getInstance(properties, auth);
				
			
				
			
	        // Tạo thông điệp email
	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(senderEmail));
	        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
	        message.setSubject("Tạo tài khoản thành công từ website printer-shop");
	        message.setText("Chúc mừng bạn đã tạo tài khoản thành công! Hãy đăng nhập để trải nghiệm dịch vụ tốt nhất từ chúng tôi.");

	        // Gửi email
	        Transport.send(message);
	    }
}
