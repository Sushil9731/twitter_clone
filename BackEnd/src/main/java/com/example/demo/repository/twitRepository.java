package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Twit;
import com.example.demo.model.User;

public interface twitRepository extends JpaRepository<Twit, Long>{
	
List<Twit> findAllByIsTwitTrueOrderByCreatedAtDesc();

List<Twit> findByRetwitUserContainsOrUser_IdAndIsTwitTrueOrderByCreatedAtDesc(User user,Long userId);

List<Twit> findByLikesContainingOrderByCreatedAtDesc(User user);

@Query("SELECT t FROM Twit t JOIN t.likes l WHERE l.user.id=:userId")
List<Twit> findByLikesUser_Id(Long userId);
}
