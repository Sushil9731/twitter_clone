package com.example.demo.service;

import java.util.List;

import com.example.demo.exception.TwitException;
import com.example.demo.exception.UserException;
import com.example.demo.model.Like;
import com.example.demo.model.User;

public interface LikeService {

	public Like likeTwit(Long twitId, User user)throws UserException,TwitException;
	
	public List<Like>getAllLikes(Long twitId)throws TwitException;
	
	
}
