package com.org.simplelab.controllers;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.services.UserDB;
import com.org.simplelab.restcontrollers.rro.RRO;
import com.org.simplelab.security.SecurityUtils;
import com.org.simplelab.security.SimpleLabAuthentication;
import org.codehaus.jackson.map.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Map;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
@RequestMapping(path="/forgetPage")
public class ForgetController extends BaseController {

    public static final String FORGOT_USER_MAPPING =  "/fpFindUser";
    public static final String FORGOT_PASSWORD_MAPPING = "/fpChangePassword";

    @ResponseBody
    @PostMapping(FORGOT_USER_MAPPING)
    public RRO<String> fpGetUser (@RequestParam("userName") String username){
        RRO<String> rro = new RRO();
        User user = null;
        try{
            user = userDB.findUser(username);
        }catch (Exception e){

        }
        return rro;
    }

//    public class fpuserInput{
//        private String userinput;
//        private User user;
//    }
//
//    @PostMapping(FORGOT_PASSWORD_MAPPING)
//    public boolean fpCheckAnswer (@RequestBody fpuserInput checkPassword){
//        if (Arrays.equals(DBUtils.getHash(checkPassword.userinput),checkPassword.user.getAnswer())){
//            return true;
//        }
//        else
//            return false;
//    }
//
//
//
//    @PostMapping(FORGOT_PASSWORD_MAPPING)
//    public void changePassword (@RequestBody fpuserInput newPassword){
//
//        try{
//            newPassword.user.setPassword(newPassword.userinput);
//        }catch (Exception e){
//
//        }
//    }
}
