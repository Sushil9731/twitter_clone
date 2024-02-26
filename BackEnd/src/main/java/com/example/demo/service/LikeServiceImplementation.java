package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.exception.LikeException;
import com.example.demo.exception.TwitException;
import com.example.demo.exception.UserException;
import com.example.demo.model.Like;
import com.example.demo.model.Twit;
import com.example.demo.model.User;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.TwitRepository;

@Service
public class LikeServiceImplementation implements LikesService {

	private LikeRepository likeRepository;
	private TwitService twitService;
	private TwitRepository twitRepository;
	
	public LikeServiceImplementation(
			LikeRepository likeRepository,
			TwitService twitService,
			TwitRepository twitRepository) {
		this.likeRepository=likeRepository;
		this.twitService=twitService;
		this.twitRepository=twitRepository;
	}

	@Override
	public Like likeTwit(Long twitId, User user) throws UserException, TwitException {
		
		Like isLikeExist=likeRepository.isLikeExist(user.getId(), twitId);
		
		if(isLikeExist!=null) {
			likeRepository.deleteById(isLikeExist.getId());
			return isLikeExist;
		}
		
		var twit=twitService.findById(twitId);
		var like=new Like();
		like.setTwit(twit);
		like.setUser(user);
		
		var savedLike=likeRepository.save(like);
		
		
		twit.getLikes().add(savedLike);
		twitRepository.save(twit);
		
		return savedLike;
	}

	@Override
	public Like unlikeTwit(Long twitId, User user) throws UserException, TwitException, LikeException {
		var like=likeRepository.findById(twitId).orElseThrow(()->new LikeException("Like Not Found"));
		
		if(like.getUser().getId().equals(user.getId())) {
			throw new UserException("somthing went wrong...");
		}
		
		likeRepository.deleteById(like.getId());
		return like;
	}

	@Override
	public List<Like> getAllLikes(Long twitId) throws TwitException {
		var twit=twitService.findById(twitId);
		
		List<Like> likes=likeRepository.findByTwitId(twit.getId());
		return likes;
	}

}
