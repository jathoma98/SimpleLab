package com.org.simplelab.restcontrollers;

import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.validators.UserValidator;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restcontrollers.rro.RRO;
import com.org.simplelab.restcontrollers.rro.RRO_ACTION_TYPE;
import com.org.simplelab.restcontrollers.rro.RRO_MSG;
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
    public RRO<List<User>> searchUsers(@RequestBody DTO.UserSearchDTO toSearch){
        RRO<List<User>> rro = new RRO();
        String regex = toSearch.getRegex();
        //dont allow empty searches
        if (regex == null || regex.equals("")){
            rro.setSuccess(false);
            rro.setAction(RRO_ACTION_TYPE.NOTHING.name());
            return rro;
        }
        rro.setSuccess(true);
        rro.setAction(RRO_ACTION_TYPE.LOAD_DATA.name());
        rro.setData(userDB.searchUserWithKeyword(regex));
        return rro;
    }

    @GetMapping(LOAD_USER_MAPPING)
    public RRO<User> getUserInfo(HttpSession session){
        RRO<User> rro = new RRO();
        long userId = getUserIdFromSession(session);
        User user = userDB.findById(userId);
        if(user == null){
            rro.setSuccess(false);
            rro.setAction(RRO_ACTION_TYPE.PRINT_MSG.name());
            rro.setMsg(RRO_MSG.USER_NO_FOUND.getMsg());
        }
        rro.setSuccess(true);
        rro.setData(user);
        rro.setAction(RRO_ACTION_TYPE.LOAD_DATA.name());
        return rro;
    }

    @PostMapping(RESET_USER_MAPPING)
    public void resetUserInfo(@RequestBody User user
                                           ,HttpSession session) throws Exception {

        long userId = getUserIdFromSession(session);
        user.setId(userId);
        userDB.update(user);
    }

    public RRO<String> registerUser(UserValidator validator, HttpSession session){
        return super.addEntity(validator, userDB);
    }






}
