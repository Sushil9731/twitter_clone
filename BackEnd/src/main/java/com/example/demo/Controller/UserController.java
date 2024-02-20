package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.mapper.UserDtoMapper;
import com.example.demo.exception.UserException;
import com.example.demo.model.User;
import com.example.demo.services.UserService;
import com.example.demo.util.UserUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	@GetMapping("/profile")
	public ResponseEntity<UserDto>getUserProfile(@RequestHeader("Authorization")String jwt)throws UserException
	{
		
		User user=userService.findUserProfileByJwt(jwt);
		var userDto=UserDtoMapper.toUserDto(user);
		userDto.setReq_user(true);
		return new ResponseEntity<>(userDto,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{userid}")
	public ResponseEntity<UserDto>getUserById(@PathVariable Long userId,@RequestHeader("Authorization")String jwt)throws UserException
	{
		
		User requser=userService.findUserProfileByJwt(jwt);
		
		User user=userService.findUserById(userId);
		var userDto=UserDtoMapper.toUserDto(user);
		userDto.setReq_user(UserUtil.isReqUser(reqUser, user));
		userDto.setFollowed(UserUtil.isFollowedByReqUser(reqUser, user));
		return new ResponseEntity<>(userDto,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<UserDto>>searchUser(@RequestParam String query,@RequestHeader("Authorization")String jwt)throws UserException
	{
		
		User requser=userService.findUserProfileByJwt(jwt);
		
		List<User> users=userService.searchUser(query);
		var userDtos=UserDtoMapper.toUserDtos(users);
		//userDto.setReq_user(UserUtil.isReqUser(reqUser, user));
		//userDto.setFollowed(UserUtil.isFollowedByReqUser(reqUser, user));
		return new ResponseEntity<>(userDtos,HttpStatus.ACCEPTED);
	}
	
	
	
	@PutMapping("/update")
	public ResponseEntity<UserDto>searchUser(@RequestBody User req,@RequestHeader("Authorization")String jwt)throws UserException
	{
		
		User requser=userService.findUserProfileByJwt(jwt);
		
		User user=userService.updateUser(reqUser.getId(),req);
		var userDto=UserDtoMapper.toUserDto(user);
		//userDto.setReq_user(UserUtil.isReqUser(reqUser, user));
		//userDto.setFollowed(UserUtil.isFollowedByReqUser(reqUser, user));
		return new ResponseEntity<>(userDto,HttpStatus.ACCEPTED);
	}
	
	
	
	@PutMapping("/{userId}/follow")
	public ResponseEntity<UserDto>searchUser(@PathVariable Long userId,@RequestHeader("Authorization")String jwt)throws UserException
	{
		
		User requser=userService.findUserProfileByJwt(jwt);
		
		User user=userService.followUser(userId,reqUser);
		var userDto=UserDtoMapper.toUserDto(user);
		userDto.setFollowed(UserUtil.isFollowedByReqUser(reqUser, user));
		
		return new ResponseEntity<>(userDto,HttpStatus.ACCEPTED);
	}
	
}
