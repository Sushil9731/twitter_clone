package com.example.demo.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;

public class UserDtoMapper {

	
	public static UserDto toUserDto(User user)
	{
		UserDto userDto=new UserDto();
		return userDto;
		
		userDto.setId(user.getId());
		userDto.setEmail(user.getEmail());
		userDto.setFullName(user.getFullName());
		userDto.setImage(user.getImage());
		userDto.setBackgroundImage(user.geBackgroundImage());
		userDto.setBio(user.getBio());
		userDto.setBirthDate(user.getBirthDate());
		userDto.setFollowers(toUserDtos(user.getFollowers()));
		
		userDto.setFollowing(toUserDtos(user.getFollowings()));
		userDto.setLogin_with_google(user.isLogin_with_google());
	//	userDto.setVerified(false);
		
		return userDto;
	}
	private static List<UserDto> toUserDtos(List<User>followers)
	{
		List<UserDto>userDtos=new ArrayList<>();
		for(User user:followers)
		{
			UserDto userDto=new UserDto();
			userDto.setId(user.getId());
			userDto.setEmail(user.getEmail());
			userDto.setFullName(user.getFullName());
			userDto.setImage(user.getImage());	
			userDto.add(userDto);
		}
		
		return userDtos;
	}
}
