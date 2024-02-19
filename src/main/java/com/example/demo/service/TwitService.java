package com.example.demo.service;

import java.util.List;

import com.example.demo.Request.TwitReplyRequest;
import com.example.demo.exception.TwitException;
import com.example.demo.exception.UserException;
import com.example.demo.model.Twit;
import com.example.demo.model.User;

public interface TwitService {

	
	public Twit createTwit(Twit req,User user)throws UserException;
	public List<Twit>findAllTwit();
	public Twit retwit(Long twitId, User user) throws UserException,TwitException;
	
	public Twit findById(Long twitId)throws TwitException;
	
	public void deleteTwitById(Long twitId,Long userId)throws TwitException,UserException;
	
	public Twit removeFromRetwit(Long twitId,User user)throws TwitException,UserException;
	
	public Twit createReply(TwitReplyRequest req,User user)throws TwitException;
	
	public List<Twit>getUserTwit(User user);
	
	public List<Twit>findByLikesContainsUser(User user);
}
