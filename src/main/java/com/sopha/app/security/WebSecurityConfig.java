package com.sopha.app.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import com.sopha.app.service.UserDetailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailService();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.authorizeRequests()
	        .antMatchers("/admin").hasAuthority("ROLE_ADMIN")  
	        .antMatchers("/creer_Produit").hasAuthority("ROLE_ADMIN")
	        .antMatchers("/ajouterProduit").hasAuthority("ROLE_ADMIN")
	        .antMatchers("/modifierProduit").hasAuthority("ROLE_ADMIN")
	        .antMatchers("/toutesLesCommandes").hasAuthority("ROLE_ADMIN")
	        .antMatchers("/user").hasAuthority("ROLE_USER")
	        .antMatchers("/panier").hasAuthority("ROLE_USER")
	        .antMatchers("/produits").hasAuthority("ROLE_USER")
	        .antMatchers("/passerCommande").hasAuthority("ROLE_USER")
	        .antMatchers("/mesCommandes").hasAuthority("ROLE_USER")
	        .antMatchers("/notifications").hasAuthority("ROLE_USER")
	        .antMatchers("/traiter_connexion").authenticated()
	        .anyRequest().permitAll()
	        .and()
	        .formLogin()
	            .loginPage("/connexion") // Utilisez l'URL de votre page de connexion
	            .loginProcessingUrl("/traiter_connexion")
	            //.defaultSuccessUrl("/tableau_de_bord", true)
	            .failureUrl("/connexion?error")
	            .successHandler(this::loginSuccessHandler)
	            .usernameParameter("email")
	            .passwordParameter("mot_de_passe")
	            .permitAll()
	        .and()
	        //pour logout 
	        .headers()
               .cacheControl().disable() // Premier niveau pour désactiver la mise en cache
               .addHeaderWriter(new StaticHeadersWriter("Cache-Control", "no-store, no-cache, must-revalidate"))
               .addHeaderWriter(new StaticHeadersWriter("Pragma", "no-cache"))
               .addHeaderWriter(new StaticHeadersWriter("Expires", "0")) //  le cache des navigateurs est géré correctement
	        .and()
	        .logout()
	            .logoutSuccessHandler(this::logoutSuccessHandler) // Utilisez un gestionnaire de succès personnalisé
                .logoutUrl("/deconnexion") // Définir l'URL de déconnexion personnalisée
                .logoutSuccessUrl("/connexion") // Définir l'URL de redirection après déconnexion
                .invalidateHttpSession(true) // Invalide la session
                .clearAuthentication(true) // Efface l'authentification
                .deleteCookies("JSESSIONID") // Supprime les cookies de session
               .permitAll()     
	        .and()
	        .headers().cacheControl().disable()
	        .and()
	        .csrf().disable();
	}

	private void loginSuccessHandler(
			  HttpServletRequest request,
			  HttpServletResponse response,
			  Authentication authentication) throws IOException {

			  String redirectURL = "/";
			  Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

			  for (GrantedAuthority authority : authorities) {
			      String role = authority.getAuthority();
			      if ("ROLE_ADMIN".equals(role)) {
			          redirectURL = "/admin";
			          break;
			      } else if ("ROLE_USER".equals(role)) {
			          redirectURL = "/user";
			          break;
			      }
			      
			  }

			  response.sendRedirect(request.getContextPath() + redirectURL);
			}

	private void logoutSuccessHandler(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        Authentication authentication) throws IOException {
	    
	    response.sendRedirect(request.getContextPath() + "/connexion");
	}
	
}
