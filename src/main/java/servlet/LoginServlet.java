package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dao.UserDAO;
import daoImpl.UseDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ManagerFactoryUtils;
import utils.TokenUtil;

/**
 * Servlet implementation class LoginServlet
 */
// Cấu hình UrlPattern cho Servlet . Quy ước đặt tên urlPattern theo dạng /api/v1/...
@WebServlet(urlPatterns = {"/api/v1/login", "/api/v1/login/*"})
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	   private ManagerFactoryUtils managerFactoryUtils;
		private UserDAO userDAO;
		private TokenUtil tokenUtil;
    /**
     * @see HttpServlet#HttpServlet()
     */
		  @Override
		public void  init () throws ServletException {
		    	super.init();
		    	managerFactoryUtils = new ManagerFactoryUtils();
		    	userDAO = new UseDAOImpl(managerFactoryUtils.getEntityManager());
		    	tokenUtil = new TokenUtil();
		    }
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 // Thiết lập response là JSON
        response.setContentType("application/json"); // Response trả về JSON
        response.setCharacterEncoding("UTF-8"); // Encoding UTF-8 cho response
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000"); // chấp nhận request từ domain khác localhost:3000
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE"); // Chấp nhận method POST , GET, DELETE
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization"); // Chấp nhận header Authorization
        response.setHeader("Access-Control-Allow-Credentials", "true"); // chập nhận cookie
        PrintWriter out = response.getWriter(); // Lấy PrintWriter từ response để ghi dữ liệu vào body của response
        JsonObject jsonObject = new JsonObject(); // Tạo JsonObject để chứa dữ liệu trả về
     // Đọc dữ liệu JSON từ body
        StringBuilder sb = new StringBuilder(); // Tạo StringBuilder để đọc dữ liệu từ body
        BufferedReader reader = request.getReader(); // Lấy BufferedReader từ request để đọc dữ liệu từ body
        String line; // Biến để đọc dữ liệu từng dòng
        while ((line = reader.readLine()) != null) {
            sb.append(line); // Đọc dữ liệu từng dòng và thêm vào StringBuilder
        }
        String requestBody = sb.toString(); // Convert StringBuilder sang String
        // Kiểm tra xem request body có dữ liệu hay không
		if (requestBody == null || requestBody.isEmpty()) {
			jsonObject.addProperty("message", "Request body is missing");
			jsonObject.addProperty("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			out.print(jsonObject.toString());
			out.flush();
			return;
		}
        // Phân tích JSON để lấy username và password
        jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();
        boolean check = userDAO.checkLogin(username, password);
        if(check) {
        	String role = userDAO.getRoleUser(username);
        	String token = TokenUtil.generateToken(username);
        	jsonObject.addProperty("message", "Login success");
        	jsonObject.addProperty("status", 200);
        	jsonObject.addProperty("token", token);
        	jsonObject.addProperty("role", role);
        	jsonObject.addProperty("username", username);
        }
        else
		{
			jsonObject.addProperty("message", "Login fail");
			jsonObject.addProperty("status", 404);
		}
        out.print(jsonObject.toString());
        out.flush();
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

}
