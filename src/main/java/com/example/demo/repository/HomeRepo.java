package com.example.demo.repository;

import com.example.demo.entity.LoginObject;
import com.example.demo.entity.SignUpObject;
import com.example.demo.entity.TweetObject;
import com.example.demo.entity.UserObject;
import com.example.demo.utility.PasswordUtility;
import com.example.demo.utility.UserUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class HomeRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    HomeRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    UserUtility userUtility;

    public Integer singUpUser(SignUpObject signUpObject) {
        Boolean doesUserAlreadyExist = userUtility.checkForUser(signUpObject.getUsername());

        if(doesUserAlreadyExist == false){
            return 0;
        }

        PasswordUtility passwordUtility = new PasswordUtility();

        String password = passwordUtility.encrypt(signUpObject.getPassword());

        String insertIntoUsers = "insert into users(name, username, password, confirm_password) values ('" + signUpObject.getName() + "', '" + signUpObject.getUsername() + "', '" + password + "'" +
                ", '"+password+"')";

        jdbcTemplate.update(insertIntoUsers);

        return 1;

    }

    public Integer authenticateUser(LoginObject loginObject){
        String getUserFromAuthenticate = "select * from users where username = '"+loginObject.getUsername()+"'";

        List<Map<String, Object>> getUser = jdbcTemplate.queryForList(getUserFromAuthenticate);

        if(getUser == null || getUser.size() == 0){
            return 0;
        }
        PasswordUtility passwordUtility = new PasswordUtility();
        String password = passwordUtility.decrypt(getUser.get(0).get("password").toString());

        if(password.equals(loginObject.getPassword())){
            return 1;
        }

        return 0;
    }

    public Integer findUserByUsername(LoginObject loginObject){
        String getUserFromAuthenticate = "select * from users where username = '"+loginObject.getUsername()+"'";

        List<Map<String, Object>> getUser = jdbcTemplate.queryForList(getUserFromAuthenticate);

        if(!getUser.isEmpty()){
            return 1;
        }

        return 0;

    }

    public Integer follow(Integer toUser, Integer byUser){
        String toUserFollowers = "select * from follow where id = "+toUser+"";
        String byUserFollowing = "select * from follow where id = "+byUser+"";



        List<Map<String, Object>> toUserList = jdbcTemplate.queryForList(toUserFollowers);
        List<Map<String, Object>> byUserList = jdbcTemplate.queryForList(byUserFollowing);

        String touserName = toUserList.get(0).get("username").toString();
        String byuserName = byUserList.get(0).get("username").toString();

        JsonElement toUserListJsonElement = new JsonParser().parse(toUserList.get(0).get("followers").toString());
        JsonElement byUserListJsonElement = new JsonParser().parse(byUserList.get(0).get("followings").toString());
        JsonObject toUserListJsonObject = toUserListJsonElement.getAsJsonObject();
        JsonObject byUserListJsonObject = byUserListJsonElement.getAsJsonObject();

        toUserListJsonObject.get("followers").getAsJsonArray().add(byuserName);
        byUserListJsonObject.get("followings").getAsJsonArray().add(touserName);

        String updateToUserFollowers = "update follow set followers = '{\"followers\": "+toUserListJsonObject.get("followers").getAsJsonArray()+"}' where id = "+toUser+"";
        String updateByUserFollowings = "update follow set followings = '{\"followings\": "+byUserListJsonObject.get("followings").getAsJsonArray()+"}' where id = "+byUser+"";

        jdbcTemplate.update(updateToUserFollowers);
        jdbcTemplate.update(updateByUserFollowings);


        return 1;

    }

    public Object showTweets(Integer userId){
        String userFollowers = "select * from follow where id = "+userId+"";

        List<Map<String, Object>> toUserList = jdbcTemplate.queryForList(userFollowers);
        JsonElement toUserListJsonElement = new JsonParser().parse(toUserList.get(0).get("followings").toString());
        JsonObject toUserListJsonObject = toUserListJsonElement.getAsJsonObject();
        JsonArray toUserJsonArray = toUserListJsonObject.get("followings").getAsJsonArray();

        List<String> namesOfFollowings = new ArrayList<>();
        String showTweetsForUser = "select * from tweet where username in (";
        for(int i = 0;i < toUserJsonArray.size();i++){
            if(i == toUserJsonArray.size() - 1){
                showTweetsForUser+= "'" + toUserJsonArray.get(i).getAsString() + "'" + "'js'" + ") order by createdAt desc";
                continue;
            }
            showTweetsForUser+= "'" + toUserJsonArray.get(i).getAsString() + "'" + ",";
        }

        List<Map<String, Object>> tweetsForUser = jdbcTemplate.queryForList(showTweetsForUser);


        Map<String, ArrayList<String>> tweets = new HashMap<>();
        for(int i = 0;i < tweetsForUser.size();i++){
            System.err.println(tweetsForUser.get(i).get("username").toString() + " " + tweetsForUser.get(i).get("tweet").toString());
            ArrayList<String> list=tweets.get(tweetsForUser.get(i).get("username").toString());
            if(list == null){
                list = new ArrayList<>();
                list.add(tweetsForUser.get(i).get("tweet").toString());
                tweets.put(tweetsForUser.get(i).get("username").toString(), list);
            }
            else{
                list.add(tweetsForUser.get(i).get("tweet").toString());
                tweets.put(tweetsForUser.get(i).get("username").toString(), list);
            }
        }

        return tweets;

    }

    public Integer addTweet(TweetObject tweetObject){

        if(tweetObject.getTweet().trim().length() > 140){
            return 0;
        }

        try{
            String addTweet = "insert into tweet values ('"+tweetObject.getUsername()+"', '"+tweetObject.getTweet()+"', now())";
            jdbcTemplate.update(addTweet);
            return 1;
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
}
