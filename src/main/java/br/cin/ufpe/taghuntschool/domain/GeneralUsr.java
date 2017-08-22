
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

package br.cin.ufpe.taghuntschool.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="general_usr")
@RequestScoped
@NamedQueries({
    @NamedQuery(name = "GeneralUsr.findAll", query = "SELECT u FROM GeneralUsr u"),
    @NamedQuery(name = "GeneralUsr.findAllStudentByKey", query="SELECT g FROM GeneralUsr g WHERE g.principalRole.id = 3 and g.sessionKey = :sessionKey"),
    @NamedQuery(name = "GeneralUsr.findAllInstructors", query = "SELECT u FROM GeneralUsr u WHERE u.principalRole.id = 2"),
    @NamedQuery(name = "GeneralUsr.findById", query = "SELECT u FROM GeneralUsr u WHERE u.id = :id"),
    @NamedQuery(name = "GeneralUsr.findInstructorLikeName", query = "SELECT u FROM GeneralUsr u WHERE u.principalRole.id = 2 and lower(trim(both ' ' from u.name)) LIKE :searchName"),
    @NamedQuery(name = "GeneralUsr.findByUsername", query = "SELECT u FROM GeneralUsr u WHERE u.username = :username"),
    @NamedQuery(name = "GeneralUsr.findByEmail", query = "SELECT u FROM GeneralUsr u WHERE u.email = :email"),
    @NamedQuery(name = "GeneralUsr.findByEnabled", query = "SELECT u FROM GeneralUsr u WHERE u.enabled = :enabled")
})
public class GeneralUsr implements Serializable, UserDetails {

	private static final long serialVersionUID = 8854814860558885739L;
	
	public static final String FIND_ALL_STUDENT_BY_KEY = "GeneralUsr.findAllStudentByKey";
	public static final String FIND_ALL_INSTRUCTORS = "GeneralUsr.findAllInstructors";
	public static final String FIND_BY_ID = "GeneralUsr.findById";
	public static final String FIND_INSTRUCTOR_LIKE_NAME = "GeneralUsr.findInstructorLikeName";
	public static final String FIND_BY_USERNAME = "GeneralUsr.findByUsername";
	public static final String FIND_BY_EMAIL = "GeneralUsr.findByEmail";
	public static final String FIND_BY_ENABLED = "GeneralUsr.findByEnabled";

	@Id
	@SequenceGenerator(name="GENERAL_USR_ID_GENERATOR", sequenceName="general_usr_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GENERAL_USR_ID_GENERATOR")
	@Column(name="id")
	private Integer id; 
	
	@Column(name="general_usr_key")
	private String sessionKey;

	@Size(max=100, message="O tamanho máximo para o campo Nome é 100 caracteres!")
	@Column(name="general_usr_name")
	private String name;
	
	@Size(max=30, message="O tamanho máximo para o campo Login é 30 caracteres!")
	@Column(name = "general_usr_login")
	private String username; 
	
	@Column(name = "general_usr_password")
	private String password; 
	
	@Size(max=100, message="O tamanho máximo para o campo E-mail é 100 caracteres!")
	@Column(name = "general_usr_email")
	private String email; 
	
	@Column(name = "general_usr_enabled")
	private boolean enabled;

	@JoinColumn(name = "principal_role_kind_fk_id", referencedColumnName = "id")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private RoleKind principalRole;
	
	@OneToMany(fetch=FetchType.EAGER) 
	@JoinTable(name="general_usr_role_kind", 
			   joinColumns = {@JoinColumn(name="general_usr_id_fk", referencedColumnName="id")}, 
			   inverseJoinColumns = {@JoinColumn(name="role_kind_id_fk", referencedColumnName="id")}) 
	private List<RoleKind> authorities;
	
	@Transient
	private String emailToConfirm;
	
	@Transient
	private String passwordToConfirm;
	
	public GeneralUsr(){
		
	}

	public GeneralUsr(String name, String sessionKey){
		this.name = name;
		this.sessionKey = sessionKey;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAuthorities(List<RoleKind> authorities) {
		this.authorities = authorities;
	}

	public String getEmailToConfirm() {
		return emailToConfirm;
	}

	public void setEmailToConfirm(String emailToConfirm) {
		this.emailToConfirm = emailToConfirm;
	}

	public String getPasswordToConfirm() {
		return passwordToConfirm;
	}

	public void setPasswordToConfirm(String senhaToConfirm) {
		this.passwordToConfirm = senhaToConfirm;
	}

	public RoleKind getPrincipalRole() {
		return principalRole;
	}

	public void setPrincipalRole(RoleKind principalRole) {
		this.principalRole = principalRole;
	}

	public RoleKind getAuthorityById(Integer idRoleKind){
		if(this.authorities != null){
			for(RoleKind item : this.authorities){
				if(item.getId().equals(idRoleKind)){
					return item;
				}
			}
		}
		return null;
	}
	
	public void addAuthorityById(Integer idRoleKind){
		if(getAuthorityById(idRoleKind) == null){
			RoleKind roleKindaux = new RoleKind();
			roleKindaux.setId(idRoleKind);
			this.authorities.add(roleKindaux);
		}
	}
	
	public void removeAuthorityById(Integer idRoleKind){
		RoleKind roleKindaux = getAuthorityById(idRoleKind);
		if(roleKindaux != null){
			this.authorities.remove(roleKindaux);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeneralUsr other = (GeneralUsr) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
