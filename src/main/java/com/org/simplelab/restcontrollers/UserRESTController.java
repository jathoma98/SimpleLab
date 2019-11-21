package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.CourseDB;
import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.validators.CourseValidator;
import com.org.simplelab.database.validators.UserValidator;
import com.org.simplelab.database.validators.Validator;
import com.org.simplelab.restcontrollers.dto.DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/rest")
    public class UserRESTController {

    @Autowired
    UserDB userDB;

    public static final String LOAD_USER_MAPPING = "/loadUserInfo";
    public static final String RESET_USER_MAPPING = "/restUserInfo";
    public static final String SEARCH_USER_MAPPING = "/searchUser";

    /**
     * Returns a list of Users with attributes that match a given string
     * @param toSearch - JSON object with format:
     *                 {
     *                      "regex": "the string to be searched for"
     *                 }
     *                 example:
     *                 {
     *                      "regex": "jacob"
     *                 }
     *                 would return all Users which have string "jacob" in at least one of fields
     *                 username, firstname, lastname, institution
     * @return A list of JSON Users with attributes: username, firstname, lastname, institution,
     * all other attributes null
     */
    @PostMapping(SEARCH_USER_MAPPING)
    public List<User> searchUsers(@RequestBody DTO.UserSearchDTO toSearch){
        String regex = toSearch.getRegex();
        //dont allow empty searches
        if (regex == null || regex.equals("")){
            return new ArrayList<>();
        }
        return userDB.searchByMatchingString(regex);
    }

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

    @PostMapping(RESET_USER_MAPPING)
    public void resetUserInfo(@RequestBody User user
                                           ,HttpSession session) {
        String userId = "";
        try {
            userId = (String) session.getAttribute("user_id");
        } catch (Exception e) {
            //redirect to login
        }
        userDB.updateUser(user);
    }
}
