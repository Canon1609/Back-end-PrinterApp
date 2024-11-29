package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dao.CouponDAO;
import daoImpl.CouponDAOImpl;
import entity.Category;
import entity.Coupon;
import entity.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ManagerFactoryUtils;

/**
 * Servlet implementation class CouponServlet
 */
@WebServlet(urlPatterns = {"/api/v1/coupons/*" , "/api/v1/coupons"})
public class CouponServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ManagerFactoryUtils managerFactoryUtils;
    private CouponDAO couponDAO;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CouponServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	public void init() throws ServletException {
		super.init();
		managerFactoryUtils = new ManagerFactoryUtils();
		couponDAO = new  CouponDAOImpl(managerFactoryUtils.getEntityManager());
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   	response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        Gson gson = new GsonBuilder().serializeNulls().create();
        PrintWriter out = response.getWriter();
        String pathInfo = request.getPathInfo();
        JsonObject jsonObject = new JsonObject();
        ObjectMapper objectMapper = new ObjectMapper(); // Khởi tạo ObjectMapper của Jackson
        objectMapper.registerModule(new JavaTimeModule()); // Đăng ký module để chuyển đổi LocalDate
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        objectMapper.registerModule(javaTimeModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
				if (pathInfo == null || pathInfo.equals("/")) {
					   List<Coupon> coupons = new ArrayList<>();
					   coupons = couponDAO.getAllCoupon();
					   if(coupons == null) {
						   jsonObject.addProperty("message", "No Coupons found");
						   response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					   }
					   else{
						  jsonObject.addProperty("message", "Coupons found");
		                  jsonObject.addProperty("total", coupons.size());
		                  // dùng ObjectMapper để chuyển đổi object java thành JSON
		                String json = objectMapper.writeValueAsString(coupons);
		                // dùng gson để chuyển đổi json thành JsonObject
		                jsonObject.add("data", gson.fromJson(json, JsonArray.class));
		                jsonObject.addProperty("status", response.getStatus());
		                  out.println(jsonObject.toString());

		               }
				}
				else {
					jsonObject.addProperty("message", "Invalid request");
					jsonObject.addProperty("status", HttpServletResponse.SC_BAD_REQUEST);
					out.println(jsonObject.toString());
				}
	}
	catch (Exception e) {
		e.printStackTrace();
		jsonObject.addProperty("message", "Internal server error");
		jsonObject.addProperty("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		out.println(jsonObject.toString());
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
				String couponCode = request.getParameter("couponCode");
				String typeCoupon = request.getParameter("typeCoupon");
				String valueCouponStr = request.getParameter("valueCoupon");
				String startDateStr = request.getParameter("startDate");
				String endDateStr = request.getParameter("endDate");
				String minPurchaseStr = request.getParameter("minPurchase");
				String usageLimitStr = request.getParameter("usageLimit");
			    String usageCountStr = request.getParameter("usageCount");
				
				if(couponCode == "" ||typeCoupon == "" || valueCouponStr == "" || usageLimitStr == "" ) {
					jsonObject.addProperty("message", "Invalid request");
					jsonObject.addProperty("status", HttpServletResponse.SC_BAD_REQUEST);
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
				else if(usageCountStr == "" ||startDateStr == "" || endDateStr == "" || minPurchaseStr == "" ) {
					int usageCount = 0;
					double minPurchase = 0;
					LocalDate startDate = LocalDate.now();
					LocalDate endDate = startDate.plusMonths(1);
					
				}
				else {
					int valueCoupon = Integer.parseInt(valueCouponStr);
					double minPurchase = Double.parseDouble(minPurchaseStr);
					int usageLimit = Integer.parseInt(usageLimitStr);
					int usageCount = Integer.parseInt(usageCountStr);
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					LocalDate startDate = LocalDate.parse(startDateStr, formatter);
					LocalDate endDate = LocalDate.parse(endDateStr, formatter);
					Coupon coupon = new Coupon(couponCode, typeCoupon, valueCoupon, startDate, endDate, minPurchase,
							usageLimit, usageCount);
					boolean addCoupon = couponDAO.insertCoupon(coupon);
					if (addCoupon) {
						jsonObject.addProperty("message", "Thêm mã giảm giá thành công");
						jsonObject.addProperty("status", HttpServletResponse.SC_OK);
					} else {
						jsonObject.addProperty("message", "Thêm mã giảm giá không thành công");
						jsonObject.addProperty("status", HttpServletResponse.SC_BAD_REQUEST);
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					}
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
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
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
		PrintWriter out = response.getWriter();
		try {
				if (pathInfo == null || pathInfo.equals("/update")) {
				int id = Integer.parseInt(request.getParameter("id"));
				String couponCode = request.getParameter("couponCode");
				String typeCoupon = request.getParameter("typeCoupon");
				String valueCouponStr = request.getParameter("valueCoupon");
				String startDateStr = request.getParameter("startDate");
				String endDateStr = request.getParameter("endDate");
				String minPurchaseStr = request.getParameter("minPurchase");
				String usageLimitStr = request.getParameter("usageLimit");
			    String usageCountStr = request.getParameter("usageCount");
			    Coupon coupon = couponDAO.getCouponById(id);
			    if(coupon == null ) {
					jsonObject.addProperty("message", "Coupon not found");
					jsonObject.addProperty("status", HttpServletResponse.SC_NOT_FOUND);
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
				else 
				{
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					LocalDate startDate = LocalDate.parse(startDateStr, formatter);
					LocalDate endDate = LocalDate.parse(endDateStr, formatter);
				    coupon.setCode(couponCode);
				    coupon.setDiscount_type(typeCoupon);
				    coupon.setDiscount_value(Integer.parseInt(valueCouponStr));
				    coupon.setStart_date(startDate);
				    coupon.setEnd_date(endDate);
				    coupon.setMin_purchase(Double.parseDouble(minPurchaseStr));
				    coupon.setUsage_limit(Integer.parseInt(usageLimitStr));
				    coupon.setUsage_count(Integer.parseInt(usageCountStr));
					boolean updateCoupon = couponDAO.updateCoupon(coupon);
					if (updateCoupon) {
						jsonObject.addProperty("message", "Cập nhật mã khuyến mãi thành công");
						jsonObject.addProperty("status", HttpServletResponse.SC_OK);

					} else {
						jsonObject.addProperty("message", "Cập nhật mã khuyến mãi không thành công");
						jsonObject.addProperty("status", HttpServletResponse.SC_BAD_REQUEST);
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					}
				}
			    
				}

			 else {
				jsonObject.addProperty("message", "Yêu cầu không hợp lệ");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			 }
		}
		 catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.print(gson.toJson(jsonObject));
			out.flush();
			out.close();
		}
	}
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
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
        PrintWriter out = response.getWriter();
        try {
            if (pathInfo == null || pathInfo.equals("/delete")) {
                int id = Integer.parseInt(request.getParameter("id"));
                boolean deleteCoupon = couponDAO.deleteCoupon(id);
                if (!deleteCoupon) {
                    jsonObject.addProperty("message", "Không tìm thấy mã khuyến mãi !");
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                } 
                else {
                    jsonObject.addProperty("message", "Xóa mã khuyến mãi thành công !");
                    jsonObject.addProperty("status", HttpServletResponse.SC_OK);
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            } 
            
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

	

}
