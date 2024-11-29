package utils;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenUtil {
	
    // Khóa bí mật cố định, mã hóa bằng Base64
    private static final String SECRET_KEY_STRING = "bmd1eWVucGh1b2NodXkxMDAyMjAwM2Roa3RwbTE3YWdmaGZienZjeGtqYXNqa2Fkc2tqYXNoZGthanNibm5iemN6bW4seGJja2phaGlxd2Fic2prenhramNramFlaWpxd3pkYmNha2poYmRrYWpoZGtqenhiY3ptbmJ4Y2Fqa2RhamtjYWtzamhkcXdoZGFnZGthamhkY2t6bmNiemNqa2N6eGNramh1aXdlaXVxaGFzY2p6YnZmYw=="; // Thay thế bằng chuỗi cố định của bạn
    private static final byte[] SECRET_KEY_BYTES = Base64.getDecoder().decode(SECRET_KEY_STRING);
    private static final Key SECRET_KEY = new SecretKeySpec(SECRET_KEY_BYTES, SignatureAlgorithm.HS512.getJcaName());
	// thời gian sống của token
	private static final long EXPIRE_TIME = 864000000; // 10 days


    // Map lưu trữ token cho mỗi người dùng
    private static final Map<String, String> userTokens = new ConcurrentHashMap<>();

	// phương thức tạo token
	 public static String generateToken(String username) {
		 if (userTokens.containsKey(username)) {
	            return userTokens.get(username); // Trả về token đã tồn tại
	        }

	        // Nếu token chưa tồn tại, tạo token mới
	        String token = Jwts.builder()
	                .setSubject(username)
	                .setIssuedAt(new Date(System.currentTimeMillis()))
	                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
	                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
	                .compact();

	        // Lưu token mới vào Map
	        userTokens.put(username, token);

	        return token;
	    }
	 
	 public static boolean isTokenValid(String token) {
	        try {
	            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
	            return true; // Token hợp lệ
	        } catch (Exception e) {
	            return false; // Token không hợp lệ
	        }
	    }
	 

}
