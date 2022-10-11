package com.geodesic.adaas.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

/**
 * The JWT Converter which is used by the JWT Configurer in spring httpsecurity. Note here that the
 * setAuthoritiesClaimName() is set to 'roles' and the setAuthorityPrefix() is set to 'ROLE_'. This
 * is so that the GrantedAuthority which is created when the JWT is parsed is recognised as a role.
 * This allows us to use the annotation <code>@PreAuthorize(hasRole('...'))</code> in the
 * controller. Without setting the 'ROLE_' as a prefix the @PreAuthorize annotation would not
 * recognise the <code>GrantedAuthority</code> objects as roles.
 *
 * <p>This class is used in the JwtSecurityConfiguration in this package but can be used separately
 * where this class isn't appropriate and another HttpSecurity configuration is needed.
 */
@Configuration
public class JwtAuthenticationConverterConfiguration {

    private static final String ROLES_CLAIM_NAME = "cognito:groups";
    private static final String ROLE_PREFIX = "";

    @Bean
    public JwtAuthenticationConverter getJwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
                new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(ROLES_CLAIM_NAME);
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(ROLE_PREFIX);
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
