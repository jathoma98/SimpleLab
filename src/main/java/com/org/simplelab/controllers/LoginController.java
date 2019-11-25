package com.org.simplelab.controllers;

import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.services.UserDB;
import com.org.simplelab.security.SecurityUtils;
import com.org.simplelab.security.SimpleLabAuthentication;
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
import java.util.Map;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * Controllers for login and signup pages
 */

//TODO: make sure user info is taken from security context, not session.

@Controller
@RequestMapping(path="/")
public class LoginController{

    @Autowired
    UserDB userDB;

    @Autowired
    SimpleLabAuthentication authManager;

    public static final String FORBIDDEN_MAPPING =  "/forbidden";

    @GetMapping("/login")
    public String loginGet(){
        return "login";
    }

    /**
     * Processes user data submission in /login and adds
     * user data to current session.
     *
     * @param username -username from form submission in login.html
     * @param password -password from form submission in login.html
     * @param session -current user session
     * @return JSON response with format:
     *          { success: "true" } if authentication is successful
     *          { success: "false" } otherwise
     */
    @ResponseBody
    @PostMapping("/login")
    public Map<String, String> login(@RequestParam("userName") String username,
                                     @RequestParam("password") String password,
                                     HttpSession session) {
        RequestResponse resp = new RequestResponse();
        resp.setSuccess(false);

        //setup Spring security context
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = null;
        try{
            auth = authManager.authenticate(token);
        } catch (BadCredentialsException e) {
            return resp.map();
        }
        if (auth == null){
            return resp.map();
        }
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

        User user = userDB.findUser(username);
        session.setAttribute("username", username);
        session.setAttribute("user_id", user.getId());
        session.setAttribute("identity", user.getRole());
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

        /**if (userDB.authenticate(username, password) == UserDB.UserAuthenticationStatus.SUCCESSFUL) {
            User user = userDB.findUser(username);

            //setup Spring security context
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationManager authenticationManager

            session.setAttribute("username", username);
            session.setAttribute("user_id", user.getId());
            session.setAttribute("identity", user.getRole());
            resp.setSuccess(true);
            return resp.map();
        }
        else{
            resp.setSuccess(false);
            return resp.map();
        }**/

        resp.setSuccess(true);
        return resp.map();
    }


    /**
     * Redirect user page base on they role in the current session.
     *
     * @param session -current user session
     * @return ModelAndView redirect path for different role of user
    @RequestMapping(value = "/role", method = RequestMethod.GET)
     */
    @GetMapping("/role")
    public ModelAndView rolePageRedirect(HttpSession session) {
        String username = SecurityUtils.getAuthenticatedUsername();
        String identity = (String) session.getAttribute("identity");
        if (username != null && identity != null) {
            if (identity.equals("teacher") ) {
                return new ModelAndView("redirect:/teacher");
            } else if (identity.equals("student"))
                return new ModelAndView("redirect:/student");
        }
        return new ModelAndView("redirect:/");
    }

    @GetMapping(FORBIDDEN_MAPPING)
    public String forbidden(HttpSession session){
        return "forbidden";
    }


    @GetMapping("/forgotpassword")
    public String forgotPassword(HttpSession session) {

        return "forgotPassword";
    }
}