package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.type.descriptor.java.LocalDateJavaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import dao.OrderDAO;
import daoImpl.OrderDAOImpl;
import entity.Order;
import entity.User;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.FloatAdapters;
import utils.ManagerFactoryUtils;
import utils.TypeAdapters;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet(urlPatterns = {"/api/v1/orders/*" ,"/api/v1/orders/" })
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ManagerFactoryUtils managerFactoryUtils;
	private OrderDAO order;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		managerFactoryUtils = new ManagerFactoryUtils();
		order = new OrderDAOImpl( managerFactoryUtils.getEntityManager());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String pathInfo = request.getPathInfo();
	    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new TypeAdapters()).registerTypeAdapter(Double.class, new FloatAdapters()).create();
		PrintWriter out = response.getWriter();
		JsonObject jsonObject = new JsonObject();
		try {
			if (pathInfo == null || pathInfo.equals("/")) {
				List<Order> orders = order.getAllOrders();
				jsonObject.addProperty("message", "Lấy danh sách order thành công!");
				jsonObject.addProperty("total", orders.size());
				
				response.setStatus(HttpServletResponse.SC_OK);
				out.print(gson.toJson(orders));
			}
			else if(pathInfo.equals("/search")) {
				String search = request.getParameter("search");
                List<Order> orders = order.getOrderByKeyword(search);
                if(orders.isEmpty()) {
                    jsonObject.addProperty("message", "Không tìm thấy order");
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(gson.toJson(jsonObject));
                }
                else {
                    response.setStatus(HttpServletResponse.SC_OK);
                    jsonObject.addProperty("message", "Tìm thấy " + orders.size() + " user");
                    out.print(gson.toJson(orders));
			}
			}
//			} else {
//				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
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
