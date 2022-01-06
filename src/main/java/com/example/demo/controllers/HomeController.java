package com.example.demo.controllers;


import com.example.demo.entity.SignUpObject;
import com.example.demo.entity.TweetObject;
import com.example.demo.repository.HomeRepo;
import com.example.demo.service.HomeService;
import com.example.demo.entity.LoginObject;
import com.example.demo.entity.UserObject;
import com.example.demo.serviceImpl.HomeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

	@Autowired
	HomeService homeService;

	@PostMapping(value = "/signup", produces = "application/json", consumes = "application/json")
	public ResponseEntity signUpUser(@RequestBody SignUpObject signUpObject){

		Integer result = homeService.signUpUser(signUpObject);
		if(result == 1){
			return ResponseEntity.ok().body("Successfully Signed in");
		}
		return ResponseEntity.badRequest().body("Something went wrong");
	}

	@GetMapping(value = "/login", produces = "application/json", consumes = "application/json")
	public ResponseEntity loginUser(@RequestBody LoginObject loginObject){
		if(loginObject == null){
			return ResponseEntity.badRequest().body("username/password can not be empty");
		}
		Integer result = homeService.authenticateUser(loginObject);
		if(result != 0){
			return ResponseEntity.accepted().body("Valid Credentials");
		}

		return ResponseEntity.badRequest().body("Invalid username/password");

	}

	@PostMapping(value = "/follow/{toUserId}/{byUserId}", produces = "application/json", consumes = "application/json")
	public ResponseEntity followUser(@PathVariable("toUserId") Integer toUserObject, @PathVariable("byUserId") Integer byUserObject){

		if(byUserObject == null || toUserObject == null){
			return ResponseEntity.badRequest().body("Bad request");
		}

		Integer result = homeService.follow(toUserObject, byUserObject);
		if(result == 0){
			return ResponseEntity.badRequest().body("Bad Request");
		}

		return ResponseEntity.accepted().body("Accepted");
	}

	@GetMapping(value = "/showtweets/{userId}", produces = "application/json", consumes = "application/json")
	public Object relevantTweets(@PathVariable("userId") Integer userId){
		Object result = homeService.showRelevantTweets(userId);

		if(result == null){
//			return ResponseEntity.badRequest().body("Bad Request");
			return null;
		}

//		return ResponseEntity.accepted().body(result);
		return result;
	}

	@PostMapping(value = "/addtweet", produces = "application/json", consumes = "application/json")
	public ResponseEntity addTweet(@RequestBody TweetObject tweetObject){
		Integer result = homeService.addTweet(tweetObject);

		if(result == 0){
			return ResponseEntity.badRequest().body("Bad Request");
		}

		return ResponseEntity.accepted().body("Accepted");
	}
}