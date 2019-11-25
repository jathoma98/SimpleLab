package com.org.simplelab.restcontrollers;

import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.validators.UserValidator;
import com.org.simplelab.restcontrollers.dto.DTO;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(UserRESTController.BASE_MAPPING)
public class UserRESTController extends BaseRESTController<User> {

    public static final String BASE_MAPPING = "/user/rest";

    public static final String LOAD_USER_MAPPING = "/loadUserInfo";
    public static final String RESET_USER_MAPPING = "/restUserInfo";
    public static final String SEARCH_USER_MAPPING = "/searchUser";
    public static final String FORGOT_PASSWORD_MAPPING = "/fpChangePassword";

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
        return userDB.searchUserWithKeyword(regex);
    }

    @GetMapping(LOAD_USER_MAPPING)
    public User getUserInfo(HttpSession session){
        long userId = getUserIdFromSession(session);
        User user = userDB.findById(userId);
        return user;
    }

    @PostMapping(RESET_USER_MAPPING)
    public void resetUserInfo(@RequestBody User user
                                           ,HttpSession session) {
        long userId = -1;
        try {
            userId = (long) session.getAttribute("user_id");
        } catch (Exception e) {

        }
        long userId = getUserIdFromSession(session);
        user.setId(userId);
        userDB.update(user);
    }

    public Map registerUser(UserValidator validator, HttpSession session){
        return super.addEntity(validator, userDB);
    }



    public class userNewPassword{
        private String password;
        private User user;
    }

    @PostMapping(FORGOT_PASSWORD_MAPPING)
    public void changepassword (@RequestBody userNewPassword newPassword){
        try{
            newPassword.user.setPassword(newPassword.password);
        }catch (Exception e){

        }

    }


}
