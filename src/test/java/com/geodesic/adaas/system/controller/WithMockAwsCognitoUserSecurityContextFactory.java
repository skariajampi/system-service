package com.geodesic.adaas.system.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithMockAwsCognitoUserSecurityContextFactory
        implements WithSecurityContextFactory<WithAwsCognitoUser> {

    @Override
    public SecurityContext createSecurityContext(final WithAwsCognitoUser withAwsCognitoUser) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", withAwsCognitoUser.username());
        attributes.put("cognito:username", withAwsCognitoUser.username());
        attributes.put("email", withAwsCognitoUser.email());
        attributes.put("cognito:groups", withAwsCognitoUser.authority());

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(withAwsCognitoUser.authority()));
        OAuth2User user = new DefaultOAuth2User(authorities, attributes, "sub");

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        OAuth2AuthenticationToken oAuth2AuthenticationToken =
                new OAuth2AuthenticationToken(user, authorities, "client-registration-id");
        oAuth2AuthenticationToken.setAuthenticated(withAwsCognitoUser.authenticated());
        context.setAuthentication(oAuth2AuthenticationToken);

        return context;
    }
}