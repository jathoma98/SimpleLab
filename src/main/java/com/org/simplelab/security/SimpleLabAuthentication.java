package com.org.simplelab.security;

import com.org.simplelab.database.entities.sql.User;
import com.org.simplelab.database.services.restservice.UserDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Authentication protocol for standard username and password sign in.
 */

@Component
public class SimpleLabAuthentication implements AuthenticationProvider {

    @Autowired
    UserDB userDB;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        if (userDB.authenticate(username, password) == UserDB.UserAuthenticationStatus.SUCCESSFUL){
            User user = userDB.findUser(username);
            String[] auths_raw = {SecurityUtils.getRoleFromDB(user.getRole())};
            List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(auths_raw);
            Authentication full_authentication = new UsernamePasswordAuthenticationToken(username, password, authorities);
            return full_authentication;
        } else {
            throw new BadCredentialsException("Authentication failed for username: " + username);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
