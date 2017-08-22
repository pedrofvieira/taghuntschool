
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

package br.cin.ufpe.taghuntschool.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import br.cin.ufpe.taghuntschool.dao.GeneralUsrDAO;
import br.cin.ufpe.taghuntschool.domain.GeneralUsr;
import br.cin.ufpe.taghuntschool.domain.RoleKind;
import br.cin.ufpe.taghuntschool.exception.ExceptionTaghuntSchool;
import br.cin.ufpe.taghuntschool.util.Utils;
import br.cin.ufpe.taghuntschool.util.SecurityUtil;
 
@Service
@Named
@RequestScoped
public class GeneralUsrService implements UserDetailsService {
 
	@Autowired
	@Inject
	private GeneralUsrDAO generalUsrDAO;
 
   	public void setGeneralUsrDAO(GeneralUsrDAO generalUsrDAO) {
		this.generalUsrDAO = generalUsrDAO;
	}

	@Override
    public GeneralUsr loadUserByUsername(final String username)  {
    	try {
			return generalUsrDAO.findByUsername(username);
		} catch (Exception e) {
			return null;
		}
    }
 
	public List<GeneralUsr> findAll(){
    	try {
			return generalUsrDAO.findAll();
		} catch (Exception e) {
			return null;
		}
    }
    
	public List<GeneralUsr> findAllInstructors(){
    	try {
			return generalUsrDAO.findAllInstructors();
		} catch (Exception e) {
			return null;
		}
    }
    
	public GeneralUsr findByEmail(String email){
    	try {
			return generalUsrDAO.findByEmail(email);
		} catch (Exception e) {
			return null;
		}
    }
    
    public List<GeneralUsr> findInstructorLikeName(String searchName){
    	try {
			return generalUsrDAO.findInstructorLikeName(searchName);
		} catch (Exception e) {
			return null;
		}
    }
	
    public GeneralUsr findAllStudentByKey(String key){
    	try {
			return generalUsrDAO.findAllStudentByKey(key);
		} catch (Exception e) {
			return null;
		}
    }
	
    @Transactional
    public void enableGeneralUsr(GeneralUsr user){
    	
    	user.setEnabled(true);

    	generalUsrDAO.update(user);
   }

    @Transactional
    public GeneralUsr update(GeneralUsr user) throws ExceptionTaghuntSchool{
    	
    	if(user.getUsername() != null)
    		user.setUsername(user.getUsername().toLowerCase());
    	return generalUsrDAO.update(user);
    	
   }

    @Transactional
    public void updatePassword(GeneralUsr user) throws ExceptionTaghuntSchool{
    	
    	user.setPassword(SecurityUtil.criptografa(user.getPassword()));
    	generalUsrDAO.update(user);
    	
   }

   @Transactional
    public void create(GeneralUsr user) throws ExceptionTaghuntSchool{
    	
    	if(user.getPrincipalRole() ==  null || user.getPrincipalRole().getId() == null){
        	user.setPrincipalRole(new RoleKind(Utils.ROLE_INSTRUCTOR_ID));
    	}
    	if(user.getUsername() != null)
    		user.setUsername(user.getUsername().toLowerCase());
    	if(user.getSessionKey() == null)
    		user.setSessionKey(SecurityUtil.base64Encode(user.getUsername()));
    	
    	if(user.getPassword() != null)
    		user.setPassword(SecurityUtil.criptografa(user.getPassword()));
    	   	
    	RoleKind roleKind = new RoleKind();
    	roleKind.setId(user.getPrincipalRole().getId());
    	List<RoleKind> roles = new ArrayList<RoleKind>();
    	roles.add(roleKind);
    	
    	user.setAuthorities(roles);
    	
    	generalUsrDAO.update(user);
    }

}