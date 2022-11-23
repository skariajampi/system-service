package com.geodesic.adaas.system.controller;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = WithMockAwsCognitoUserSecurityContextFactory.class)
public @interface WithAwsCognitoUser {
  String username() default "";

  String authority() default "";

  String email() default "test@test.com";

  boolean authenticated() default true;
}
