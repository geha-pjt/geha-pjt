package com.bit.geha.security;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.bit.geha.service.UserDetailService;


@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailService userDetailService;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	OAuth2ClientContext oauth2ClientContext;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/script/**", "image/**", "/fonts/**", "lib/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/hello")
			.authenticated()
			.antMatchers("/login").anonymous()
			.antMatchers("/**").permitAll()
			.and().exceptionHandling().accessDeniedPage("/").and()
			.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
		.formLogin()
			.loginPage("/login")
			.usernameParameter("id")
			.defaultSuccessUrl("/")
			.and()
		.logout()
			.logoutSuccessUrl("/").permitAll();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.eraseCredentials(false).userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	private Filter ssoFilter() {
		
		  OAuth2ClientAuthenticationProcessingFilter facebookFilter
		  = new OAuth2ClientAuthenticationProcessingFilter("/login/facebook");
		  
		  OAuth2RestTemplate facebookTemplate 
		  = new OAuth2RestTemplate(facebook(), oauth2ClientContext);
		  
		  facebookFilter.setRestTemplate(facebookTemplate);
		  
		  UserInfoTokenServices tokenServices 
		  = new UserInfoTokenServices(facebookResource().getUserInfoUri(), 
				  facebook().getClientId());
		  
		  tokenServices.setRestTemplate(facebookTemplate);
		  
		  facebookFilter.setTokenServices(tokenServices);
		  
		  facebookFilter.setAuthenticationSuccessHandler((request,response,authentication)
				  ->response.sendRedirect("/facebook/complete"));
		  
		  return facebookFilter;
		}
	
	@Bean
	@ConfigurationProperties("facebook.client")
	public AuthorizationCodeResourceDetails facebook() {
	return new AuthorizationCodeResourceDetails();
	}
	
	@Bean
	@ConfigurationProperties("facebook.resource")
	public ResourceServerProperties facebookResource() {
	return new ResourceServerProperties();
	}
	
	@Bean
	public FilterRegistrationBean oauth2ClientFilterRegistration(
			OAuth2ClientContextFilter filter) {
	  FilterRegistrationBean registration = new FilterRegistrationBean();
	  registration.setFilter(filter);
	  registration.setOrder(-100);
	  return registration;
	}

}
