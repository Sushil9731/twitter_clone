package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.User;

// need two methods fineByemail and search user.
// auto bydefault method is avilable by extending jparepository .methods are delete,update,add
public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findByEmail(String email);// naming convection..if want to not use naming then use query 
	// using query annotation query is select user from u where u.email=email
	
	@Query("SELECT DISTINCT u FROM User WHERE u.fullName LIKE %:query% OR u.email LIKE %:query%")
	public List<User>searchUser(@Param("query")String query);
	
	//for example code is usrname and yoou type code then it match then return user .if not match then return false
	
	
}
