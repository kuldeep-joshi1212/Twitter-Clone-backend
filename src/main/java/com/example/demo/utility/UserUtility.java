package com.example.demo.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserUtility {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    UserUtility(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }



    public Boolean checkForUser(String username){
        String checkUser = "select * from users where username = '"+username+"'";
        List<Map<String, Object>> foundUser = jdbcTemplate.queryForList(checkUser);

        if(foundUser == null || foundUser.size() == 0)
            return false;

        return true;
    }
}
