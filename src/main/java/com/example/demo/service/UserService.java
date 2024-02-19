package com.example.demo.service;

import java.util.List;

import com.example.demo.exception.UserException;
import com.example.demo.model.User;

public interface UserService {

	public User findUserById(Long userId) throws UserException;
	public User findUserProfileBtJwt(String jwt)throws UserException;
	public User updateUser(Long userId, User user)throws UserException;
	
	public User followUser(Long userId,User user)throws UserException;

	public List<User> searchUser(String query);
	
}
