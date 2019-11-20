package com.org.simplelab.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Map;

public class SimpleLabAuthoritiesExtractor implements AuthoritiesExtractor {

    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
        System.out.println(map.toString());
        return null;
    }

}
