package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void 
	configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("user").password("{noop}a").roles("USER").and()
		.withUser("admin").password("{noop}a").roles("ADMIN")
		;
	}
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
	    http.authorizeRequests()
	    //.antMatchers("/*").permitAll(); 
	    //.antMatchers("/auth/admin/*").hasAuthority("ADMIN")
	    .antMatchers("/bbs/article/*").hasAnyAuthority("ADMIN", "USER") 	    
	    ; 
	}

}