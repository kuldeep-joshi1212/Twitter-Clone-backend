package com.example.demo.serviceImpl;

import com.example.demo.entity.SignUpObject;
import com.example.demo.entity.TweetObject;
import com.example.demo.repository.HomeRepo;
import com.example.demo.service.HomeService;
import com.example.demo.entity.LoginObject;
import com.example.demo.entity.UserObject;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    HomeRepo homeRepo;

    @Override
    public Integer signUpUser(SignUpObject signUpObject) {
        if(signUpObject.getPassword().equals(signUpObject.getConfirm_password()))
            return homeRepo.singUpUser(signUpObject);

        return 0;
    }

    @Override
    public Integer authenticateUser(LoginObject loginObject) {

        if(loginObject == null || loginObject.getUsername().trim() == "" ){
            return 0;
        }

        return homeRepo.authenticateUser(loginObject);
    }

    @Override
    public Integer follow(Integer byUser, Integer toUser) {
        return homeRepo.follow(byUser, toUser);
    }

    @Override
    public Object showRelevantTweets(Integer userId) {
        return homeRepo.showTweets(userId);
    }

    @Override
    public Integer addTweet(TweetObject tweetObject){
        return homeRepo.addTweet(tweetObject);
    }


}
