package com.org.simplelab.security;

import com.org.simplelab.controllers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebSecurity
@Order(10)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan({"com.org.simplelab.security"})
public class OAuth2LoginConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http

                .authorizeRequests()
                //Allow any user to access static resources like images, CSS, javascript

                .antMatchers("/login",
                        ForgetController.BASE_MAPPING + "/**",
                        SignUpController.BASE_MAPPING + "/**",
                        "/img/**", "/css/**", "/js/**", "/libs/**")

                .permitAll()

                //only students can access /student pages
                .antMatchers(StudentController.BASE_MAPPING + "/**")
                .hasAuthority(SecurityUtils.AUTH_STUDENT)

                //only teachers can access /teacher pages
                .antMatchers(TeacherController.BASE_MAPPING + "/**")
                .hasAuthority(SecurityUtils.AUTH_TEACHER)

                //all other pages require logged in user
                .anyRequest().authenticated()

                //Sets up OAuth2 login at endpoint /login
                .and()
                .oauth2Login()
                .loginPage("/login")

                //Registers custom exception handler if user tries to access pages they
                //are not authorized to access.
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());
    }

    //this isnt used yet -- just for google login
    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper(){
        return (authorities) -> {
            Set<GrantedAuthority> auths = new HashSet<>();
            authorities.forEach( authority -> {
                if (OidcUserAuthority.class.isInstance(authority)){
                    System.out.println("oidc token");
                    OidcUserAuthority oidcUserAuthority = (OidcUserAuthority)authority;
                    OidcIdToken idToken = oidcUserAuthority.getIdToken();
                    OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();
                    System.out.println(idToken.toString());
                    //System.out.println(userInfo.toString());
                } else if (OAuth2UserAuthority.class.isInstance(authority)) {
                    System.out.println("OAuth2 Token found");
                    OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority)authority;
                    Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();
                    System.out.println(userAttributes.toString());
                }
            });
            //TODO: make this update properly
            String[] test_auths = {SecurityUtils.AUTH_STUDENT};
            List<GrantedAuthority> l_auths = AuthorityUtils.createAuthorityList(test_auths);
            for (GrantedAuthority g: l_auths){
                System.out.println("Authority: " + g.getAuthority());
                auths.add(g);
            }
            return auths;
        };
    }

    /**
     * Handles redirection of user if they try to access forbidden pages.
     * ex: student user tries to access teacher pages, they should
     * be redirected to some error page.
     */

    //just for registering with Spring
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new SimpleLabAccessDeniedHandler();
    }

    /**
     * handle() method handles user accessing pages they are not authorized to access
     */
    public class SimpleLabAccessDeniedHandler implements AccessDeniedHandler{

        @Override
        public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + LoginController.FORBIDDEN_MAPPING);
        }
    }

}
