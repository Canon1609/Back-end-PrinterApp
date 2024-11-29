package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.*;
import javax.mail.Session;
import java.util.Properties;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dao.UserDAO;
import daoImpl.UseDAOImpl;
import entity.Category;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ManagerFactoryUtils;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet(urlPatterns = { "/api/v1/user", "/api/v1/user/*" })
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ManagerFactoryUtils managerFactoryUtils;
	private UserDAO userDAO;

	/**
	 * @throws ServletException
	 * @see HttpServlet#HttpServlet()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		managerFactoryUtils = new ManagerFactoryUtils();
		userDAO = new UseDAOImpl(managerFactoryUtils.getEntityManager());
	}

	public UserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String pathInfo = request.getPathInfo();
		Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
		PrintWriter out = response.getWriter();
		JsonObject jsonObject = new JsonObject();
		try {
			if (pathInfo == null || pathInfo.equals("/")) {
				List<User> users = new ArrayList<>();
				users = userDAO.getAllUser();
				response.setStatus(HttpServletResponse.SC_OK);
				out.print(gson.toJson(users));
			}	else if(pathInfo.equals("/search")) {
				String search = request.getParameter("search");
                List<User> users = userDAO.searchUser(search);
                if(users.isEmpty()) {
                    jsonObject.addProperty("message", "Không tìm thấy user!");
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(gson.toJson(jsonObject));
                }
                else {
                    response.setStatus(HttpServletResponse.SC_OK);
                    jsonObject.addProperty("message", "Tìm thấy " + users.size() + " user");
                    out.print(gson.toJson(users));
			}
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		String pathInfo = request.getPathInfo();
		Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
		JsonObject jsonObject = new JsonObject(); // tạo một đối tượng JSON Object để chứa thông tin trả về cho client
		PrintWriter out = response.getWriter(); // dùng để ghi thông tin trả về cho client
		try {
			if (pathInfo == null || pathInfo.equals("/")) {
				jsonObject.addProperty("message", "Invalid request");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				out.print(gson.toJson(jsonObject));
			}
			else {

				String idParam = pathInfo.substring(1);
				int id = Integer.parseInt(idParam);
				boolean deleteUser = userDAO.deleteUser(id);
				if (!deleteUser) {
					jsonObject.addProperty("message", "Không tìm thấy user!");
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					out.print(gson.toJson(jsonObject));
				} else {
					jsonObject.addProperty("message", "Xóa thành công !");
					jsonObject.addProperty("status", HttpServletResponse.SC_OK);
					response.setStatus(HttpServletResponse.SC_OK);
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		String pathInfo = request.getPathInfo();
		Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
		JsonObject jsonObject = new JsonObject();
		PrintWriter out = response.getWriter(); // dùng để ghi thông tin trả về cho client
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}
		String jsonBody = sb.toString();
		try {
			System.out.println(pathInfo);
			// khi người dùng signup
		    if(pathInfo.equals("/signup")) {
		    	
		    	User user = gson.fromJson(jsonBody, User.class);
		    	// xử lý tạo User từ jsonBody
		    	boolean checkExitMail = userDAO.checkExitMail(user.getEmail());
				if (checkExitMail) {
					jsonObject.addProperty("message", "Email đã tồn tại");
					jsonObject.addProperty("status", HttpServletResponse.SC_BAD_REQUEST);
					out.print(gson.toJson(jsonObject));
				} 
				else {
					boolean addUser = userDAO.addUser(user);
					if (addUser) {
						sendEmail(user.getEmail());
						jsonObject.addProperty("message", "Đăng kí  thành công");
						jsonObject.addProperty("status", HttpServletResponse.SC_CREATED);
						response.setStatus(HttpServletResponse.SC_CREATED);
						out.print(gson.toJson(jsonObject));
					} else {
						jsonObject.addProperty("message", "Thêm User thất bại");
						jsonObject.addProperty("status", HttpServletResponse.SC_BAD_REQUEST);
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						out.print(gson.toJson(jsonObject));
					}
				}
		    }
		    else
		    {
		    	User user = gson.fromJson(jsonBody, User.class);
				boolean checkExitMail = userDAO.checkExitMail(user.getEmail());
				if (checkExitMail) {
	                jsonObject.addProperty("message", "Email đã tồn tại");
	                jsonObject.addProperty("status", HttpServletResponse.SC_BAD_REQUEST);
	                out.print(gson.toJson(jsonObject));
	            } 
				else {
	                boolean addUser = userDAO.addUser(user);
	                if (addUser) {
	                	sendEmail(user.getEmail());
	                    jsonObject.addProperty("message", "Thêm User thành công");
	                    jsonObject.addProperty("status", HttpServletResponse.SC_CREATED);
	                    response.setStatus(HttpServletResponse.SC_CREATED);
	                } else {
	                    jsonObject.addProperty("message", "Thêm User thất bại");
	                    jsonObject.addProperty("status", HttpServletResponse.SC_BAD_REQUEST);
	                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                }
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
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		String pathInfo = request.getPathInfo();
		Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
		JsonObject jsonObject = new JsonObject();
		PrintWriter out = response.getWriter(); // dùng để ghi thông tin trả về cho client
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}
		String jsonBody = sb.toString();
		User user = gson.fromJson(jsonBody, User.class);

		try {
			if (pathInfo == null || pathInfo.equals("/")) {
				// tìm catgory theo id trước khi update
				jsonObject.addProperty("message", "User not found");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				boolean updateUser = userDAO.updateUser(user);
				if (updateUser) {
					jsonObject.addProperty("message", "Cập nhật User thành công");
					jsonObject.addProperty("status", HttpServletResponse.SC_OK);
					response.setStatus(HttpServletResponse.SC_OK);
				} else {
					jsonObject.addProperty("message", "Cập nhật User thất bại");
					jsonObject.addProperty("status", HttpServletResponse.SC_BAD_REQUEST);
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.print(gson.toJson(jsonObject));
			out.flush();
		}

	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
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
		message.setText(
				"Chúc mừng bạn đã tạo tài khoản thành công! Hãy đăng nhập để trải nghiệm dịch vụ tốt nhất từ chúng tôi.");

		// Gửi email
		Transport.send(message);
	}
}
