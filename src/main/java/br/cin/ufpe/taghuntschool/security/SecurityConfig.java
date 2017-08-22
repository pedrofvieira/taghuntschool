
/******* 
    Taghunt School - uma plataforma para criação de jogos como ferramenta de apoio à educação
    Copyright (C) 2017 Pedro Fernandes Vieira
 
    Este arquivo é parte da Taghunt School
 
    Taghunt School é um software livre;  você pode redistribuí-lo e/ou modificá-lo sob os
    termos da Licença Pública Geral GNU/GPL como publicada pela Free Software Foundation,
    na versão 3 da Licença, ou (a seu critério) qualquer versão mais recente.
 
    Este programa é distribuído na expectativa de ser útil, mas SEM QUALQUER GARANTIA; sem
    mesmo a garantia implícita de COMERCIALIZAÇÃO ou de ADEQUAÇÃO A QUALQUER APLICAÇÃO 
    EM PARTICULAR. Veja Licença Pública Geral GNU/GPL em português para maiores detalhes.
 
    Você deve ter recebido uma cópia da Licença Pública Geral GNU/GPL, sob o título “LICENSE”,
    junto com Taghunt School. Se não, acesse <http://www.gnu.org/licenses/>. 

    ---
	Taghunt School - Platform for creating games as a tool to support education
    Copyright (C) 2017 Pedro Fernandes Vieira
	
    This file is part of Taghunt School.

    Taghunt School is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Taghunt School is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Taghunt School.  If not, see <http://www.gnu.org/licenses/>.

*******/  

package br.cin.ufpe.taghuntschool.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import br.cin.ufpe.taghuntschool.security.authentication.AutenticacaoTaghuntSchool;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages= 
		{"br.cin.ufpe.taghuntschool.security.authentication", 
		"br.cin.ufpe.taghuntschool.service",
		"br.cin.ufpe.taghuntschool.dao"
		})
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AutenticacaoTaghuntSchool autenticacaoTaghuntSchool;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(autenticacaoTaghuntSchool);
	}
	
	/*@Override
	public void setAuthenticationConfiguration(
			AuthenticationConfiguration authenticationConfiguration) {
		super.setAuthenticationConfiguration(authenticationConfiguration);
	}
	
	@Override
	public void setObjectPostProcessor(
			ObjectPostProcessor<Object> objectPostProcessor) {
		super.setObjectPostProcessor(objectPostProcessor);
	}*/
	
	protected void configure(HttpSecurity http) throws Exception {
		/*http.csrf().disable();
		http.exceptionHandling().accessDeniedPage("/acessoNegado.xhtml")
			.and()
			.authorizeRequests()
			.antMatchers("/administracao/**").hasRole("ADMIN")
			.antMatchers("/javax.faces.resource/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.logout().logoutSuccessUrl("/login.xhtml?logout")
			.permitAll()
			.and()
			.formLogin().loginPage("/login.xhtml")
			.failureUrl("/login.xhtml?erro")
			.permitAll();*/
		http
			.exceptionHandling()
				.accessDeniedPage("/acessoNegado.xhtml")
				.and()
			.authorizeRequests()
				.antMatchers("/administracao/**").hasRole("ADMIN")
				.antMatchers("/images/**").permitAll()
				.antMatchers("/javax.faces.resource/**").permitAll()
				.antMatchers("/inicio.xhtml").permitAll()
				.antMatchers("/takeTip.xhtml").permitAll()
				.antMatchers("/resources/**").permitAll()
				.antMatchers("/pages/public/**").permitAll()
				.antMatchers("/padrao.xhtml").permitAll()
				.antMatchers("/login.xhtml").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login.xhtml")
				.failureUrl("/login.xhtml?erro")
				.permitAll()
				.defaultSuccessUrl("/pages/protected/homeAfterLogin.xhtml", true)
				.and()
			.logout()
				.logoutSuccessUrl("/login.xhtml?logout")
				.permitAll()
				.and()
			.csrf().disable();
	}
}