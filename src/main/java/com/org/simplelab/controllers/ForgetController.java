package com.org.simplelab.controllers;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.sql.User;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restcontrollers.rro.RRO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Controller
@RequestMapping(ForgetController.BASE_MAPPING)
public class ForgetController extends BaseController {

    public static final String BASE_MAPPING = "/forgetPage";

    public static final String FORGOT_USER_MAPPING =  "/fpFindUser";
    public static final String FORGOT_PASSWORD_MAPPING = "/fpChangePassword";
    public static final String FORGOT_CHECK_ANSWER_MAPPING = "/getUserAnswer";
    public static final String USER_QUESTION_MAPPING =  "/fpGetQuestion";
    @GetMapping("")
    public String forgotPassword(HttpSession session) {
        return "forgotPassword";
    }

    @ResponseBody
    @PostMapping(FORGOT_USER_MAPPING)
    public RRO<User> fpGetUser (@RequestParam("username") String username){
        RRO<User> rro = new RRO();
        try{
            rro.setData(userDB.findUser(username));

        }catch (Exception e){
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
            return rro;
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        return rro;
    }

    @ResponseBody
    @PostMapping(USER_QUESTION_MAPPING)
    public RRO<String> fpGetQuestion (@RequestBody DTO.UserSearchDTO username){
        RRO<String> rro = new RRO();
        String Question = null;
        try{
            User user = userDB.findUser(username.getRegex());
            Question = user.getQuestion();

        }catch (Exception e){
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
            return rro;
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        rro.setData(Question);
        return rro;
    }


    @ResponseBody
    @PostMapping(FORGOT_CHECK_ANSWER_MAPPING)
    public RRO<String> fpCheckAnswer (@RequestBody DTO.fpUserInput checkPassword){
        User user = userDB.findUser(checkPassword.getUser().getUsername());
        byte[] temp = DBUtils.getHash(checkPassword.getAnswer());
        RRO<String> rro = new RRO();
        if (Arrays.equals(user.getAnswer(),temp)){
            rro.setSuccess(true);
            rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
            rro.setData("true");
            return rro;
        }
        else {
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
            return rro;
        }
    }

    @ResponseBody
    @PostMapping(FORGOT_PASSWORD_MAPPING)
    public RRO<String> changePassword (@RequestBody DTO.fpUserInput newPassword){
        RRO<String> rro = new RRO();
        try{
            rro.setSuccess(true);
            User user = userDB.findUser(newPassword.getUser().getUsername());
            user.setPassword(newPassword.getAnswer());
            userDB.update(user);
            rro.setData("true");
            return rro;
        }catch (Exception e){
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
            return rro;
        }
    }
}
