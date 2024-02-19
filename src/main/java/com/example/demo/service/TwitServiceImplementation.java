package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Request.TwitReplyRequest;
import com.example.demo.exception.TwitException;
import com.example.demo.exception.UserException;
import com.example.demo.model.Twit;
import com.example.demo.model.User;


@Service
public class TwitServiceImplementation implements TwitService {
	@Autowired
	private com.example.demo.repository.twitRepository twitRepository;

	@Override
	public Twit createTwit(Twit req, User user) throws UserException {
		
		Twit twit=new Twit();
		twit.setContent(req.getContent());
		twit.setCreatedAt(LocalDateTime.now());
		twit.setimage(req.getImage());
		twit.setUser(user);
		twit.setReply(false);
		twit.setTwit(true);
		twit.setVideo(req.getVideo());
		
		
		
		return twitRepository.save(twit);
	}

	@Override
	public List<Twit> findAllTwit() {
		
		return twitRepository.findAllByIsTwitTrueOrderByCreatedAtDesc();
	}

	@Override
	public Twit retwit(Long twitId, User user) throws UserException, TwitException {
		Twit twit=findById(twit);
		if(twit.getRetwitUser().Contains(user))
		{
			twit.getRetwitUser().remove(user);
		}
		else
		{
			twit.getRetwit().add(user);
		}
		return twitRepository.save(twit);
	}

	@Override
	public Twit findById(Long twitId) throws TwitException {
		Twit twit=twitRepository.findById(twitId)
				.orElseThrow()-> new TwitException("Twit not found with id"+twitId);
				return twit;
	}

	@Override
	public void deleteTwitById(Long twitId, Long userId) throws TwitException, UserException {
		Twit twit=findById(twitId);
		if(useId.equals(twit.getUser().getId()))
		{
			throw new UserException("You cant delet nothers twit");
		}
		twitRepository.deleteById(twit.getId());
		
	}

	@Override
	public Twit removeFromRetwit(Long twitId, User user) throws TwitException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Twit createReply(TwitReplyRequest req, User user) throws TwitException {
		
		Twit replyFor=findById(req.getTwitId);
		
		Twit twit=new Twit();
		twit.setContent(req.getContent());
		twit.setCreatedAt(LocalDateTime.now());
		twit.setimage(req.getImage());
		twit.setUser(user);
		twit.setReply(true);
		twit.setTwit(false);
		twit.setReplyFor(replyFor);
		
		Twit savedReply=twitRepository.save(twit);
		
		twit.getReplyTwits().add(savedReply);
		twitRepository.save(replyFor);
		
		return replyFor;
	}

	@Override
	public List<Twit> getUserTwit(User user) {
		
		return twitRepository.findByRetwitUserContainsOrUser_IdAndIsTwitTrueOrderByCreatedAtDesc(user, user.getId);
	}

	@Override
	public List<Twit> findByLikesContainsUser(User user) {
	
		return twitRepository.findByLikesUser_Id(user.getId());
	}

}
