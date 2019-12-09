package com.org.simplelab.controllers;

import com.org.simplelab.database.entities.sql.User;
import com.org.simplelab.restcontrollers.rro.RRO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(ForgetController.BASE_MAPPING)
public class ForgetController extends BaseController {

    public static final String BASE_MAPPING = "/forgetPage";

    public static final String FORGOT_USER_MAPPING =  "/fpFindUser";
    public static final String FORGOT_PASSWORD_MAPPING = "/fpChangePassword";
    public static final String FORGOT_CHECKANSWER_MAPPING = "/getUserAnswer";

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

        }
        return rro;
    }


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
