package com.example.demo.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LikeDto;
import com.example.demo.dto.mapper.LikeDtoMapper;
import com.example.demo.exception.LikeException;
import com.example.demo.exception.TwitException;
import com.example.demo.exception.UserException;
import com.example.demo.model.Like;
import com.example.demo.model.User;
import com.example.demo.service.LikesService;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name="Like-Unlike Twit")
public class LikeController {
	
	private UserService userService;
	private LikesService likeService;
	
	public LikeController(UserService userService,LikesService likeService) {
		this.userService=userService;
		this.likeService=likeService;
	}
	
	@PostMapping("/{twitId}/like")
	public ResponseEntity<LikeDto>likeTwit(
			@PathVariable Long twitId, 
			@RequestHeader("Authorization") String jwt) throws UserException, TwitException{
		
		var user=userService.findUserProfileByJwt(jwt);
		var like =likeService.likeTwit(twitId, user);
		
		var likeDto=LikeDtoMapper.toLikeDto(like,user);
		
		return new ResponseEntity<>(likeDto,HttpStatus.CREATED);
	}
	@DeleteMapping("/{twitId}/unlike")
	public ResponseEntity<LikeDto>unlikeTwit(
			@PathVariable Long twitId, 
			@RequestHeader("Authorization") String jwt) throws UserException, TwitException, LikeException{
		
		var user=userService.findUserProfileByJwt(jwt);
		var like =likeService.unlikeTwit(twitId, user);
		
		
		var likeDto=LikeDtoMapper.toLikeDto(like,user);
		return new ResponseEntity<>(likeDto,HttpStatus.CREATED);
	}
	
	@GetMapping("/twit/{twitId}")
	public ResponseEntity<List<LikeDto>>getAllLike(
			@PathVariable Long twitId,@RequestHeader("Authorization") String jwt) throws UserException, TwitException{
		var user=userService.findUserProfileByJwt(jwt);
		
		var likes =likeService.getAllLikes(twitId);
		
		var likeDtos=LikeDtoMapper.toLikeDtos(likes,user);
		
		return new ResponseEntity<>(likeDtos,HttpStatus.CREATED);
	}


}
