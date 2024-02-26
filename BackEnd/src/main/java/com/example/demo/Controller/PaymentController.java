package com.example.demo.Controller;


import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Response.PaymentLinkResponse;
import com.example.demo.exception.UserException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api/plan")
public class PaymentController {
	
	  @Value("${razorpay.api.key}")
	    private String razorpayApiKey;

	    @Value("${razorpay.api.secret}")
	    private String razorpayApiSecret;
	    
	    @Autowired
	    private UserService userService;
	    
	    @Autowired
	    private UserRepository userRepository;
	
	    @PostMapping("/subscribe/{planType}")
	    public ResponseEntity<PaymentLinkResponse> createSubscription(@PathVariable String planType,
	    		@RequestHeader("Authorization") String jwt)
	    		throws RazorpayException, UserException {
	    	
	    	var razorpay = new RazorpayClient(razorpayApiKey, razorpayApiSecret);
	    	
	        try {
	        	
	        	var user=userService.findUserProfileByJwt(jwt);
	        	
	        	var paymentLinkRequest = new JSONObject();
	        	paymentLinkRequest.put("currency","INR");
	        	paymentLinkRequest.put("description","Twitter Verification");
	        	
	        	var customer = new JSONObject();
	        	customer.put("name",user.getFullName());
	        	customer.put("email",user.getEmail());
	        	paymentLinkRequest.put("customer",customer);
	        	
	        	var notify = new JSONObject();
	        	notify.put("sms",true);
	        	notify.put("email",true);
	        	paymentLinkRequest.put("notify",notify);
	        	paymentLinkRequest.put("reminder_enable",true);
	        	
	        	var notes = new JSONObject();
	        	
	        	notes.put("user_id", user.getId().toString());
	        	paymentLinkRequest.put("notes",notes);
	        	
	        	paymentLinkRequest.put("callback_url","http://localhost:3000/verified");
	        	paymentLinkRequest.put("callback_method","get");
	        	
	         	if(planType.equals("monthly")) {
	        		paymentLinkRequest.put("amount",650*100);
	        		notes.put("plan","monthly");
	        	}
	         	else {
	         		paymentLinkRequest.put("amount",6800*100);
	        		notes.put("plan","monthly");
	         	}
	        	              
	        	var payment = razorpay.paymentLink.create(paymentLinkRequest);
	        	
	        	System.out.println("plan : yearly"+payment);
	        	
			    var paymentLinkId = payment.get("id");
			    var paymentLinkUrl = payment.get("short_url");
			      
			    PaymentLinkResponse res=new PaymentLinkResponse();
			    res.setPaymentLink(paymentLinkUrl);
	        	
	            return new ResponseEntity<>(res,HttpStatus.CREATED);
	            
	        } catch (RazorpayException e) {
	            throw new RazorpayException(e.getMessage());
	        }
	    }
	    
	    
	    @GetMapping("/{paymentLinkId}")
	    public ResponseEntity<String> fetchPaymetn(@PathVariable String paymentLinkId) throws RazorpayException {
	    	
	    	var razorpay = new RazorpayClient(razorpayApiKey, razorpayApiSecret);

	        try {

	        	var payment = razorpay.paymentLink.fetch(paymentLinkId);
	        	
	            var customerJsonString = payment.get("customer").toString();

	            var customerObject = new JSONObject(customerJsonString);

	            var email = customerObject.getString("email");
	            
	            var user =userRepository.findByEmail(email);
	            
	            var notesJsonString=payment.get("notes").toString();
	            
	            var notesObject=new JSONObject(notesJsonString);
	            
	            var plan=notesObject.getString("plan");
	            
	            if(payment.get("status").equals("paid")) {
	            	user.getVerification().setStartedAt(LocalDateTime.now());
	            	user.getVerification().setPlanType(plan);

	                if (plan.equals("yearly")) {
	                    var endsAt = user.getVerification().getStartedAt().plusYears(1);
	                    user.getVerification().setEndsAt(endsAt);
	                }
	                else if (plan.equals("monthly")) {
	                    var endsAt = user.getVerification().getStartedAt().plusMonths(1);
	                    user.getVerification().setEndsAt(endsAt);
	                }
	                
	                userRepository.save(user);
	                
	            }
	        	
	            return new ResponseEntity<>(email,HttpStatus.CREATED);
	            
	        } catch (RazorpayException e) {
	            throw new RazorpayException(e.getMessage());
	        }
	    }
	    
	    
}
