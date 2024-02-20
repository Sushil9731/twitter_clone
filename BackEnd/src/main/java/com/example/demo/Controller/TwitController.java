package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Request.TwitReplyRequest;
import com.example.demo.Response.ApiResponse;
import com.example.demo.dto.TwitDto;
import com.example.demo.dto.mapper.TwitDtoMapper;
import com.example.demo.exception.TwitException;
import com.example.demo.exception.UserException;
import com.example.demo.model.Twit;
import com.example.demo.model.User;
import com.example.demo.service.TwitService;
import com.example.demo.services.UserService;

@RestController
@RequestMapping("/api/twits")
public class TwitController {

	@Autowired
	private TwitService twitService;
	
	@Autowired
	private UserService userService;
	@PostMapping("/create")
	public ResponseEntity<TwitDto>createTwit(@RequestBody Twit req,@RequestHeader("Authorization")String jwt)throws UserException,TwitException 
	{
		User user=userService.findUserProfileByJwt(jwt);
		var twit=twitService.createTwit(req, user);
		var twitDto=TwitDtoMapper.toTwitDto(twit, user);
		
		
		return new ResponseEntity<>(twitDto,HttpStatus.CREATED);
	}
	@PostMapping("/reply")
	public ResponseEntity<TwitDto>replyTwit(@RequestBody TwitReplyRequest req,@RequestHeader("Authorization")String jwt)throws UserException,TwitException 
	{
		User user=userService.findUserProfileByJwt(jwt);
		var twit=twitService.createReply(req, user);
		var twitDto=TwitDtoMapper.toTwitDto(twit, user);
		
		
		return new ResponseEntity<>(twitDto,HttpStatus.CREATED);
	}
	@PutMapping("/{twitId}/retwit")
	public ResponseEntity<TwitDto>retwit(@PathVariable Long twitId,@RequestHeader("Authorization")String jwt)throws UserException,TwitException 
	{
		User user=userService.findUserProfileByJwt(jwt);
		var twit=twitService.retwit(twitId,user);
		var twitDto=TwitDtoMapper.toTwitDto(twit, user);
		
		
		return new ResponseEntity<>(twitDto,HttpStatus.OK);
	}
	@GetMapping("/{twitId}")
	public ResponseEntity<TwitDto>findTwitById(@PathVariable Long twitId,@RequestHeader("Authorization")String jwt)throws UserException,TwitException 
	{
		User user=userService.findUserProfileByJwt(jwt);
		var twit=twitService.findById(twitId);
		var twitDto=TwitDtoMapper.toTwitDto(twit, user);
		
		
		return new ResponseEntity<>(twitDto,HttpStatus.OK);
	}
	@DeleteMapping("/{twitId}")
	public ResponseEntity<ApiResponse>deleteTwit(@PathVariable Long twitId,@RequestHeader("Authorization")String jwt)throws UserException,TwitException 
	{
		User user=userService.findUserProfileByJwt(jwt);
		Twit twit=twitService.deleteTwitById(twitId,user.getId());
		var res=new ApiResponse();
		res.setMessage("Twit deleted successfully");
		res.setStatus(true);
		
		
		return new ResponseEntity<>(res,HttpStatus.OK);
		
	}
	
	@GetMapping("/")
	public ResponseEntity<List<TwitDto>>getAllTwits(@RequestHeader("Authorization")String jwt)throws UserException,TwitException 
	{
		User user=userService.findUserProfileByJwt(jwt);
		var twits=twitService.findAllTwit();
		var twitDtos=TwitDtoMapper.toTwitDtos(twits, user);
		
		
		return new ResponseEntity<>(twitDtos,HttpStatus.OK);
	}
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<TwitDto>>getUsersAllTwits(@PathVariable Long userId,@RequestHeader("Authorization")String jwt)throws UserException,TwitException 
	{
		User user=userService.findUserProfileByJwt(jwt);
		var twits=twitService.getUserTwit(user);
		
		var twitDtos=TwitDtoMapper.toTwitDtos(twits, user);
		
		
		return new ResponseEntity<>(twitDtos,HttpStatus.OK);
	}
	@GetMapping("/user/{userId}/likes")
	public ResponseEntity<List<TwitDto>>findTwitByLikesContainsUser(@PathVariable Long userId,@RequestHeader("Authorization")String jwt)throws UserException,TwitException 
	{
		User user=userService.findUserProfileByJwt(jwt);
		var twits=twitService.findByLikesContainsUser(user);
		
		var twitDtos=TwitDtoMapper.toTwitDtos(twits, user);
		
		
		return new ResponseEntity<>(twitDtos,HttpStatus.OK);
	}
	
	
	
	
	
	
	
}
