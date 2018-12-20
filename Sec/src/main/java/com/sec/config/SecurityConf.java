package com.sec.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.thymeleaf.standard.expression.AndExpression;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConf extends WebSecurityConfigurerAdapter{
	
//	@Bean
//	public LayoutDialect layoutDialect() {
//	    return new LayoutDialect();
//	}
	
//	@Autowired
//	public void configureAuth(AuthenticationManagerBuilder auth) throws Exception{
//		auth
//			.inMemoryAuthentication()
//				.withUser("user1")
//				.password("{noop}pass")
//				.roles("USER")
//				
//			.and()
//				.withUser("admin")
//				.password("{noop}pass")
//				.roles("ADMIN")
//				
//			.and()
//				.withUser("user2")
//				.password("pass")
//				.roles("USER");
//	}
	
	@Override
	public void configure(HttpSecurity httpSec) throws Exception {
		httpSec
			.authorizeRequests()
//				.antMatchers(HttpMethod.GET,"/").permitAll()
//				.antMatchers("/delete").hasRole("ADMIN")
				.antMatchers( "/reg").permitAll()
				.antMatchers( "/registration").permitAll()
				.antMatchers( "/activation/**").permitAll()
				.antMatchers( "/login").permitAll()
				.antMatchers( "/db/**").permitAll()		//H2 console megjelenítéséhez
				.antMatchers( "/css/**").permitAll()
				.antMatchers("/admin/**").hasRole("ADMIN")
//				.antMatchers("/incidents").hasRole("ADMIN")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/login?logout")
				.permitAll();
		
//		H2 console megjelenítéséhez
		httpSec.headers().frameOptions().disable();
		httpSec.csrf().disable();
		}
	
}
