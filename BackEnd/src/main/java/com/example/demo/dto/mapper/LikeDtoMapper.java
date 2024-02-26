package com.example.demo.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.dto.LikeDto;
import com.example.demo.dto.TwitDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.Like;
import com.example.demo.model.User;

public class LikeDtoMapper {

	
	public static LikeDto toLikeDto(Like like,User reqUser) {
		
		UserDto user=UserDtoMapper.toUserDto(like.getUser());
		var reqUserDto=UserDtoMapper.toUserDto(reqUser);
		TwitDto twit =TwitDtoMapper.toTwitDto(like.getTwit(),reqUser);
		
		var likeDto=new LikeDto();
		likeDto.setId(like.getId());
		likeDto.setTwit(twit);
		likeDto.setUser(user);
		
		return likeDto;
		
	}

	public static List<LikeDto> toLikeDtos(List<Like> likes,User reqUser) {
		
		List<LikeDto> likeDtos=new ArrayList<>() ;
		
		for(Like like:likes) {
			UserDto user=UserDtoMapper.toUserDto(like.getUser());
			TwitDto twit =TwitDtoMapper.toTwitDto(like.getTwit(),reqUser);
			
			var likeDto=new LikeDto();
			likeDto.setId(like.getId());
			likeDto.setTwit(twit);
			likeDto.setUser(user);
			
			likeDtos.add(likeDto);
		}
		
		
		
		return likeDtos;
		
	}
}
