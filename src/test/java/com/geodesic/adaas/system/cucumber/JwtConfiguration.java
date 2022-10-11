package com.geodesic.adaas.system.cucumber;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

@TestConfiguration
public class JwtConfiguration {

    /**
     * The KeyPair is created as a bean so that it is available within the cucumber code to generate
     * and sign JWTs for the requests being created.
     */
    @Bean
    KeyPair jwtKeypair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * This is an implementation of a JwtDecoder available in spring security. This allows the
     * signature to be verified in the security mechanism without calling out to get the key (OIDC
     * provider).
     */
    @Bean
    JwtDecoder jwtDecoder(KeyPair keyPair) {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) keyPair.getPublic()).build();
    }
}
