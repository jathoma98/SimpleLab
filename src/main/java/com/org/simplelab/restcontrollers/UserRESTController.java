package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.CourseDB;
import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.validators.CourseValidator;
import com.org.simplelab.database.validators.UserValidator;
import com.org.simplelab.database.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/rest")
    public class UserRESTController {

    @Autowired
    UserDB userDB;
    public static final String LOAD_USER_MAPPING = "/loadUserInfo";
    public static final String RESET_USER_MAPPING = "/restUserInfo";

    @GetMapping(LOAD_USER_MAPPING)
    public User getUserInfo(HttpSession session){
        String userId = "";
        try{
            userId = (String)session.getAttribute("user_id");
        } catch (Exception e){
            //redirect to login
        }
        User user = userDB.findUserById(userId);
        return user;
    }

//    @PostMapping(RESET_USER_MAPPING)
//    public User resetUserInfo(@RequestBody UserValidator userValidator
//                                           ,HttpSession session){
//
//    }

}
