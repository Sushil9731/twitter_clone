package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data // lombok use so constructor , toString and getter,setter inside this data
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String fullName;
	private String location;
	private String website;
	private String birthDate;
	private String email;
	private String password;
	private String mobile; //+91
	private String image;
	private String backgroundimage;
	private String bio;
	private boolean req_user;
	
	@JsonIgnore
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL)
	private List<Twit> twit=new ArrayList<>();
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL)
	private List<Like> likes=new ArrayList<>();
	
	@Embedded
	private Varification verification;
	
	@JsonIgnore
	@ManyToMany
	private List<User>followers=new ArrayList<>();
	
	@JsonIgnore
	@ManyToMany
	private List<User>followings=new ArrayList<>();
	
}
