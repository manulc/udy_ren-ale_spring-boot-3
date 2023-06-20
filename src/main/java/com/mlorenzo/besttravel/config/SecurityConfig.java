package com.mlorenzo.besttravel.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Configuration
@PropertySource(value = "classpath:client-oauth2.properties")
public class SecurityConfig {
    private static final String[] PUBLIC_RESOURCES = {"/flights/**", "/hotels/**", "/.well-known/*", "/swagger-ui/**",
            "/v3/api-docs*/**"};
    private static final String[] USER_RESOURCES = {"/tours/**", "/tickets/**", "/reservations/**"};
    private static final String[] ADMIN_RESOURCES = {"/users/**", "/reports/**"};
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String LOGIN_PATH = "/login";

    private final UserDetailsService userDetailsService;
    private final Environment env;

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(final HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults()); // Enable OpenID Connect 1.0
        // Redirect to the login page when not authenticated from the authorization endpoint
        http.exceptionHandling(exception ->
                exception.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_PATH)));
        return http.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain (final HttpSecurity http) throws Exception {
        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtRoleConverter());
        http.authorizeHttpRequests()
                .requestMatchers(PUBLIC_RESOURCES).permitAll()
                .requestMatchers(USER_RESOURCES).authenticated()
                .requestMatchers(ADMIN_RESOURCES).hasRole(ADMIN_ROLE)
                .and()
                .formLogin()
                .and()
                .oauth2ResourceServer()
                .jwt()
                // Para que el servidor de recursos(Esta misma aplicación) tenga en cuenta los roles del usuario
                // autenticado en vez de los "scopes" solicitados para el control de acceso a los recursos
                .jwtAuthenticationConverter(jwtAuthenticationConverter);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(final PasswordEncoder bCryptPasswordEncoder) {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(final PasswordEncoder bCryptPasswordEncoder) {
        final RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(env.getProperty("app.client.id"))
                .clientSecret(bCryptPasswordEncoder.encode(env.getProperty("app.client.secret")))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(env.getProperty("app.client.redirect-url1"))
                .redirectUri(env.getProperty("app.client.redirect-url2"))
                .scope(env.getProperty("app.client.scope-read"))
                .scope(env.getProperty("app.client.scope-write"))
                .build();
        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        final KeyPair keyPair = generateRsaKey();
        final RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        final RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        final RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        final JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(final JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    // Por defecto, el token JWT sólo contiene los "scopes" solicitados y el servidor de recursos(Esta misma aplicación)
    // realiza el control de acceso a los recuersos teniendo en cuenta estos "scopes". Si queremo que ese control de
    // acceso a los recursos sea a través de los roles en lugar de los "scopes", tenemos que añadir los roles del
    // usuario autenticado al token JWT.
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            if (context.getTokenType() == OAuth2TokenType.ACCESS_TOKEN) {
                final Authentication principal = context.getPrincipal();
                final Set<String> authorities = principal.getAuthorities().stream()
                        // Versión simplificada de la expresión "grantedAuthority -> grantedAuthority.getAuthority()"
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());
                context.getClaims().claim("roles", authorities);
            }
        };
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException();
        }
        return keyPair;
    }

    // Sustituimos los "Granted Authorities" basados en "scopes"(Comportamiento por defecto) por aquellos basados en
    // los roles del usuario autenticado. Ésto lo hacemos para que el servidor de recursos(Esta misma aplicación)
    // tenga en cuenta estos roles en vez de los "scopes" para el control de acceso a los recursos
    private Converter<Jwt, Collection<GrantedAuthority>> jwtRoleConverter() {
        return jwt -> {
            @SuppressWarnings("unchecked")
            final List<String> roles = (ArrayList<String>) jwt.getClaims().get("roles");
            if (roles == null || roles.isEmpty()) {
                return new ArrayList<>();
            }
            // Versión simplificada de la expresión "role -> new SimpleGrantedAuthority(role)"
            return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        };
    }
}
