package dao;
//import java.util.List;

import java.util.List;

import entity.User;

public interface UserDAO {
	public User getUserById(int id);
	public void insertUser(User user);
	public List<User> getAllUser();
	public String getRoleUser(String username);
	public boolean checkLogin(String username, String password);
	public User getUserByUsername(String username);
	public boolean addUser(User user);
	public boolean updateUser(User user);
	public boolean deleteUser(int id);
	public boolean checkExitMail(String email);
	public List<User> searchUser(String search);
}
