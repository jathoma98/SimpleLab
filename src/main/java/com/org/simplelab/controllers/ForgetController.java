package com.org.simplelab.controllers;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.services.UserDB;
import com.org.simplelab.restcontrollers.dto.DTO;
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
    public static final String FORGOT_CHECKANSWER_MAPPING = "/getUserAnswer";

    @ResponseBody
    @PostMapping(FORGOT_USER_MAPPING)
    public RRO<User> fpGetUser (@RequestParam("username") String username){
        RRO<User> rro = new RRO();
        try{
            rro.setData(userDB.findUser(username));
        }catch (Exception e){

        }
        return rro;
    }

//
//    @PostMapping(FORGOT_CHECKANSWER_MAPPING)
//    public boolean fpCheckAnswer (@RequestBody DTO.fpUserInput checkPassword){
//        byte[] temp = DBUtils.getHash(checkPassword.getUserInput());
//        if (Arrays.equals(temp,checkPassword.getUser().getAnswer())){
//            return true;
//        }
//        else
//            return false;
//    }
//
//    @PostMapping(FORGOT_PASSWORD_MAPPING)
//    public void changePassword (@RequestBody DTO.fpUserInput newPassword){
//        try{
//            newPassword.getUser().setPassword(newPassword.getUserInput());
//        }catch (Exception e){
//
//        }
//    }
}
