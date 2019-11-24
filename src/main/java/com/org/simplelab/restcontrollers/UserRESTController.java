package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.BaseController;
import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.restcontrollers.dto.DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(UserRESTController.BASE_MAPPING)
public class UserRESTController extends BaseController {

    public static final String BASE_MAPPING = "/user/rest";

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
        //TODO: reimplement this
        return userDB.searchUserWithKeyword(regex);
//        return null;
    }

    @GetMapping(LOAD_USER_MAPPING)
    public User getUserInfo(HttpSession session){
        long userId = -1;
        try{
            userId = getUserIdFromSession(session);
        } catch (Exception e){
            //redirect to login
        }
        User user = userDB.findUserById(userId);
        return user;
    }

    @PostMapping(RESET_USER_MAPPING)
    public void resetUserInfo(@RequestBody User user
                                           ,HttpSession session) {
        long userId = getUserIdFromSession(session);
        user.setId(userId);
        userDB.updateUser(user);
    }
}
