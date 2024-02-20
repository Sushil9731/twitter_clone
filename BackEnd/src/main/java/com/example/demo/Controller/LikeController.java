package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LikeDto;
import com.example.demo.dto.mapper.LikeDtoMapper;
import com.example.demo.exception.TwitException;
import com.example.demo.exception.UserException;
import com.example.demo.model.User;
import com.example.demo.service.LikeService;
import com.example.demo.services.UserService;

@RestController
@RequestMapping("/api")
public class LikeController {
@Autowired
	private UserService userService;



@Autowired
	private LikeService likeService;

@PostMapping("/twitId/likes")
public ResponseEntity<LikeDto>likeTwit(@PathVariable Long twitId,
		@RequestHeader("Authorization")String jwt)throws UserException,TwitException
{
	
	User user=userService.findUserProfileByJwt(jwt);
	var like=likeService.likeTwit(twitId, user);
	
	var likeDto=LikeDtoMapper.toLikeDto(like, user);
	
	return new ResponseEntity<>(likeDto,HttpStatus.CREATED);
}
	
@PostMapping("/twit/{twitId}")
public ResponseEntity<List<LikeDto>>getAllLikes(@PathVariable Long twitId,
		@RequestHeader("Authorization")String jwt)throws UserException,TwitException
{
	
	User user=userService.findUserProfileByJwt(jwt);
	var like=likeService.getAllLikes(twitId);
	
	var likeDtos=LikeDtoMapper.toLikeDtos(like, user);
	
	return new ResponseEntity<>(likeDtos,HttpStatus.CREATED);
}










}
