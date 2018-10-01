package com.aak.configuration;

import java.security.KeyPair;
import java.util.Collections;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * Created by ahmed on 21.5.18.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
	
	//for password grant needed
	@Autowired
    private AuthenticationManager authenticationManager;
	
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource oauthDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public JdbcClientDetailsService clientDetailsService() {
    	//TODO cutomize client detail
        return new JdbcClientDetailsService(oauthDataSource());
    }

    @Bean
    public TokenStore tokenStore() {
    	//TODO cutomize token store
    	return new JdbcTokenStore(oauthDataSource());
    }

    @Bean
    public ApprovalStore approvalStore() {
    	//TODO cutomize approval store
        return new JdbcApprovalStore(oauthDataSource());
    }
    
	@Bean
	public AuthorizationServerTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		// client detail service to get below setting, no need of global setting here
//		defaultTokenServices.setSupportRefreshToken(true);
//		defaultTokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
//		defaultTokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
		defaultTokenServices.setReuseRefreshToken(true);
		defaultTokenServices.setTokenEnhancer(JasonWebTokenConverter());
		defaultTokenServices.setClientDetailsService(clientDetailsService());
		defaultTokenServices.setAuthenticationManager(authenticationManager);
		return defaultTokenServices;
	}
    
    public static class CustomizedJwtAccessTokenConverter extends JwtAccessTokenConverter{
    	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    		DefaultOAuth2AccessToken beforeToken = new DefaultOAuth2AccessToken(accessToken);
    		Map<String, Object> additionalInfoMap = beforeToken.getAdditionalInformation();
    		//TODO enrich jwt claims
    		additionalInfoMap.put("iss", "issuer");
    		additionalInfoMap.put("sub", "subject");
    		additionalInfoMap.put("nbf", System.currentTimeMillis());
    		additionalInfoMap.put("iat", System.currentTimeMillis());
    		
    		OAuth2AccessToken result = super.enhance(beforeToken, authentication);
    		
    		//because all additional information already in encoded jwt, so exclude them from plain text part to minimize token size
    		DefaultOAuth2AccessToken afterToken = new DefaultOAuth2AccessToken(result);
    		afterToken.setAdditionalInformation(Collections.emptyMap());
    		
    		return afterToken;
    	}
    }
    
	@Bean
	public JwtAccessTokenConverter JasonWebTokenConverter() {
		JwtAccessTokenConverter converter = new CustomizedJwtAccessTokenConverter();
		converter.setSigningKey("abcd");
		// TODO customize token key with keypair instead of plain text key
//		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("mytest.jks"),
//				"mypass".toCharArray());
//		KeyPair keyPair = keyStoreKeyFactory.getKeyPair("alias");
//		converter.setKeyPair(keyPair);
		return converter;
	}
    
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
    	//customize authorization code service
        return new JdbcAuthorizationCodeServices(oauthDataSource());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
    }

    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
    	//Security the context path /oauth/token, /oauth/authorize is not in this method scope
    	oauthServer
        .tokenKeyAccess("permitAll()") //url:/oauth/token_key,exposes public key for token verification if using JWT tokens
        .checkTokenAccess("isAuthenticated()") //url:/oauth/check_token allow check token
        .allowFormAuthenticationForClients();// allow client_id/client_secret passing as URL parameter, otherwise only accept http user header authentication
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
        		.authenticationManager(authenticationManager)//for password grant needed
                .approvalStore(approvalStore())
                .authorizationCodeServices(authorizationCodeServices())
                .tokenStore(tokenStore())
                .tokenServices(tokenServices());
    }


}
