package com.example.demo.Controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import javax.security.auth.login.CredentialException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Request.LoginRequest;
import com.example.demo.Request.LoginWithGoogleRequest;
import com.example.demo.Response.AuthResponse;
import com.example.demo.config.JwtProvider;
import com.example.demo.exception.UserException;
import com.example.demo.model.User;
import com.example.demo.model.Varification;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CustomeUserDetailsServiceImplementation;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Payload;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name="Authentication Management", description = "Endpoints for user authentication and authorization")
public class AuthController {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private JwtProvider jwtProvider;
	private CustomeUserDetailsServiceImplementation customUserDetails;
	
	
	 private static final String GOOGLE_CLIENT_ID = "YOUR_GOOGLE_CLIENT_ID";
	
	
	public AuthController(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			JwtProvider jwtProvider,
			CustomeUserDetailsServiceImplementation customUserDetails
			) {
		this.userRepository=userRepository;
		this.passwordEncoder=passwordEncoder;
		this.jwtProvider=jwtProvider;
		this.customUserDetails=customUserDetails;
		
	}
	
	  @PostMapping("/signin/google")
	    public ResponseEntity<AuthResponse> googleLogin(@RequestBody LoginWithGoogleRequest req) throws GeneralSecurityException, IOException {
	        
	        var user = validateGoogleIdToken(req);
	        
	        var email = user.getEmail();
	        var existingUser = userRepository.findByEmail(email);

	        if (existingUser == null) {
	           
	            var newUser = new User();
	            newUser.setEmail(email);
	            newUser.setImage(user.getImage());
	            newUser.setFullName(user.getFullName());
	            newUser.setLogin_with_google(true);
	            newUser.setPassword(user.getPassword());
	            newUser.setVerification(new Varification());
	            
	            userRepository.save(newUser);
	        }

//	        System.out.println("email ---- "+ existingUser.getEmail()+" jwt - ");
	      
	        Authentication authentication =  new UsernamePasswordAuthenticationToken(email, user.getPassword());
	   
	        
	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        var token = jwtProvider.generateToken(authentication);
	        
	        
	        var authResponse = new AuthResponse();
	        authResponse.setStatus(true);
	        authResponse.setJwt(token);
	        
//	        System.out.println("email ---- "+ existingUser.getEmail()+" jwt - "+token);

	        return new ResponseEntity<>(authResponse, HttpStatus.OK);
	    }

	
	private User validateGoogleIdToken(LoginWithGoogleRequest req) throws GeneralSecurityException, IOException {
		HttpTransport transport = new NetHttpTransport();
		var jsonFactory = JacksonFactory.getDefaultInstance();
        
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
			    .setAudience(Collections.singletonList(req.getClientId()))
			    .build();

			GoogleIdToken token = verifier.verify(req.getCredential());
			if (req.getCredential() != null) {
				
			  Payload payload = token.getPayload();
			  String userId = payload.getSubject();
			  
			  System.out.println("User ID: " + userId);

			  String email = payload.getEmail();
			  boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
			  var name = (String) payload.get("name");
			  var pictureUrl = (String) payload.get("picture");
			  var locale = (String) payload.get("locale");
			  var familyName = (String) payload.get("family_name");
			  var givenName = (String) payload.get("given_name");

			 var user=new User();
			 user.setImage(pictureUrl);
			 user.setEmail(email);
			 user.setFullName(name);
			 user.setPassword(userId);
			 
			 System.out.println("image url - -  "+pictureUrl);
			 
			 return user;

			} else {
			  throw new CredentialException("invalid id token...");
			}
			
			
	}

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@Valid @RequestBody User user) throws UserException{
		
		  	var email = user.getEmail();
	        var password = user.getPassword();
	        var fullName=user.getFullName();
	        var birthDate=user.getBirthDate();
	        
	        var isEmailExist=userRepository.findByEmail(email);

	        
	        if (isEmailExist!=null) {
	
	        	
	            throw new UserException("Email Is Already Used With Another Account");
	        }

	        // Create new user
			var createdUser= new User();
			createdUser.setEmail(email);
			createdUser.setFullName(fullName);
	        createdUser.setPassword(passwordEncoder.encode(password));
	        createdUser.setBirthDate(birthDate);
	        createdUser.setVerification(new Varification());
	        
	        var savedUser= userRepository.save(createdUser);
	        
	        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        
	        var token = jwtProvider.generateToken(authentication);

	        var authResponse= new AuthResponse(token,true);
			
	        return new ResponseEntity<>(authResponse,HttpStatus.OK);
		
	}
	
	@PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {

        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        
        System.out.println(username +" ----- "+password);
        
        var authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        
        var token = jwtProvider.generateToken(authentication);
        var authResponse= new AuthResponse();
		
		authResponse.setStatus(true);
		authResponse.setJwt(token);
		
        return new ResponseEntity<>(authResponse,HttpStatus.OK);
    }

	
	private Authentication authenticate(String username, String password) {
        var userDetails = customUserDetails.loadUserByUsername(username);
        
        System.out.println("sign in userDetails - "+userDetails);
        
        if (userDetails == null) {
        	System.out.println("sign in userDetails - null " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
        	System.out.println("sign in userDetails - password not match " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
