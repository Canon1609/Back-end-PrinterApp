package servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dao.CategoryDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import daoImpl.CategoryDAOImpl;
import daoImpl.OrderDAOImpl;
import daoImpl.OrderItemDAOImpl;
import daoImpl.ProductDAOImpl;
import entity.Category;
import entity.OrderItem;
import entity.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ManagerFactoryUtils;

/**
 * Servlet implementation class ProductServlet
 */
@WebServlet(urlPatterns = { "/api/v1/products", "/api/v1/products/*" })
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ManagerFactoryUtils managerFactoryUtils;
	private ProductDAO productDAO;
	private CategoryDAO categoryDAO;
	private OrderItemDAO  orderItemDAO;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException {
		super.init();
		managerFactoryUtils = new ManagerFactoryUtils();
		productDAO = new ProductDAOImpl(managerFactoryUtils.getEntityManager());
		categoryDAO = new CategoryDAOImpl(managerFactoryUtils.getEntityManager());
		orderItemDAO =  new OrderItemDAOImpl(managerFactoryUtils.getEntityManager());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=UTF-8");
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
		Gson gson1 = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
		System.out.println("pathInfo: " + pathInfo);
		try {
			if (pathInfo == null || pathInfo.equals("/")) {
				List<Product> products = new ArrayList<>();
				products = productDAO.getAllProducts();
				if (products == null) {
					jsonObject.addProperty("message", "No products found");
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				} else {
					jsonObject.addProperty("message", "Products found");
					jsonObject.addProperty("total", products.size());
					// get category name by id of each product in list products then add to json
					// object

					// dùng ObjectMapper để chuyển đổi object java thành JSON
					String json = objectMapper.writeValueAsString(products);
					// dùng gson để chuyển đổi json thành JsonObject
					jsonObject.add("data", gson.fromJson(json, JsonArray.class));
					jsonObject.addProperty("status", response.getStatus());
					out.println(jsonObject.toString());
				}
			} else {
				// Lấy ID sản phẩm từ URL
				String idParam = pathInfo.substring(1); // Bỏ dấu "/"
				try {
					String[] pathParts = pathInfo.split("/");
					int id = Integer.parseInt(pathParts[1]);
					// Gọi DAO để lấy sản phẩm theo ID
					Product product = productDAO.getProductById(id);
					if (product == null) {
						jsonObject.addProperty("message", "Sản phẩm không tồn tại");
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					} else {
						jsonObject.addProperty("message", "Lấy sản phẩm thành công");
						// dùng ObjectMapper để chuyển đổi object java thành JSON
						String json = objectMapper.writeValueAsString(product);
						// dùng gson để chuyển đổi json thành JsonObject
						jsonObject.add("data", gson.fromJson(json, JsonObject.class));
						response.setStatus(HttpServletResponse.SC_OK);
						out.println(jsonObject.toString());

					}
				} catch (NumberFormatException e) {
					jsonObject.addProperty("message", "ID không hợp lệ");
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
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
	 * @throws InterruptedException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

//    public  String uploadBase64ImageToSirv(String token, String base64Data, String remotePath) throws Exception {
//    	String SIRV_UPLOAD_URL = "https://api.sirv.com/v2/files/upload";
//    	   base64Data = base64Data.replaceAll("\\s+", ""); // Loại bỏ khoảng trắng, xuống dòng
//        // Giải mã Base64 thành byte array
//        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
//
//        // Tạo file tạm thời từ dữ liệu Base64
//        File tempFile = File.createTempFile("upload", ".jpg");
//        Files.write(tempFile.toPath(), decodedBytes);
//
//        // Tạo HTTP Client
//        try (CloseableHttpClient client = HttpClients.createDefault()) {
//            HttpPost post = new HttpPost(SIRV_UPLOAD_URL + "?filename=/" + remotePath);
//
//            // Đính kèm file
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            builder.addBinaryBody("file", tempFile, org.apache.hc.core5.http.ContentType.DEFAULT_BINARY, tempFile.getName());
//            post.setEntity(builder.build());
//
//            // Thêm header Authorization
//            post.addHeader("Authorization", "Bearer " + token);
//
//            // Gửi request
//            try (CloseableHttpResponse response = client.execute(post)) {
//                int statusCode = response.getCode();
//                String responseBody = new String(response.getEntity().getContent().readAllBytes());
//
//                // Xóa file tạm
//                tempFile.delete();
//
//                if (statusCode == 200 || statusCode == 201) {
//                    // Trả về đường dẫn Sirv
//                    return "https://your-sirv-url.com/" + remotePath;
//                } else {
//                    throw new RuntimeException("Tải ảnh thất bại: " + responseBody);
//                }
//            }
//        }
//    }
	 private String uploadToSirv(String filePath) throws IOException, InterruptedException {
	        // API Endpoint của Sirv
	        String uploadUrl = "https://api.sirv.com/v2/files/upload";

	        // Token Sirv (Thay bằng token thật)
	        String sirvToken = getBearerToken();

	        // Tạo HTTP client và gửi request
	        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
	            HttpPost uploadRequest = new HttpPost(uploadUrl);

	            // Tạo form-data
	            File file = new File(filePath);
	            HttpEntity entity = MultipartEntityBuilder.create()
	                    .addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, file.getName())
	                    .build();

	            // Cấu hình header
	            uploadRequest.setEntity(entity);
	            uploadRequest.setHeader("Authorization", "Bearer " + sirvToken);

	            // Gửi request và nhận phản hồi
	            try (CloseableHttpResponse response = httpClient.execute(uploadRequest)) {
	                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	                StringBuilder jsonResponse = new StringBuilder();
	                String line;
	                while ((line = reader.readLine()) != null) {
	                    jsonResponse.append(line);
	                }

	                // Trích xuất URL từ JSON phản hồi
	                JsonObject responseJson = JsonParser.parseString(jsonResponse.toString()).getAsJsonObject();
	                return responseJson.get("fileUrl").getAsString();
	            }
	        }
	    }
	    private File saveBase64ToFile(String base64Data) throws IOException {
	        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
	        File tempFile = File.createTempFile("temp-image", ".png");
	        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
	            fos.write(decodedBytes);
	        }
	        return tempFile;
	    }
    private String cleanBase64String(String base64String) {
        if (base64String.startsWith("data:image")) {
            return base64String.substring(base64String.indexOf(",") + 1);
        }
        return base64String;
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        setCorsHeaders(response);

        JsonObject jsonResponse = new JsonObject();
        PrintWriter out = response.getWriter();

        try (BufferedReader reader = request.getReader()) {
            // Read JSON payload
            StringBuilder jsonBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }

            // Parse JSON
            JsonObject jsonObject = JsonParser.parseString(jsonBody.toString()).getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            String description = jsonObject.get("description").getAsString();
            String base64Data = cleanBase64String(jsonObject.get("img").getAsString());
            System.out.println("base64Data: " + base64Data);
            // Save Base64 image to temp file
            File tempFile = saveBase64ToFile(base64Data);

            // Upload to Sirv
            String imageUrl = uploadToSirv(tempFile.getAbsolutePath());
            System.out.println("imageUrl: " + imageUrl);

            // Chuẩn bị phản hồi thành công
            jsonResponse.addProperty("message", "Thêm sản phẩm thành công");
            jsonResponse.addProperty("imageUrl", imageUrl);
            response.setStatus(HttpServletResponse.SC_CREATED);
            // Save Product to Database
//            Product product = new Product();
//            product.setName(name);
//            product.setDescription(description);
//            product.setImageUrl(imageUrl);
//
//            if (productDAO.addProduct(product)) {
//                jsonResponse.addProperty("message", "Product added successfully");
//                jsonResponse.addProperty("imageUrl", imageUrl);
//                response.setStatus(HttpServletResponse.SC_CREATED);
//            } else {
//                throw new Exception("Failed to add product to database");
//            }

        } catch (IllegalArgumentException e) {
            jsonResponse.addProperty("error", "Invalid Base64 data");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.addProperty("error", "An error occurred: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            out.print(jsonResponse.toString());
            out.flush();
            out.close();
        }
    }
//			throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		response.setContentType("application/json");
//		response.setCharacterEncoding("UTF-8");
//		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
//		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
//		response.setHeader("Access-Control-Allow-Credentials", "true");
//		String pathInfo = request.getPathInfo();
//		Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
//		JsonObject jsonObject = new JsonObject(); // tạo một đối tượng JSON Object để chứa thông tin trả về cho client
//		PrintWriter out = response.getWriter(); // dùng để ghi thông tin trả về cho client
//		StringBuilder jsonBody = new StringBuilder();
//		try (BufferedReader reader = request.getReader()) {
//			String line;
//			while ((line = reader.readLine()) != null) {
//				jsonBody.append(line);
//			}
//		}
//		// chuyển đổi dữ liệu từ dạng JSON sang đối tượng Product
//		JsonObject jsonObject1 = gson.fromJson(jsonBody.toString(), JsonObject.class);
//		String base64Data =  cleanBase64String(jsonObject1.get("img").toString()); 
//		   // Giải mã Base64 và lưu thành file tạm
//        String tempFilePath = "C:\\temp\\temp-image.png"; // Đường dẫn file tạm
//        byte[] imageBytes = Base64.getDecoder().decode(base64Data.replaceFirst("^data:image/\\w+;base64,", ""));
//        try (FileOutputStream fos = new FileOutputStream(tempFilePath)) {
//            fos.write(imageBytes);
//        }
//
//		try {
//			String imageUrl = uploadToSirv(tempFilePath);
//			System.out.println("Token: " + imageUrl);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	
//		// đọc dữ liêu từ request gửi lên từ client
//
//		try {
//			if (pathInfo == null || pathInfo.equals("/add")) {
//			String name = jsonObject1.get("name").getAsString();
//			String description = jsonObject1.get("description").getAsString();
//			
//
//			}
//			// xử lý khi client gửi request không hợp lệ va trả về status code 400
//			else {
//				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			out.print(gson.toJson(jsonObject));
//			out.flush();
//			out.close();
//
//		}
//
//	}
	
 
	// hàm lấy bearer token từ sirv 
	private String getBearerToken() throws MalformedURLException, IOException, InterruptedException {		
		// Tạo HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Tạo body dữ liệu cho request
        Map<String, String> requestData = new HashMap<>();
        requestData.put("clientId", "3vdHm5GkmIZ9AKh20LChr1sCnH4");
        requestData.put("clientSecret", "PUioZSDOQlwsuMcu0xLZFFNDgiLfaB+wzP4o5Ni49n12XvnSjhPpOASbcGPZRtGH0qivFu0qXMMxebBfMuBw0A==");
        // Chuyển body thành JSON
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(requestData);

        // Tạo HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.sirv.com/v2/token"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();


        // Gửi request và nhận response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            // Phân tích response JSON để lấy token
            Map<String, Object> responseData = mapper.readValue(response.body(), Map.class);
            return (String) responseData.get("token");
        } else {
            throw new RuntimeException("Failed to get Bearer Token: " + response.body());
        }

	}
	  private void setCorsHeaders(HttpServletResponse response) {
	        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
	        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
	        response.setHeader("Access-Control-Allow-Credentials", "true");
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
//		Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
		JsonObject jsonObject = new JsonObject();
		PrintWriter out = response.getWriter();
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}
		String jsonBody = sb.toString();
		Gson gson = new Gson();
		Product product = gson.fromJson(jsonBody, Product.class);
		Category category = categoryDAO.getCategoryById(product.getCategoryId());
		if (category == null) {
			jsonObject.addProperty("message", "Category not found");
			jsonObject.addProperty("status", HttpServletResponse.SC_NOT_FOUND);
			out.print(gson.toJson(jsonObject));
		}
		product.setCategory(category);
		try {
			if (pathInfo == null || pathInfo.equals("/")) {
				jsonObject.addProperty("message", "Invalid request");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				out.print(gson.toJson(jsonObject));
				return;
			}

			boolean updateProduct = productDAO.updateProduct(product);
			if (updateProduct) {
				jsonObject.addProperty("message", "Cập nhật sản phẩm thành công");
				jsonObject.addProperty("status", HttpServletResponse.SC_OK);
			} else {
				jsonObject.addProperty("message", "Cập nhật sản phẩm không thành công");
				jsonObject.addProperty("status", HttpServletResponse.SC_BAD_REQUEST);
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

			if (pathInfo == null || pathInfo.equals("/")) {
				jsonObject.addProperty("message", "Invalid request");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {

				String idParam = pathInfo.substring(1);
				int id = Integer.parseInt(idParam);
				Product product = productDAO.getProductById(id);
				List<OrderItem> orderItems = orderItemDAO.getOrderItemByProductId(id);
				for (OrderItem orderItem : orderItems) {
					orderItem.setProduct(null);
				}
				boolean deleteProduct = productDAO.deleteProduct(id);
				if (!deleteProduct) {
					jsonObject.addProperty("message", "Không tìm thấy sản phẩm !");
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				} else {
					jsonObject.addProperty("message", "Xóa thành công !");
					jsonObject.addProperty("status", HttpServletResponse.SC_OK);
					response.setStatus(HttpServletResponse.SC_OK);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.print(gson.toJson(jsonObject));
			out.flush();
			out.close();
		}
	}
	
	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setStatus(HttpServletResponse.SC_OK);
	}

}
