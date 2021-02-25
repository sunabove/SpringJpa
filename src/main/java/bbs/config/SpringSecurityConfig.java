package bbs.config;

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
		.withUser("user").password("{noop}user").roles("USER").and()
		.withUser("admin").password("{noop}admin").roles("ADMIN")
		;
	}
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
	    http.authorizeRequests()
	    .antMatchers("/*").permitAll(); 
	    //.antMatchers("/auth/admin/*").hasAuthority("ADMIN")
	    //.antMatchers("/auth/*").hasAnyAuthority("ADMIN", "USER") 	    
	    ; 
	}

}