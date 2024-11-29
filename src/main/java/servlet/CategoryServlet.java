package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import dao.CategoryDAO;
import daoImpl.CategoryDAOImpl;
import entity.Category;
import entity.Product;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ManagerFactoryUtils;

/**
 * README servlet này xử lý các request liên quan đến category doGet: lấy danh
 * sách category doPost: thêm mới category , xóa category doPut: update category
 * test các api này bằng cách sử dụng postman theo url sau
 * http://localhost:8080/BE_PRINTER/api/v1/xxx (xxx là đường dẫn tương ứng) Ví
 * dụ: http://localhost:8080/BE_PRINTER/api/v1/categories để lấy danh sách
 * category http://localhost:8080/BE_PRINTER/api/v1/categories/add để thêm mới
 * category nếu có lỗi xảy ra sẽ trả về status code và message thông báo lỗi ví
 * dụ: {"message":"Category not found"} và status code 404
 */
@WebServlet(urlPatterns = { "/api/v1/categories", "/api/v1/categories/*" })
public class CategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ManagerFactoryUtils managerFactoryUtils;
	private CategoryDAO categoryDAO;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CategoryServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		managerFactoryUtils = new ManagerFactoryUtils();
		categoryDAO = new CategoryDAOImpl(managerFactoryUtils.getEntityManager());
	}

	/**
	 * @see Servlet#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
		ObjectMapper mapper = new ObjectMapper(); // dùng để chuyển đổi object java thành JSON hoặc ngược lại
		PrintWriter out = response.getWriter();
		try {
			if (pathInfo == null || pathInfo.equals("/")) {
				List<Category> categories = new ArrayList<>();
				categories = categoryDAO.getAllCategory();
				out.println(mapper.writeValueAsString(categories));
			}
		} catch (Exception e) {
			// TODO: handle exception
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
		JsonObject jsonObject = new JsonObject(); // tạo một đối tượng JSON Object để chứa thông tin trả về cho client
		PrintWriter out = response.getWriter(); // dùng để ghi thông tin trả về cho client
		try {
			if (pathInfo == null || pathInfo.equals("/add")) {
				String name = request.getParameter("name");
				String description = request.getParameter("description");
				String imguri = request.getParameter("imguri");
				Category category = new Category(name, description, imguri, null);
				if (categoryDAO.insertCategory(category)) {
					jsonObject.addProperty("message", "Insert category successfully");
					response.setStatus(HttpServletResponse.SC_CREATED);
				} else {
					jsonObject.addProperty("message", "Insert category failed");
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
			// xử lý khi client gửi request không hợp lệ va trả về status code 400
			else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.print(gson.toJson(jsonObject));
			out.flush();
			out.close();
		}

	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
		    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		    response.setHeader("Access-Control-Allow-Credentials", "true");
		   

		String pathInfo = request.getPathInfo();
		Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
		JsonObject jsonObject = new JsonObject(); // tạo một đối tượng JSON Object để chứa thông tin trả về cho client
		PrintWriter out = response.getWriter(); // dùng để ghi thông tin trả về cho client
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}
		String jsonBody = sb.toString();
		Category category = gson.fromJson(jsonBody, Category.class);
		try {
			if (pathInfo == null || pathInfo.equals("/")) {
				// tìm catgory theo id trước khi update
					jsonObject.addProperty("message", "Category not found");
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				} else {
					
//					Category categoryById = categoryDAO.getCategoryById(category.getId());
					boolean updateCategory = categoryDAO.updateCategory(category);
					if (updateCategory) {
						jsonObject.addProperty("message", "Update category successfully");
						jsonObject.addProperty("status", HttpServletResponse.SC_OK);
						response.setStatus(HttpServletResponse.SC_OK);
					} else {
						jsonObject.addProperty("message", "Update category failed");
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
	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
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
			System.out.println(pathInfo);
			if (pathInfo == null || pathInfo.equals("/")) {
				jsonObject.addProperty("message", "Invalid request");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				out.print(gson.toJson(jsonObject));
			} else {

				String idParam = pathInfo.substring(1);
				int id = Integer.parseInt(idParam);
				boolean deleteProduct = categoryDAO.deleteCategory(id);
				if (!deleteProduct) {
					jsonObject.addProperty("message", "Không tìm thấy loại sản phẩm!");
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
	 * @see HttpServlet#doOptions(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
	    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
	    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
	    response.setHeader("Access-Control-Allow-Credentials", "true");
	    response.setStatus(HttpServletResponse.SC_OK);
	}

//	  private void setCorsHeaders(HttpServletResponse response) {
//	        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
//	        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//	        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
//	        response.setHeader("Access-Control-Allow-Credentials", "true");
//	    }

}
