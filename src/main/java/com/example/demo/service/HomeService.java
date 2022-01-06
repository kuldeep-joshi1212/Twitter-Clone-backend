package com.example.demo.service;

import com.example.demo.entity.LoginObject;
import com.example.demo.entity.SignUpObject;
import com.example.demo.entity.TweetObject;
import com.example.demo.entity.UserObject;
import com.google.gson.JsonArray;

public interface HomeService {

    public Integer signUpUser(SignUpObject signUpObject);

    public Integer authenticateUser(LoginObject loginObject);

    public Integer follow(Integer byUser, Integer toUser);

    public Object showRelevantTweets(Integer userId);

    public Integer addTweet(TweetObject tweetObject);
}
