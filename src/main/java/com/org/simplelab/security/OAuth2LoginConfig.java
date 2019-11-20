package com.org.simplelab.security;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@Configuration
@EnableWebSecurity
@Order(10)
@ComponentScan({"com.org.simplelab.security"})
public class OAuth2LoginConfig extends WebSecurityConfigurerAdapter {

    /**
     * This ensures cookies are created for users.
     *
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http

                .authorizeRequests()
                /**.anyRequest().permitAll()
                .and().httpBasic();**/
                .antMatchers("/login", "/img/**", "/css/**", "/js/**", "/libs/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login(oauth2Login -> oauth2Login.userInfoEndpoint(
                        userInfoEndpoint -> userInfoEndpoint.userAuthoritiesMapper(
                                this.userAuthoritiesMapper()
                        )
                ).loginPage("/login"));
    }

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
                    System.out.println(userInfo.toString());
                } else if (OAuth2UserAuthority.class.isInstance(authority)) {
                    System.out.println("OAuth2 Token found");
                    OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority)authority;
                    Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();
                    System.out.println(userAttributes.toString());
                }
            });
            //TODO: make this update properly
            String[] test_auths = {SecurityUtils.ROLE_STUDENT};
            List<GrantedAuthority> l_auths = AuthorityUtils.createAuthorityList(test_auths);
            for (GrantedAuthority g: l_auths){
                auths.add(g);
            }
            return auths;
        };
    }

}
