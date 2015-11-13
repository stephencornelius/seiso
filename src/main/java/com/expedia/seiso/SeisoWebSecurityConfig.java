/* 
 * Copyright 2013-2015 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.expedia.seiso;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import com.expedia.seiso.security.Roles;
import com.expedia.seiso.security.SeisoUserDetailsContextMapper;
import com.expedia.seiso.security.UserDetailsServiceImpl;

import lombok.val;

// See
// http://kielczewski.eu/2014/12/spring-boot-security-application/
// http://blog.springsource.org/2013/07/03/spring-security-java-config-preview-web-security/
// https://spring.io/guides/tutorials/spring-security-and-angular-js/
// https://spring.io/guides/gs/authenticating-ldap/
// http://stackoverflow.com/questions/8658584/spring-security-salt-for-custom-userdetails
// http://stackoverflow.com/questions/8521251/spring-securitypassword-encoding-in-db-and-in-applicationconext
// http://docs.spring.io/spring-security/site/docs/3.2.x/guides/helloworld.html

// Also for OAuth2
// http://aaronparecki.com/articles/2012/07/29/1/oauth2-simplified

/**
 * Java configuration for Seiso security.
 * 
 * @author Willie Wheeler
 */
@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SeisoWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected UserDetailsService userDetailsService() { return userDetailsServiceImpl(); }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			// TODO Would prefer to do this without sessions if possible. But see
			// https://spring.io/guides/tutorials/spring-security-and-angular-js/
			// http://docs.aws.amazon.com/ElasticLoadBalancing/latest/DeveloperGuide/elb-sticky-sessions.html
//			.sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//				.and()
			.authorizeRequests()
				
				.antMatchers(HttpMethod.GET, "/api/**").permitAll()
				.antMatchers(HttpMethod.POST, "/api/**").hasRole(Roles.USER)
				.antMatchers(HttpMethod.PUT, "/api/**").hasRole(Roles.USER)
				.antMatchers(HttpMethod.DELETE, "/api/**").hasRole(Roles.USER)
				
				// Admin console
				.antMatchers(HttpMethod.GET, "/admin").hasRole(Roles.ADMIN)
				.antMatchers(HttpMethod.GET, "/admin/**").hasRole(Roles.ADMIN)
				
				// Blacklist
				.anyRequest().denyAll()
//				.anyRequest().hasRole(Roles.USER)
				.and()
			.httpBasic()
				.authenticationEntryPoint(entryPoint())
				.and()
			.exceptionHandling()
				.authenticationEntryPoint(entryPoint())
				.and()
			// FIXME Enable. See https://spring.io/guides/tutorials/spring-security-and-angular-js/
			.csrf()
				.disable()
			;
		// @formatter:on
	}
	
	@Bean
	public UserDetailsService userDetailsServiceImpl() { return new UserDetailsServiceImpl(); }
	
	@Bean
	public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
	
	@Bean
	public BasicAuthenticationEntryPoint entryPoint() {
		val entry = new BasicAuthenticationEntryPoint();
		entry.setRealmName("Seiso");
		return entry;
	}
	
	@Bean
	public UserDetailsContextMapper userDetailsContextMapper() {
		return new SeisoUserDetailsContextMapper();
	}
	
	// @Bean
	// public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
	// 	   return new SeisoGrantedAuthoritiesMapper();
	// }
	
	@SuppressWarnings("unused")
	private void configureTestLdap(AuthenticationManagerBuilder auth) throws Exception {
		// @formatter:off
		auth
			.ldapAuthentication()
				.userDnPatterns("uid={0},ou=people")
				.groupSearchBase("ou=groups")
				.contextSource()
				.ldif("classpath:test-server.ldif");
		// @formatter:on
	}
	
}
